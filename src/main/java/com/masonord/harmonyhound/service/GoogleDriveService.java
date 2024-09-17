package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.response.FilePathResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleDriveService {
    void test(FilePathResponse filePathResponse, String chatIde) throws IOException, GeneralSecurityException;
}
