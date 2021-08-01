package me.jeff.ignitepoc.infra.repo;

import me.jeff.ignitepoc.model.Country;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import java.util.List;

@RepositoryConfig(cacheName = "Country")
@Repository
public interface CountryRepository extends IgniteRepository<Country, String> {

    public List<Cache.Entry<String, Country>> findByPopulationGreaterThanEqualOrderByPopulationDesc(int population);
}