package com.masonord.harmonyhound.util;

import com.google.gson.Gson;
import com.masonord.harmonyhound.exception.ExceedFileSizeLimitException;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DownloadUtil.class);
    private final double MAX_FILE_SIZE = 10;
    private final String botToken;
    private final Gson gson;
    private final FileSystemUtil fileSystemUtil;
    private final LanguageUtil languageUtil;

    public DownloadUtil(String botToken, LanguageUtil languageUtil) {
        this.languageUtil = languageUtil;
        this.botToken = botToken;
        this.gson = new Gson();
        this.fileSystemUtil = new FileSystemUtil();
    }

    public FilePathResponse download(String fieldId, String chatId) throws URISyntaxException, IOException, InterruptedException, ExceedFileSizeLimitException {
        FilePathResponse filePathResponse = getFilePath(fieldId);
        double fileSizeInMB = (double) filePathResponse.getResult().getFile_size() / (1024 * 1024);

        if (fileSizeInMB > MAX_FILE_SIZE) {
            LOGGER
                .atError()
                .setMessage("Exceeded file size limit: {} > {}")
                .addArgument(fileSizeInMB)
                .addArgument(MAX_FILE_SIZE)
                .log();
            throw new ExceedFileSizeLimitException(languageUtil.getProperty("file.too.big"));
        }

        String destinationToDownload = "downloaded-media/chat_" + chatId + "/"
                + filePathResponse.getResult().getFile_path().split("/")[1];

        LOGGER.atDebug()
            .setMessage("A destination to download a file: {}")
            .addArgument(destinationToDownload)
            .log();

        fileSystemUtil.createNewFolder("downloaded-media/chat_" + chatId);
        fileSystemUtil.createNewFile(destinationToDownload);

        URI url = new URI("https://api.telegram.org/file/bot" + botToken + "/" + filePathResponse.getResult().getFile_path());
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.toURL().openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(destinationToDownload);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        LOGGER.atInfo().setMessage("The file has been successfully downloaded").log();
        return filePathResponse;
    }

    // TODO: add java doc
    private FilePathResponse getFilePath(String fieldId) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fieldId);

        LOGGER.atDebug().setMessage("File path uri: {}").addArgument(uri.getPath()).log();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), FilePathResponse.class);
    }
}
