package com.masonord.harmonyhound.response.videoresponse;

import lombok.Data;

@Data
public class Actions {
    private String name;
    private String type;
    private Share share;
    private String uri;
}
