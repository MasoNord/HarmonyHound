package com.masonord.harmonyhound.response;

import lombok.Data;

@Data
public class Tracks {
    private String track_name;
    private String artist_name;
    private String album_name;
    private int album_year;
}
