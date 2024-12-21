package com.masonord.harmonyhound.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

// TODO: change usage from classic IO to NIO to boost performance

/**
 * FileSystemUtil is responsible for creating or deleting files or folders
 *
 */
public class FileSystemUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileSystemUtil.class);

    public void createNewFile(String destination) {
        try {
            File file = new File(destination);
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Unable to create a file at specified path");
            }
        }catch (IOException e) {
            LOGGER.atError().setMessage(e.getMessage()).log();
        }

        LOGGER.atInfo().setMessage("A new file at {} has been created successfully").addArgument(destination).log();
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
            LOGGER.atError().setMessage(e.getMessage()).log();
        }

        LOGGER.atInfo().setMessage("A new folder at {} has been created successfully").addArgument(destination).log();
    }

    public void deleteFile(String destination) {
        try {
            File file = new File(destination);
            boolean fileDeleted = file.delete();
            if (!fileDeleted) {
                throw new IOException("Unable to delete a file from at specified path");
            }
        }catch(IOException e) {
            LOGGER.atError().setMessage(e.getMessage()).log();
        }

        LOGGER.atInfo().setMessage("The file at {} has been deleted successfully").addArgument(destination).log();
    }

    public boolean fileExists(String destination) {
        File file = new File(destination);
        return file.exists();
    }
}
