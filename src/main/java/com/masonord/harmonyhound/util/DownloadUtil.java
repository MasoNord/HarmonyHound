package com.masonord.harmonyhound.util;

import com.google.gson.Gson;
import com.masonord.harmonyhound.exception.ExceedFileSizeLimitException;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@Component
public class DownloadUtil {
    private final double MAX_FILE_SIZE = 10;
    private final String botToken;
    private final LanguageUtil languageUtil;
    private final Gson gson;

    @Autowired
    FileSystemUtil fileSystemUtil;

    public DownloadUtil(@Value("${telegram.bot-token}") String botToken) {
        this.gson = new Gson();
        this.botToken = botToken;
        this.languageUtil = new LanguageUtil();
    }

    public FilePathResponse download(String fieldId, String chatId) throws URISyntaxException, IOException, InterruptedException, ExceedFileSizeLimitException {
        FilePathResponse filePathResponse = getFilePath(fieldId);
        double fileSizeInMB = (double) filePathResponse.getResult().getFile_size() / (1024 * 1024);
        System.out.println(fileSizeInMB);

        if (fileSizeInMB > MAX_FILE_SIZE) {
            throw new ExceedFileSizeLimitException(languageUtil.getProperty("file.too.big"));
        }

        String destinationToDownload = "downloaded-media/chat_" + chatId + "/"
                + filePathResponse.getResult().getFile_path().split("/")[1];

        fileSystemUtil.createNewFolder("downloaded-media/chat_" + chatId);
        fileSystemUtil.createNewFile(destinationToDownload);

        URI url = new URI("https://api.telegram.org/file/bot" + botToken + "/" + filePathResponse.getResult().getFile_path());
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.toURL().openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(destinationToDownload);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        return filePathResponse;
    }

    // TODO: add java doc
    private FilePathResponse getFilePath(String fieldId) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fieldId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), FilePathResponse.class);
    }
}
