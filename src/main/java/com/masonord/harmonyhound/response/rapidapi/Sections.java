package com.masonord.harmonyhound.response.rapidapi;

import lombok.Data;
import java.util.List;

@Data
public class Sections {
    private List<Metadata> metadata;
    private List<Metapages> metapages;
    private String tabname;
    private String type;
    private String url;
    private String youtubeurl;
}
