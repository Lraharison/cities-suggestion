package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.di.AwsLambdaComponent;
import com.cyranno.citiessuggestion.di.DaggerAwsLambdaComponent;
import com.cyranno.citiessuggestion.model.City;
import com.cyranno.citiessuggestion.model.Suggestion;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class ScoringServiceImplTest {

    private AwsLambdaComponent awsLambdaComponent;
    private ScoringServiceImpl scoringService;

    @Before
    public void setUp() {
        this.awsLambdaComponent = DaggerAwsLambdaComponent.builder().build();
        this.scoringService = (ScoringServiceImpl) this.awsLambdaComponent.scoringService();
    }

    @Test
    public void shouldCalculateNameScore() {
        final City city = new City("Antananarivo", new HashSet<>(Arrays.asList("Tananarive", "Tana")), "MG", "101", -18.91368, 47.53613);

        final double score0 = this.scoringService.calculateNameScore(city, "Tulear");
        final double score1 = this.scoringService.calculateNameScore(city, "Tananarivou");
        final double score2 = this.scoringService.calculateNameScore(city, "Tananariv");
        final double score3 = this.scoringService.calculateNameScore(city, "Tananarive");
        final double score4 = this.scoringService.calculateNameScore(city, "Antananarivo");

        assertThat(score4).isEqualTo(score3);
        assertThat(score3).isGreaterThan(score2);
        assertThat(score2).isGreaterThan(score1);
        assertThat(score1).isGreaterThan(score0);
    }


    @Test
    public void shouldCalculateLocationScore() {
        final City city = new City("Antananarivo", new HashSet<>(Arrays.asList("Tananarive", "Tana")), "MG", "101", -18.91368, 47.53613);

        final double score1 = this.scoringService.calculateLocationScore(city, -18.78766, 47.49685);
        final double score2 = this.scoringService.calculateLocationScore(city, -20.53034, 47.24344);
        final double score3 = this.scoringService.calculateLocationScore(city, -23.3541, 43.66705);

        assertThat(score1).isGreaterThan(score2);
        assertThat(score2).isGreaterThan(score3);
    }

    @Test
    public void shouldCalculateLocationScoreEvenIfLocationIsVeryFar() {
        final City city = new City("Antananarivo", new HashSet<>(Arrays.asList("Tananarive", "Tana")), "MG", "101", -18.91368, 47.53613);

        final double score = this.scoringService.calculateLocationScore(city, 46.81228, -71.21454);

        assertThat(score).isLessThan(0.2);
    }

    @Test
    public void shouldCalculateScore() {
        final City city = new City("Antananarivo", new HashSet<>(Arrays.asList("Tananarive", "Tana")), "MG", "101", -18.91368, 47.53613);

        final Suggestion suggestion = this.scoringService.calculateScore(city, "tana", -18.26731, 47.12359);

        assertThat(suggestion.getLatitude()).isEqualTo(city.getLatitude());
        assertThat(suggestion.getLongitude()).isEqualTo(city.getLongitude());
        assertThat(suggestion.getName()).contains(city.getName());
        assertThat(suggestion.getScore()).isGreaterThan(0.5);
    }

    @Test
    public void shouldCalculateSuggestions() {

        final Set<Suggestion> suggestions = this.scoringService.calculate("tana", -18.91368, 47.53600);

        final Iterator<Suggestion> iterator = suggestions.iterator();
        final Suggestion suggestion1 = iterator.next();
        final Suggestion suggestion2 = iterator.next();
        final Suggestion suggestion3 = iterator.next();
        final Suggestion suggestion4 = iterator.next();

        assertThat(suggestion1.getScore()).isGreaterThan(suggestion2.getScore());
        assertThat(suggestion2.getScore()).isGreaterThan(suggestion3.getScore());
        assertThat(suggestion3.getScore()).isGreaterThan(suggestion4.getScore());
    }
}