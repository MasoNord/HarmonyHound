package com.masonord.harmonyhound.response;

import lombok.Data;
import java.util.List;

@Data
public class GetAudioRecognitionResult {
    private boolean success;
    private String error;
    private String result;
    private List<Recognized> data;
    private List<Recognized> raw;
}