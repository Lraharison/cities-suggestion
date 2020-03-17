package com.cyranno.citiessuggestion.di;

import com.cyranno.citiessuggestion.service.LocationRepository;
import com.cyranno.citiessuggestion.service.ScoringService;
import com.cyranno.citiessuggestion.service.ScoringServiceImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ScoreServiceModule {

    @Provides
    @Singleton
    public ScoringService scoringService(final LocationRepository locationRepository) {
        return new ScoringServiceImpl(locationRepository);
    }
}
