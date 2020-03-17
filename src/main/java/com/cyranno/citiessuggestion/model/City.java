package com.cyranno.citiessuggestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class City {
    private String name;
    private Set<String> alternateNames;
    private String country;
    private String state;
    private double latitude;
    private double longitude;
}