package com.masonord.harmonyhound.response;

import lombok.Data;

import java.util.List;

@Data
public class GetAudioRecognitionResult {

    private boolean success;
    private String error;
    private String result;
    private List<Recognized> data;
}

@Data
class Recognized {
    private int confidence;
    private String time;
    private List<Tracks> tracks;
}

@Data
class Tracks {
    private String track_name;
    private String artist_name;
    private String album_name;
    private int album_year;
}