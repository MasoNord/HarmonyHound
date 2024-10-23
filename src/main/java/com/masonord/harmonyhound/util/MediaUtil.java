package com.masonord.harmonyhound.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Component
public class MediaUtil {
    private final FileSystemUtil fileSystemUtil;

    @Autowired
    MediaUtil(FileSystemUtil fileSystemUtil) {
        this.fileSystemUtil = fileSystemUtil;
    }

    public void convertFileToWav(String inFilename, String outFilename) {
        String cmd = "ffmpeg -i " + inFilename + " " + outFilename;
        try {
            if (fileSystemUtil.fileExists(outFilename)) {
                throw new FileAlreadyExistsException("the output file is already exists");
            }
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }catch (IOException | InterruptedException e) {
            // TODO: logging
        }
    }

    public double getAudioDuration(String destination) {
        try {
            File file = new File(destination);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return ((frames + 0.0) / format.getFrameRate());
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
