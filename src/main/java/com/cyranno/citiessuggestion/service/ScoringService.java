package com.cyranno.citiessuggestion.service;

import com.cyranno.citiessuggestion.model.Suggestion;

import java.util.Set;

public interface ScoringService {
    Set<Suggestion> calculate(String queryName, double queryLatitude, double queryLongitude);
}
