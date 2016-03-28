/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.drive_api;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.ParentReference;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.InputStream;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author carlos
 */
public class DriveOperations {
    public static File insertFile(Drive service, String title, String parentId, String mimeType, java.io.File fileFromClient, String filename) {
        // File's metadata.
        File body = new File();
        body.setTitle(title);
        //body.setDescription(description);
        body.setMimeType(mimeType);

        // Set the parent folder.
        if (parentId != null && parentId.length() > 0) {
          body.setParents(
              Arrays.asList(new ParentReference().setId(parentId)));
        }

        // File's content.
        java.io.File fileContent = fileFromClient;
        FileContent mediaContent = new FileContent(mimeType, fileContent);
        try {
          File file = service.files().insert(body, mediaContent).execute();

          // Uncomment the following line to print the File ID.
          // System.out.println("File ID: " + file.getId());

          return file;
        } catch (IOException e) {
          System.out.println("An error occured: " + e);
          return null;
        }
    }
    
    public static File getFile(Drive service, String fileId) {
    try {
      File file = service.files().get(fileId).execute();
      return file;
    } catch (IOException e) {
      System.out.println("An error occured: " + e);
    }
    return null;
  }

    
    public static InputStream downloadFile(Drive service, File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
          try {
            HttpResponse resp =
                service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                    .execute();
            return resp.getContent();
          } catch (IOException e) {
            // An error occurred.
            e.printStackTrace();
            return null;
          }
        } else {
          // The file doesn't have any content stored on Drive.
          return null;
        }
    }
    
    public static Drive getService() throws Access.CodeExchangeException, Access.NoRefreshTokenException, IOException{
        Credential credential = Access.getCredentials(null, null);
        return Authorization.buildService(credential);
    }
}
