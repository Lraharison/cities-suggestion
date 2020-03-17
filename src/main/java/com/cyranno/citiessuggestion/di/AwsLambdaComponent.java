package com.cyranno.citiessuggestion.di;

import com.cyranno.citiessuggestion.service.LocationRepository;
import com.cyranno.citiessuggestion.service.ScoringService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ScoreServiceModule.class, CityRepositoryModule.class, LineMapperModule.class})
public interface AwsLambdaComponent {

    ScoringService scoringService();

    LocationRepository cityRepository();
}
