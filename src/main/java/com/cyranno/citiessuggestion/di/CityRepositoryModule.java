package com.cyranno.citiessuggestion.di;

import com.cyranno.citiessuggestion.service.LineMapper;
import com.cyranno.citiessuggestion.service.LocationRepository;
import com.cyranno.citiessuggestion.service.LocationRepositoryImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class CityRepositoryModule {

    @Provides
    @Singleton
    public LocationRepository CityRepository(final LineMapper lineMapper) {
        return new LocationRepositoryImpl(lineMapper);
    }
}
