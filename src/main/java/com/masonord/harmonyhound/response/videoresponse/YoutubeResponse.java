package com.masonord.harmonyhound.response.videoresponse;

import lombok.Data;
import java.util.List;

@Data
public class YoutubeResponse {
    private String caption;
    private Image image;
    private List<Actions> actions;
}
