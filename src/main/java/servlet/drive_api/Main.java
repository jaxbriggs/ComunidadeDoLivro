/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.drive_api;

import com.google.api.client.auth.oauth2.Credential;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.api.services.drive.Drive;

/**
 *
 * @author T2S-CARLOS
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //String teste = Access.getAuthorizationUrl("1932thx@gmail.com", null);
            //System.out.println(teste);

            Credential credential = Access.getCredentials(null, null);
            Drive drive = Authorization.buildService(credential);
            //Request.printFile(drive, "0B8nUo2ghuUyGSlRLX3BIcjBmQVE");
            //Request.insertFile(drive, "Ronaldo", "This is a test file", null, "text/plain", "FIRST INSERT");
            Request.insertFile(drive, "Ronaldo", "testing image insertion", null, "image/png", "matrix-wallpaper.jpg");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
