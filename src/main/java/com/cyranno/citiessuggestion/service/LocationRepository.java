package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.model.City;

import java.util.Set;

public interface LocationRepository {
    Set<City> getCities();

    String getStateCode(String countryCode, String adminCode1);

    String getCountryName(String countryCode);
}
