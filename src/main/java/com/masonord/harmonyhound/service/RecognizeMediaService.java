package com.masonord.harmonyhound.service;

import com.google.gson.Gson;
import com.masonord.harmonyhound.response.rapidapi.RecognizedSongResponse;
import com.masonord.harmonyhound.util.EnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RecognizeMediaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RecognizeMediaService.class);
    private final String apikey;
    private final String rapidapi;

    public RecognizeMediaService() {
        this.apikey = EnvUtil.getValue("shazam.api-key");
        this.rapidapi = EnvUtil.getValue("rapid.api");
    }

    /**
     * Recognizes the audio file by its Google Drive share link
     * It performs a GET request to rapid api service
     *
     * For more information, please visit the official documentation
     * <a href="https://rapidapi.com/dashydata-dashydata-default/api/shazam-song-recognition-api/playground/apiendpoint_1ca56af7-705d-4e91-8a0a-5735134e6dbf">...</a>
     *
     * @param fileLink Google Drive share link
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */

    public RecognizedSongResponse recognizeAudio(String fileLink) throws IOException, InterruptedException, URISyntaxException {
        Gson gson = new Gson();
        URI uri = new URI("https://shazam-song-recognition-api.p.rapidapi.com/recognize/url?url=" + fileLink);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("x-rapidapi-key", apikey)
                .header("x-rapidapi-host", rapidapi)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        LOGGER.atInfo().setMessage("The recognize audio command has been completed successfully").log();

        return gson.fromJson(response.body(), RecognizedSongResponse.class);
    }
}
