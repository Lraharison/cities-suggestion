package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.di.AwsLambdaComponent;
import com.cyranno.citiessuggestion.di.DaggerAwsLambdaComponent;
import com.cyranno.citiessuggestion.model.City;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationRepositoryImplTest {

    private AwsLambdaComponent awsLambdaComponent;
    private LocationRepositoryImpl cityRepository;

    @Before
    public void setUp() {
        this.awsLambdaComponent = DaggerAwsLambdaComponent.builder().build();
        this.cityRepository = (LocationRepositoryImpl) this.awsLambdaComponent.cityRepository();
    }

    @Test
    public void shouldLoadResourceOnInstantiation() {
        assertThat(this.cityRepository.getCities().size()).isEqualTo(4);
        final City tanaCity = this.cityRepository.getCities().iterator().next();
        assertThat(tanaCity.getName()).isEqualTo("Antananarivo");
        assertThat(tanaCity.getAlternateNames()).contains("Antananarivu", "Tananarive");
        assertThat(tanaCity.getCountry()).isEqualTo("MG");
        assertThat(tanaCity.getState()).isEqualTo("11");
        assertThat(tanaCity.getLatitude()).isEqualTo(-18.91368);
        assertThat(tanaCity.getLongitude()).isEqualTo(47.53613);
    }

    @Test
    public void shouldGetCanadaStateCode() {

        final String quebecState = this.cityRepository.getStateCode("CA", "10");

        assertThat(quebecState).isEqualTo("QC");
    }

    @Test
    public void shouldAdminCodeIfCountryNotCA() {

        final String mgState = this.cityRepository.getStateCode("MG", "10");

        assertThat(mgState).isEqualTo("10");
    }

    @Test
    public void shouldReturnCountryName() {

        final String canadaCountry = this.cityRepository.getCountryName("CA");
        final String madaCountry = this.cityRepository.getCountryName("MG");

        assertThat(canadaCountry).isEqualTo("CANADA");
        assertThat(madaCountry).isEqualTo("MG");
    }
}
