package com.masonord.harmonyhound.response;

import lombok.Data;

import java.util.List;

@Data
public class Recognized {
    private String time;
    private int confidence;
    private List<List<String>> tracks;
    private int aid;
}
