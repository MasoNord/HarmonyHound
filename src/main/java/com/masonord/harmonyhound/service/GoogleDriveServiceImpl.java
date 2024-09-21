package com.masonord.harmonyhound.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.masonord.harmonyhound.response.FilePathResponse;
import com.masonord.harmonyhound.util.FileSystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private static final String APPLICATION_NAME = "Web client 1";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "/home/masonord/credentials";
    private static final Set<String> SCOPES = Collections.singleton(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    @Autowired
    private FileSystemUtil fileSystemUtil;

    @Override
    public File uploadFile(FilePathResponse filePathResponse, String chatId) throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        File fileMetadata = new File();
        fileMetadata.setName(filePathResponse.getResult().getFile_path().split("/")[1]);
        java.io.File filePath = new java.io.File("downloaded-media/chat_" + chatId + "/"
                + filePathResponse.getResult().getFile_path().split("/")[1]);
        FileContent mediaContent = new FileContent("audio/ogg", filePath);
        File fileLink = null;
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            Permission permission = new Permission();
            permission.setType("anyone");
            permission.setRole("reader");
            service.permissions().create(file.getId(), permission).execute();
            fileLink = service.files().get(file.getId())
                    .setFields("id, webViewLink, webContentLink")
                    .execute();
        }catch (GoogleJsonResponseException e) {
            System.out.println("Something went wrong" + e.getDetails());
        }

        fileSystemUtil.deleteFile("downloaded-media/chat_" + chatId + "/"
                + filePathResponse.getResult().getFile_path().split("/")[1]);

        return fileLink;
    }

    @Override
    public void deleteFile(String fileId) throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        service.files().delete(fileId).execute();
    }

    private Credential getCredential(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleDriveServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }

}
