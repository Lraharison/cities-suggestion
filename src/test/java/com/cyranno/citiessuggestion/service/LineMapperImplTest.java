package com.cyranno.citiessuggestion.service;


import com.cyranno.citiessuggestion.exception.MapperCityException;
import com.cyranno.citiessuggestion.model.City;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineMapperImplTest {
    private LineMapper lineMapper;

    @Before
    public void setUp() {
        this.lineMapper = new LineMapperImpl();
    }

    @Test
    public void shouldCreateCityFromLine() {
        String line = "5881791\tTana\tTana\tTana,Antananarivo,Tananarive\t-18.9136800\t47.5361300\tP\tPPL\tCA\t\t02\t5957659\t\t\t151683\t\t114\tMada/Mada\t2013-04-22";

        City city = lineMapper.convertLineToCity(line);

        assertThat(city.getName()).isEqualTo("Tana");
        assertThat(city.getAlternateNames()).contains("Tana", "Antananarivo", "Tananarive");
        assertThat(city.getLatitude()).isEqualTo(-18.9136800);
        assertThat(city.getLongitude()).isEqualTo(47.5361300);
    }

    @Test(expected = MapperCityException.class)
    public void shouldThrowMapperCityExceptionWhenLineIsNotCorrect() {
        String line = "";

        lineMapper.convertLineToCity(line);
    }
}