package com.masonord.harmonyhound.response;

import lombok.Data;

@Data
public class GetAudioTokenResponse {
    private boolean success;
    private String error;
    private String token;
    private int start_time;
    private int time_len;
    private String job_status;
}
