package com.masonord.harmonyhound.response;

import lombok.*;

@Data
public class FilePathResponse {
    private String ok;
    private Result result;


}

@Data
class Result {
    private String file_id;
    private String file_unique_id;
    private long file_size;
    private String file_path;
}
