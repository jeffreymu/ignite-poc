package me.jeff.ignitepoc.service;

import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.infra.repo.CityRepository;
import me.jeff.ignitepoc.infra.repo.CountryRepository;
import me.jeff.ignitepoc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SqlDataService {

    private CityRepository cityDao;

    public CityDTO getCityByID(int cityId) {
        Cache.Entry<CityKey, City> entry = cityDao.findById(cityId);
        return new CityDTO(entry.getKey(), entry.getValue());
    }


}
