package com.masonord.harmonyhound.response.rapidapi;

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