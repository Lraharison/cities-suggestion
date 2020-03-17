package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.model.City;
import com.cyranno.citiessuggestion.model.Suggestion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ScoringServiceImpl implements ScoringService {
    private static final double NAME_SCORE_CONSTANT = 0.8;
    private static final double LOCATION_SCORE_CONSTANT = 0.2;

    private final LocationRepository locationRepository;

    @Inject
    public ScoringServiceImpl(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Set<Suggestion> calculate(final String queryName, final double queryLatitude, final double queryLongitude) {
        log.info("calculate");
        return this.locationRepository.getCities().stream()
                .map(city -> this.calculateScore(city, queryName, queryLatitude, queryLongitude))
                .sorted(Comparator.comparingDouble(Suggestion::getScore).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    protected Suggestion calculateScore(final City city, final String queryName, final double queryLatitude, final double queryLongitude) {
        final double nameScore = this.calculateNameScore(city, queryName);
        final double locationScore = this.calculateLocationScore(city, queryLatitude, queryLongitude);
        final double score = (nameScore * NAME_SCORE_CONSTANT + locationScore * LOCATION_SCORE_CONSTANT);
        final String name = String.format("%s,%s,%s", city.getName(), this.locationRepository.getStateCode(city.getCountry(), city.getState()), this.locationRepository.getCountryName(city.getCountry()));
        return new Suggestion(name, city.getLatitude(), city.getLongitude(), score);
    }

    protected double calculateNameScore(final City city, final String queryName) {
        double nameScore = this.calculateNameScore(city.getName(), queryName);

        for (final String name : city.getAlternateNames()) {
            final double alternateNameScore = this.calculateNameScore(name, queryName);
            if (alternateNameScore > nameScore) {
                nameScore = alternateNameScore;
            }
        }
        return nameScore;
    }

    private double calculateNameScore(final String name, final String queryName) {
        return new JaroWinklerDistance().apply(queryName, name);
    }

    protected double calculateLocationScore(final City city, final double queryLatitude, final double queryLongitude) {

        final double diffLatitude = Math.abs(city.getLatitude() - queryLatitude);
        final double diffLongitude = Math.abs(city.getLongitude() - queryLongitude);
        final double score = 10 - (diffLatitude + diffLongitude) / 2;
        if (score > 0) {
            return Math.round(score) / (double) 10;
        }
        return 0;
    }
}
