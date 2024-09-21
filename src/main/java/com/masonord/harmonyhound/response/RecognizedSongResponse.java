package com.masonord.harmonyhound.response;

import lombok.Data;

import java.util.List;

@Data
public class RecognizedSongResponse {
    private Location location;
    private List<Matches> matches;
    private String tagid;
    private long timestamp;
    private String timezone;
    private Track track;
}

@Data
class Location {
    private double accuracy;
}

@Data
class Matches {
    private double frequencyskew;
    private String id;
    private double offset;
    private double timeskew;
}

@Data
class Track {
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

class Artists {
    private String adamid;
    private String id;
}

class Genres {
    private String primary;
}

class Highlightsurls {
    private String artisthighlightsurl;
}

class Hub {
    private List<Actions> actions;
    private String displayname;
    private boolean explicit;
    private String image;
}

@Data
class Images {
    private String background;
    private String coverart;
    private String coverarthq;
    private String joecolor;
}

@Data
class MyShazam {
    private Apple apple;
}

@Data
class Sections {
    private List<Metadata> metadata;
    private List<Metapages> metapages;
    private String tabname;
    private String type;
    private String url;
    private String youtubeurl;
}

@Data
class Share {
    private String avatar;
    private String href;
    private String html;
    private String image;
    private String snapchat;
    private String subject;
    private String text;
    private String twitter;
}

@Data
class UrlParams {
    private String trackartist;
    private String tracktitle;
}

@Data
class Actions {
    private String id;
    private String name;
    private String type;
    private String uri;
}

@Data
class Apple {
    private List<Actions> actions;
}

@Data
class Metadata {
    private String text;
    private String title;
}

@Data
class Metapages {
    private String caption;
    private String image;
}

