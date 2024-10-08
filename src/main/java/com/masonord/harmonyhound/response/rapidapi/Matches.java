package com.masonord.harmonyhound.response.rapidapi;

import lombok.Data;

@Data
public class Matches {
    private double frequencyskew;
    private String id;
    private double offset;
    private double timeskew;
}