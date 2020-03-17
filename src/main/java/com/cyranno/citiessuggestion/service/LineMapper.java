package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.model.City;

public interface LineMapper {
    City convertLineToCity(String line);
}
