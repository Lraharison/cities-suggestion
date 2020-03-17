package com.cyranno.citiessuggestion.di;

import com.cyranno.citiessuggestion.service.LineMapper;
import com.cyranno.citiessuggestion.service.LineMapperImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class LineMapperModule {

    @Provides
    @Singleton
    public LineMapper lineMapper() {
        return new LineMapperImpl();
    }
}
