package com.masonord.harmonyhound.response.rapidapi;

import lombok.Data;

import java.util.List;

@Data
public class Hub {
    private List<Actions> actions;
    private List<Providers> providers;
    private String displayname;
    private boolean explicit;
    private String image;
    private String type;
}
