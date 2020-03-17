package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.exception.LambdaInitialisationException;
import com.cyranno.citiessuggestion.model.City;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class LocationRepositoryImpl implements LocationRepository {
    public static final String CANADA_COUNTRY_CODE = "CA";
    private static final String CITIES_DATA_TSV = "cities_canada-usa.tsv";
    private final LineMapper lineMapper;
    private Set<City> cities;
    private Map<String, String> countries;
    private Map<String, String> canadaProvinces;

    @Inject
    public LocationRepositoryImpl(final LineMapper lineMapper) {
        this.lineMapper = lineMapper;
        this.loadResource();
        this.loadCountries();
        this.loadCanadaProvinces();
    }

    @Override
    public Set<City> getCities() {
        return this.cities;
    }

    @Override
    public String getStateCode(final String countryCode, final String adminCode1) {
        if (countryCode.equalsIgnoreCase(CANADA_COUNTRY_CODE)) {
            return this.canadaProvinces.get(adminCode1);
        }
        return adminCode1;
    }

    @Override
    public String getCountryName(final String countryCode) {
        return this.countries.get(countryCode) != null ? this.countries.get(countryCode) : countryCode;
    }

    private void loadResource() {
        log.info("Loading resource");
        final File fileResource = new File(this.getClass().getClassLoader().getResource(CITIES_DATA_TSV).getFile());

        try (final Stream<String> stream = Files.lines(fileResource.toPath())) {
            this.cities = stream
                    .skip(1)
                    .map(this.lineMapper::convertLineToCity)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (final Exception e) {
            log.error("Error on reading file", e);
            throw new LambdaInitialisationException(e);
        }
    }

    private void loadCountries() {
        log.info("Loading countries");
        this.countries = ImmutableMap.of("CA", "CANADA", "US", "USA");
    }

    private void loadCanadaProvinces() {
        log.info("Loading Canada provinces");
        this.canadaProvinces = Stream.of(
                new AbstractMap.SimpleEntry<>("01", "AB"),
                new AbstractMap.SimpleEntry<>("02", "BC"),
                new AbstractMap.SimpleEntry<>("03", "MB"),
                new AbstractMap.SimpleEntry<>("04", "NB"),
                new AbstractMap.SimpleEntry<>("05", "NL"),
                new AbstractMap.SimpleEntry<>("13", "NT"),
                new AbstractMap.SimpleEntry<>("07", "NS"),
                new AbstractMap.SimpleEntry<>("14", "NU"),
                new AbstractMap.SimpleEntry<>("08", "ON"),
                new AbstractMap.SimpleEntry<>("09", "PE"),
                new AbstractMap.SimpleEntry<>("10", "QC"),
                new AbstractMap.SimpleEntry<>("11", "SK"),
                new AbstractMap.SimpleEntry<>("12", "YT"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
