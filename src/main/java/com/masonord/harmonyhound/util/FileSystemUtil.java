package com.masonord.harmonyhound.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileSystemUtil {

    public void createNewFile(String destination) {
        try {
            File file = new File(destination);
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Unable to create a file at specified path");
            }
        }catch (IOException e) {
            // TODO: logging
        }
    }

    public void createNewFolder(String destination) {
        try {
            File file = new File(destination);
            boolean folderCreated = true;

            if (!file.exists()) {
                folderCreated = file.mkdirs();
            }

            if (!folderCreated) {
                throw new IOException("Unable to create a folder at specified path");
            }
        }catch(IOException e) {
            // TODO: logging
        }
    }

    public void deleteFile(String destination) {
        try {
            File file = new File(destination);
            boolean fileDeleted = file.delete();
            if (!fileDeleted) {
                throw new IOException("Unable to delete a file from at specified path");
            }
        }catch(IOException e) {
            // TODO: logging
        }
    }
}
