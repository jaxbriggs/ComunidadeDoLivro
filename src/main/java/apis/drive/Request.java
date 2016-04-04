/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apis.drive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 *
 * @author T2S-CARLOS
 */
public class Request {
    static void printFile(Drive service, String fileId){
        try{
            //FileList files = service.files().list().execute();
            
            //System.out.println(files);
            
            File f = service.files().get(fileId).execute();
            
            System.out.println(f);
            
            //System.out.println("Title: " + file.getTitle());
//            System.out.println("Description: " + file.getDescription());
//            System.out.println("MIME Type: " + file.getMimeType());
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e);
        }
    }
    
    /**
   * Insert new file.
   *
   * @param service Drive API service instance.
   * @param title Title of the file to insert, including the extension.
   * @param description Description of the file to insert.
   * @param parentId Optional parent folder's ID.
   * @param mimeType MIME type of the file to insert.
   * @param filename Filename of the file to insert.
   * @return Inserted file metadata if successful, {@code null} otherwise.
   */
  public static File insertFile(Drive service, String title, String description,
    String parentId, String mimeType, String filename) {
    // File's metadata.
    File body = new File();
    body.setTitle("nemesis");
    body.setDescription(description);
    body.setMimeType(mimeType);

    // Set the parent folder.
//    if (parentId != null && parentId.length() > 0) {
//      body.setParents(
//          Arrays.asList(new ParentReference().setId(parentId)));
//    }

    // File's content.
    java.io.File fileContent = new java.io.File(filename);
    FileContent mediaContent = new FileContent(mimeType, fileContent);
    try {
        File file = service.files().insert(body, mediaContent).execute();
        Drive.Files.Insert create = service.files().insert(file);
      // Uncomment the following line to print the File ID.
      System.out.println("File ID: " + file.getId());

      return file;
    } catch (Exception e) {
      System.out.println("An error occured: " + e);
      return null;
    }
  }
}
