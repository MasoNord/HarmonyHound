package com.masonord.harmonyhound.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masonord.harmonyhound.response.FilePathResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class MediaUtil {
    private final ObjectMapper objectMapper;
    private final String botToken;

    public MediaUtil(@Value("${telegram.bot-token}") String botToken) {
        this.objectMapper = new ObjectMapper();
        this.botToken = botToken;
    }

    public void download(String fieldId) {
        FilePathResponse filePathResponse = getFilePath(fieldId);
        String url = "https://api.telegram.org/file/bot" + botToken + "/" + filePathResponse.getResult().getFile_path();
        String destination = "downloaded-media" + "/" + filePathResponse.getResult().getFile_path().split("/")[1];
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        try {
            File file = new File(destination);
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Unable to create a file at specified path");
            }
        }catch (IOException e) {
            // TODO: logging
        }

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent();
                        FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
                    int read;
                    byte[] buffer = new byte[4096];
                    while ((read = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    *
    *
    *
    * */

    private FilePathResponse getFilePath(String fieldId) {
        String url = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fieldId;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        StringBuilder result = new StringBuilder();
        try {
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()
            ));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }catch(IOException e) {
            // TODO: logging
            System.out.println(e.getMessage());
        }

        FilePathResponse response = null;
        try {
            response =  objectMapper.readValue(result.toString(), FilePathResponse.class);
        }catch(JsonProcessingException e) {
            // TODO: logging
            System.out.println(e.getMessage());
        }

        return response;
    }
}
