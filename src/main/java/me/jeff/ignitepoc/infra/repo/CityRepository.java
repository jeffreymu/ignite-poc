package me.jeff.ignitepoc.infra.repo;

import me.jeff.ignitepoc.model.City;
import me.jeff.ignitepoc.model.CityKey;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.Query;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;

@RepositoryConfig(cacheName = "City")
@Repository
public interface CityRepository extends IgniteRepository<City, CityKey> {

    @Query("SELECT * FROM City WHERE id = ?")
    Cache.Entry<CityKey, City> findById(int id);
}
