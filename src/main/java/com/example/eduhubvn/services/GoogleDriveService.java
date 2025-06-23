package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.FileResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_PATH = getServiceAccountKeyPath();

    private static String getServiceAccountKeyPath() {

        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "credentials.json");
        return filePath.toString();
    }

    public FileResponse uploadFileToGoogleDrive( File file ) {
        FileResponse response = new FileResponse();
        try {
            String foderId = "1Xa8TN30tGK1ABozlmX51Gb9y6lW5QZq-";
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(file.getName());
            fileMetadata.setParents(Collections.singletonList(foderId));
            FileContent mediaContent = new FileContent("image/jpeg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id").execute();
            String url = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
            System.out.println("File uploaded successfully: " + url);
            response.setStatus(200);
            response.setMessage("Upload file to Google Drive successfully");
            response.setFileName(file.getName());
            response.setFileUrl(url);
            response.setFileType(file.getName().substring(file.getName().lastIndexOf(".")) + 1);
            response.setSize(file.length());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(500);
            response.setMessage("Upload file to Google Drive failed");
        }
        return response;



    }

    private Drive createDriveService() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential).build();
    }
}
