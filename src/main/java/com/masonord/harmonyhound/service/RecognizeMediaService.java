package com.masonord.harmonyhound.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masonord.harmonyhound.response.FilePathResponse;
import com.masonord.harmonyhound.response.GetAudioRecognitionResult;
import com.masonord.harmonyhound.response.GetAudioTokenResponse;
import com.masonord.harmonyhound.util.FileSystemUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecognizeMediaService {

    private final String apikey;
    private final ObjectMapper objectMapper;
    private final String url;

    @Autowired
    private MediaUtil mediaUtil;

    @Autowired
    private FileSystemUtil fileSystemUtil;

    public RecognizeMediaService(@Value("${audiotag.api-key}") String apikey,
                                 @Value("${audiotag-url}") String url) {
        this.apikey = apikey;
        this.url = url;
        this.objectMapper = new ObjectMapper();
    }

    public GetAudioRecognitionResult recognizeAudio(String chatId, FilePathResponse filePathResponse) {
        GetAudioTokenResponse audioTokenResponse = getAudioToken(chatId, filePathResponse);

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("apikey", apikey));
        params.add(new BasicNameValuePair("action", "get_result"));
        params.add(new BasicNameValuePair("token", audioTokenResponse.getToken()));

        GetAudioRecognitionResult response = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            int n = 0;
            while (++n < 100) {
                Thread.sleep(1);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse= client.execute(httpPost);
                response = objectMapper.readValue(
                        EntityUtils.toString(httpResponse.getEntity()),
                        GetAudioRecognitionResult.class
                );
                if (!response.getResult().equals("wait")) break;
            }
        }catch (IOException | InterruptedException e) {
            // TODO: logging
        }
        return response;
    }

    private GetAudioTokenResponse getAudioToken(String chatId, FilePathResponse filePathResponse) {
        String filename = mediaUtil.convertAudioToWavFormat(chatId, filePathResponse);

        File file = new File(filename);
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("apikey", apikey, ContentType.TEXT_PLAIN);
        builder.addTextBody("action", "identify", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, filename);

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        String value = "";
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpResponse response = client.execute(httpPost);
            value = EntityUtils.toString(response.getEntity());
        }catch (IOException e) {
            //TODO: logging
        }

        GetAudioTokenResponse response = null;
        try {
            response = objectMapper.readValue(value, GetAudioTokenResponse.class);
        }catch (JsonProcessingException e) {
            // TODO: logging
        }

        fileSystemUtil.deleteFile(filename);
        return response;
    }
}
