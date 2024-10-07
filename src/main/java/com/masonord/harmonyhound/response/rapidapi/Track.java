package com.masonord.harmonyhound.response.rapidapi;

import lombok.Data;

import java.util.List;

@Data
public class Track {
    private String albumadamid;
    private List<Artists> artists;
    private Genres genres;
    private Highlightsurls highlightsurls;
    private Hub hub;
    private Images images;
    private String isrc;
    private String key;
    private String layout;
    private MyShazam myshazam;
    private String relatedtracksurl;
    private List<Sections> sections;
    private Share share;
    private String subtitle;
    private String title;
    private String type;
    private String url;
    private UrlParams urlparams;
}