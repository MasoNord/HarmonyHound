package com.masonord.harmonyhound.response.rapidapi;

import lombok.Data;

import java.util.List;

@Data
public class Providers {
    private List<Actions> actions;
    private String caption;
    private Images images;
    private String type;
}
