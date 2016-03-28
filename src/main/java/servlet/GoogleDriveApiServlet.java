package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import servlet.drive_api.Access;
import servlet.drive_api.DriveOperations;

@WebServlet(
    name = "GoogleDriveApiServlet", 
    urlPatterns = {"/drive"}
)
@MultipartConfig
public class GoogleDriveApiServlet extends HttpServlet{
    
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        response.setContentType("application/json");
        
        Part filePart = request.getPart("capa"); //Pega o arquivo da capa
        String fileName = filePart.getSubmittedFileName();
        InputStream fileContent = filePart.getInputStream();
        
        File f = new File("capas/" + fileName);
        OutputStream outputStream = new FileOutputStream(f);
        IOUtils.copy(fileContent, outputStream);
        outputStream.close();
        fileContent.close();
        
        com.google.api.services.drive.model.File driveFile = null;
        try {
            driveFile = DriveOperations.insertFile(DriveOperations.getService(), fileName, "0B03XPxH14xDTRHZDejJVWlhCR0k", filePart.getContentType(), f, fileName);
        } catch (Access.CodeExchangeException ex) {
            ex.printStackTrace();
        } catch (Access.NoRefreshTokenException ex) {
            ex.printStackTrace();
        }
        
        //Deleta o arquivo do servidor
        try{	
            if(f.delete()){
                System.out.println(f.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }
    	}catch(Exception e){
            e.printStackTrace();
    	}
        
        com.google.api.services.drive.model.File fileInfo = null;
        try{
            fileInfo = DriveOperations.getFile(DriveOperations.getService(), driveFile.getId());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        
        String fileId = driveFile == null ?  "{}" : "{\"link\":\"" +driveFile.getWebContentLink()+"\"}";
        
        PrintWriter out = response.getWriter();
        
        out.print(fileId);
        out.flush();
        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}