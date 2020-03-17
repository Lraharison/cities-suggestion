package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.exception.MapperCityException;
import com.cyranno.citiessuggestion.model.City;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class LineMapperImpl implements LineMapper {

    public static final String COLUMN_SEPARATOR = "\t";
    public static final String NAME_SEPARATOR = ",";

    @Override
    public City convertLineToCity(final String line) {
        try {
            final String[] columns = line.split(COLUMN_SEPARATOR);
            final String name = columns[1];
            final Set<String> alternateNames = new HashSet<>(Arrays.asList(columns[3].split(NAME_SEPARATOR)));
            final String country = columns[8];
            final String state = columns[10];
            final double latitude = Double.parseDouble(columns[4]);
            final double longitude = Double.parseDouble(columns[5]);
            return new City(name, alternateNames, country, state, latitude, longitude);
        } catch (final RuntimeException e) {
            log.error("Error occurred  when mapping line : {}", line);
            throw new MapperCityException(e);
        }
    }
}
