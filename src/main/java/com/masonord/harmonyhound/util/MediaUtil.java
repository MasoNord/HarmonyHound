package com.masonord.harmonyhound.util;

import com.masonord.harmonyhound.response.FilePathResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MediaUtil {

    private final String ffmpegPath;
    private final String ffprobePath;

    public MediaUtil(@Value("${ffmpeg.path}") String ffmpegPath,
                     @Value("${ffprobe.path}") String ffprobePath) {
        this.ffmpegPath = ffmpegPath;
        this.ffprobePath = ffprobePath;
    }

    public void convertAudioToWavFormat(String chatId, FilePathResponse filePathResponse) {
        String fileName = filePathResponse.getResult().getFile_path().split("/")[1];
        String inFilename ="downloaded-media/chat_" + chatId + "/" + fileName;
        String outFilename ="downloaded-media/chat_" + chatId + "/" + fileName.split("\\.(?=[^\\.]+$)")[0] + ".wav";
        String cmd = "ffmpeg -i " + inFilename + " -ar 8000 -ac 1 -vn " + outFilename;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            File file = new File(inFilename);
            boolean done = file.delete();
        }catch (IOException | InterruptedException e) {
            // TODO: logging
        }
    }
}
