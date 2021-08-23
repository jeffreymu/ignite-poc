package me.jeff.ignitepoc.service;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.model.City;
import me.jeff.ignitepoc.model.CityKey;
import me.jeff.ignitepoc.model.Country;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.util.Collections;

@Slf4j
@Component
public class IgniteCompleteService {

    @Autowired
    private Ignite ignite;

    public void getPutCountry(String name) {
        IgniteCache<String, Country> countryCache = ignite.cache("Country");
        Country country = countryCache.get(name);
        if (country == null)
            throw new RuntimeException("Can't find this resource.");

        log.info("Current President: " + country.getHeadOfState());
        country.setHeadOfState("Donald Trump");
        countryCache.put(name, country);
        log.info("New President: " + countryCache.get(name).getHeadOfState());
    }

    public void updateSingleField(String name) {
        IgniteCache<String, Country> countryCache = ignite.cache("Country");
        Country unitedKingdom = countryCache.get(name);
        log.info("Current Prime Minister: " + unitedKingdom.getHeadOfState());
        String newPrimeMinister = countryCache.<String, BinaryObject>withKeepBinary().
                invoke(name, new CountryEntryProcessor());
        log.info("New Prime Minister: " + newPrimeMinister);
    }

    private static class CountryEntryProcessor implements EntryProcessor<String, BinaryObject, String> {

        @Override public String process(MutableEntry<String, BinaryObject> entry,
                                        Object... arguments) throws EntryProcessorException {

            BinaryObjectBuilder builder = entry.getValue().toBuilder();
            builder.setField("headofstate", "Boris Johnson");
            entry.setValue(builder.build());
            return entry.getValue().field("headofstate");
        }
    }


    public void updateCitiesPopulation(String cacheName, String code, int num) {
        IgniteCache<CityKey, City> cityCache = ignite.cache(cacheName);
        IgniteTransactions txs = ignite.transactions();

        CityKey newYorkPK = new CityKey(1, code);
        CityKey losAngelesPK = new CityKey(2, code);

        int migratedResidentsNumber = 10_000;
        migratedResidentsNumber = num;

        try (Transaction tx = txs.txStart(TransactionConcurrency.PESSIMISTIC, TransactionIsolation.REPEATABLE_READ)) {
            City newYork = cityCache.get(newYorkPK);
            City losAngeles = cityCache.get(losAngelesPK);

            log.info("Population Before [NY=" + newYork.getPopulation() + ", LA=" + losAngeles.getPopulation() + "]");
            newYork.setPopulation(newYork.getPopulation() - migratedResidentsNumber);
            losAngeles.setPopulation(losAngeles.getPopulation() + migratedResidentsNumber);
            cityCache.put(newYorkPK, newYork);
            cityCache.put(losAngelesPK, losAngeles);
            tx.commit();
            log.info("Transaction has been committed");
        }

        City newYork = cityCache.get(newYorkPK);
        City losAngeles = cityCache.get(losAngelesPK);
        log.info("Population After [NY=" + newYork.getPopulation() + ", LA=" + losAngeles.getPopulation() + "]");
    }

    public void calculateAverageCountryPopulation(String countryCode) {
        //Getting a cluster node and a partition that store a primary copy of all the cities with 'countryCode'.
        Affinity<String> affinity = ignite.affinity("Country");

        ClusterNode node = affinity.mapKeyToNode(countryCode);

        int partition = affinity.partition(countryCode);

        //Scheduling the task for calculation on that primary node.
        int[] result = ignite.compute().affinityCall(Collections.singleton("Country"), partition, new AvgPopulationCalculationTask(
                countryCode, partition));

        log.info("Finished task execution [country = " + countryCode + ", avgPopulation=" +
                result[0] + ", citiesNumber=" + result[1] + "]");
    }

    private static class AvgPopulationCalculationTask implements IgniteCallable<int[]> {
        @IgniteInstanceResource
        private Ignite ignite;

        private String countryCode;
        private int partition;

        public AvgPopulationCalculationTask(String countryCode, int partition) {
            this.partition = partition;
            this.countryCode = countryCode;
        }

        @Override public int[] call() throws Exception {
            log.info("Calculating average [country=" + countryCode + ", partition=" + partition +
                    ", node = " + ignite.cluster().localNode().id() + "]");

            //Accessing object records with BinaryObject interface that avoids a need of deserialization and doesn't
            //require to keep models' classes on the server nodes.
            IgniteCache<BinaryObject, BinaryObject> cities = ignite.cache("City").withKeepBinary();

            ScanQuery<BinaryObject, BinaryObject> scanQuery = new ScanQuery<>(partition,
                    new IgniteBiPredicate<BinaryObject, BinaryObject>() {
                        @Override public boolean apply(BinaryObject key, BinaryObject object) {
                            //Filtering out cities of other countries that stored in the same partition.
                            return key.field("CountryCode").equals(countryCode);
                        }
                    });

            //Extra hint to Ignite that the data is available locally.
            scanQuery.setLocal(true);

            //Calculation average population across the cities.
            QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> cursor = cities.query(scanQuery);

            long totalPopulation = 0;
            int citiesNumber = 0;

            for (Cache.Entry<BinaryObject, BinaryObject> entry: cursor) {
                totalPopulation += (int)entry.getValue().field("Population");
                citiesNumber++;
            }
            log.info("citiesNumber= " + citiesNumber);
            log.info("totalPopulation= " + totalPopulation);
            return citiesNumber == 0 ? null : new int[] {(int)(totalPopulation/citiesNumber), citiesNumber};
        }
    }

    public void subscribeForDataUpdates() {
        ContinuousQuery<BinaryObject, BinaryObject> query = new ContinuousQuery<>();
        query.setLocalListener(new ChangesListener());
        query.setRemoteFilterFactory(new PopulationChangesFilter());
        ignite.cache("City").withKeepBinary().query(query);
        log.info("Subscribed for the notifications");
    }

    private static class ChangesListener implements CacheEntryUpdatedListener<BinaryObject, BinaryObject> {
        @Override public void onUpdated(
                Iterable<CacheEntryEvent<? extends BinaryObject, ? extends BinaryObject>> events) throws CacheEntryListenerException {

            for (CacheEntryEvent<? extends BinaryObject, ? extends BinaryObject> event : events) {
                CityKey key = event.getKey().deserialize();
                City value = event.getValue().deserialize();

                log.info("City record has been changed [key=" + key + ", value = " + value + ']');
            }
        }
    }

    private static class PopulationChangesFilter implements Factory<CacheEntryEventFilter<BinaryObject, BinaryObject>> {
        @Override public CacheEntryEventFilter<BinaryObject, BinaryObject> create() {
            return new CacheEntryEventFilter<BinaryObject, BinaryObject>() {
                @Override
                public boolean evaluate(CacheEntryEvent<? extends BinaryObject, ? extends BinaryObject> e) {
                    // Notify the application only if the population has been changes.
                    if (!e.getOldValue().<Integer>field("population").equals(
                            e.getValue().<Integer>field("population")))
                        return true;

                    // Don't send a notification in all other cases
                    return false;
                }
            };
        }
    }
    
}
