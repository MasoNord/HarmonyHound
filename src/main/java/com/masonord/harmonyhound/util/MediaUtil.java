package com.masonord.harmonyhound.util;

import com.masonord.harmonyhound.response.FilePathResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class MediaUtil {

    @Autowired
    private FileSystemUtil fileSystemUtil;

    public String convertAudioToWavFormat(String chatId, FilePathResponse filePathResponse) {
        String fileName = filePathResponse.getResult().getFile_path().split("/")[1];
        String inFilename = "downloaded-media/chat_" + chatId + "/" + fileName;
        String outFilename = "downloaded-media/chat_" + chatId + "/" + fileName.split("\\.(?=[^\\.]+$)")[0] + ".wav";
        String cmd = "ffmpeg -i " + inFilename + " -ar 8000 -ac 1 -vn " + outFilename;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            fileSystemUtil.deleteFile(inFilename);
        }catch (IOException | InterruptedException e) {
            // TODO: logging
        }

        return outFilename;
    }
}
