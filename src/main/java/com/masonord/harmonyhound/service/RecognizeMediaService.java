package com.masonord.harmonyhound.service;

import com.google.gson.Gson;
import com.masonord.harmonyhound.response.rapidapi.RecognizedSongResponse;
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
    private final String rapidapi;
    private final Gson gson;

    public RecognizeMediaService(@Value("${shazam.api-key}") String apikey,
                                 @Value("${rapid.api}") String rapidapi) {
        this.apikey = apikey;
        this.rapidapi = rapidapi;
        this.gson = new Gson();
    }

    public RecognizedSongResponse recognizeAudio(String fileLink) throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URI("https://shazam-song-recognition-api.p.rapidapi.com/recognize/url?url=" + fileLink);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("x-rapidapi-key", apikey)
                .header("x-rapidapi-host", rapidapi)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), RecognizedSongResponse.class);
    }
}
