package com.masonord.harmonyhound.response.telegram;

import lombok.Data;

@Data
public class Result {
    private String file_id;
    private String file_unique_id;
    private long file_size;
    String file_path;
}
