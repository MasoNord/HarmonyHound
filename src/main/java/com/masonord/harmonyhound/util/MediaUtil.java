package com.masonord.harmonyhound.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * MediaUtil class is made to work with audio files
 * <p>
 * specifically for converting them and calculating their durations
 *
 */


public class MediaUtil {
    public final static Logger LOGGER = LoggerFactory.getLogger(MediaUtil.class);
    private final FileSystemUtil fileSystemUtil;

    public MediaUtil() {
        this.fileSystemUtil = new FileSystemUtil();
    }

    public void convertFileToWav(String inFilename, String outFilename) {
        String cmd = "ffmpeg -i " + inFilename + " " + outFilename;
        try {
            if (fileSystemUtil.fileExists(outFilename)) {
                throw new FileAlreadyExistsException("The output file is already exists");
            }
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }catch (IOException | InterruptedException e) {
            LOGGER.atError().setMessage(e.getMessage()).log();
        }
        LOGGER.atInfo().setMessage("The file has been converted to Wav format successfully").log();
    }

    public double getAudioDuration(String destination) {
        try {
            File file = new File(destination);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();

            LOGGER.atInfo().setMessage("The file duration has been calculated successfully").log();

            return ((frames + 0.0) / format.getFrameRate());
        } catch (UnsupportedAudioFileException | IOException e) {
            LOGGER.atError().setMessage(e.getMessage()).log();
            throw new RuntimeException(e);
        }
    }
}
