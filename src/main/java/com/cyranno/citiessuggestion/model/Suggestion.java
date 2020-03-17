package com.cyranno.citiessuggestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suggestion {
    private String name;
    private double latitude;
    private double longitude;
    private double score;
}
