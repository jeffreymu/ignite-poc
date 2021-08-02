package me.jeff.ignitepoc.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class IgniteClientDataHelper {

    private final static String CITY_INSERT_SQL = "INSERT INTO MyCity (id, name) VALUES (?, ?)";
    private final static String PERSON_INSERT_SQL = "INSERT INTO Person (id, name, city_id) VALUES (?, ?, ?)";

    protected static void cleanup(ClientCache cityCache, ClientCache personCache) {
        cityCache.clear();
        personCache.clear();
    }

    public static void insertData(ClientCache cityCache, ClientCache personCache) {

        SqlFieldsQuery query = new SqlFieldsQuery(CITY_INSERT_SQL);
        cityCache.query(query.setArgs(1, "Forest Hill")).getAll();
        cityCache.query(query.setArgs(2, "Denver")).getAll();
        cityCache.query(query.setArgs(3, "St. Petersburg")).getAll();
        log.info("Insert MyCity cache: " + cityCache.size(CachePeekMode.PRIMARY));

        query = new SqlFieldsQuery(PERSON_INSERT_SQL);
        personCache.query(query.setArgs(1, "John Doe", 3)).getAll();
        personCache.query(query.setArgs(2, "Jane Roe", 2)).getAll();
        personCache.query(query.setArgs(3, "Mary Major", 1)).getAll();
        personCache.query(query.setArgs(4, "Richard Miles", 2)).getAll();
        log.info("Insert person cache: " + personCache.size(CachePeekMode.PRIMARY));
    }

    private final static String QUERY_SQL_1 = "SELECT p.name, c.name " +
            " FROM Person p, MyCity c WHERE p.city_id = c.id";
    public static void queryData(ClientCache cityCache) {

        SqlFieldsQuery query = new SqlFieldsQuery(QUERY_SQL_1);
        FieldsQueryCursor<List<?>> cursor = cityCache.query(query);
        Iterator<List<?>> iterator = cursor.iterator();
        log.info("RESULT: ");
        while (iterator.hasNext()) {
            List<?> row = iterator.next();
            log.info("##  " + row.get(0) + ", " + row.get(1));
        }
    }
    
}
