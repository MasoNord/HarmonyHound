package com.masonord.harmonyhound.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masonord.harmonyhound.response.FilePathResponse;
import com.masonord.harmonyhound.util.FileSystemUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// TODO: add java doc

@Component
public class RecognizeMediaService {
    private final String apikey;
    private final String url;
    private final String rapidapi;
    private final ObjectMapper objectMapper;

    @Autowired
    private MediaUtil mediaUtil;

    @Autowired
    private FileSystemUtil fileSystemUtil;

    public RecognizeMediaService(@Value("${shazam.api-key}") String apikey,
                                 @Value("${shazam.url}") String url,
                                 @Value("${rapid.api}") String rapidapi) {
        this.apikey = apikey;
        this.url = url;
        this.rapidapi = rapidapi;
        this.objectMapper = new ObjectMapper();
    }

    public String recognizeAudio(String chatId, FilePathResponse filePathResponse) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://shazam-song-recognition-api.p.rapidapi.com/recognize/url?url=https://drive.google.com/file/d/1PpRMf-N864zVwIePXDprDWrh5lmfdN9O/view?usp=drive_link"))
                .header("x-rapidapi-key", "7ef5347179msh324744296b9e5e8p1e55e2jsnbc0c396235f4")
                .header("x-rapidapi-host", "shazam-song-recognition-api.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
