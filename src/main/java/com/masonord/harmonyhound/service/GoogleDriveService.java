package com.masonord.harmonyhound.service;

import com.google.api.services.drive.model.File;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleDriveService {
    /**
     * Upload files to Google Drive
     *
     * @param filePath
     * @param chatIde
     * @return a Google Drive share link
     * @throws IOException
     * @throws GeneralSecurityException
     */
    File uploadFile(String filePath, String chatIde) throws IOException, GeneralSecurityException;

    /**
     * Delete file from Google Drive
     *
     * @param fileId
     * @throws GeneralSecurityException
     * @throws IOException
     */
    void deleteFile(String fileId) throws GeneralSecurityException, IOException;
}
