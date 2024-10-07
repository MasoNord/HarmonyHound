package com.masonord.harmonyhound.service;

import com.google.api.services.drive.model.File;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleDriveService {
    File uploadFile(FilePathResponse filePathResponse, String chatIde) throws IOException, GeneralSecurityException;
    void deleteFile(String fileId) throws GeneralSecurityException, IOException;
}
