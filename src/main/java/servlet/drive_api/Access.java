/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.drive_api;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import com.google.api.services.oauth2.model.Userinfoplus;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author T2S-CARLOS
 */
public class Access {
    // Path to client_secret.json which should contain a JSON document such as:
    //   {
    //     "web": {
    //       "client_id": "[[YOUR_CLIENT_ID]]",
    //       "client_secret": "[[YOUR_CLIENT_SECRET]]",
    //       "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    //       "token_uri": "https://accounts.google.com/o/oauth2/token"
    //     }
    //   }
    
    // Load client secrets.
      private static final String CLIENTSECRET_LOCATION = "client_secret.json";

    //private static final String CLIENTSECRET_LOCATION = "";
    private static final String APPLICATION_NAME = "Comunidade do Livro";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/drive",
            "email",
            "profile");

    private static GoogleAuthorizationCodeFlow flow = null;
    private static final JacksonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Exception thrown when an error occurred while retrieving credentials.
     */
    public static class GetCredentialsException extends Exception {

        protected String authorizationUrl;

        /**
         * Construct a GetCredentialsException.
         *
         * @param authorizationUrl The authorization URL to redirect the user
         * to.
         */
        public GetCredentialsException(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * Set the authorization URL.
         */
        public void setAuthorizationUrl(String authorizationUrl) {
            this.authorizationUrl = authorizationUrl;
        }

        /**
         * @return the authorizationUrl
         */
        public String getAuthorizationUrl() {
            return authorizationUrl;
        }
    }

    /**
     * Exception thrown when a code exchange has failed.
     */
    public static class CodeExchangeException extends GetCredentialsException {

        /**
         * Construct a CodeExchangeException.
         *
         * @param authorizationUrl The authorization URL to redirect the user
         * to.
         */
        public CodeExchangeException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no refresh token has been found.
     */
    public static class NoRefreshTokenException extends GetCredentialsException {

        /**
         * Construct a NoRefreshTokenException.
         *
         * @param authorizationUrl The authorization URL to redirect the user
         * to.
         */
        public NoRefreshTokenException(String authorizationUrl) {
            super(authorizationUrl);
        }
    }

    /**
     * Exception thrown when no user ID could be retrieved.
     */
    private static class NoUserIdException extends Exception {
    }

    /**
     * Retrieved stored credentials for the provided user ID.
     *
     * @param userId User's ID.
     * @return Stored Credential if found, {@code null} otherwise.
     */
    static Credential getStoredCredentials() throws IOException {
        // TODO: Implement this method to work with your database. Instantiate a new
        // Credential instance with stored accessToken and refreshToken.
        //throw new UnsupportedOperationException();

        InputStream in
                = new FileInputStream(CLIENTSECRET_LOCATION);
        GoogleClientSecrets clientSecret
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
        String selectCredencial = "select refresh, access from comunidade_do_livro.credencial;";
        
        Connection conn = null;
        Statement stmt = null;
        
        String access = "";
        String refresh = "";
        
          try {
               conn = database.Connection.getConnection();
               stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(selectCredencial);
                
                while(rs.next()){
                   //Retrieve by column name
                   access  = rs.getString("access");
                   refresh = rs.getString("refresh");
                }
                rs.close();
          } catch (URISyntaxException ex) {
              ex.printStackTrace();
          } catch (SQLException ex) {
              ex.printStackTrace();
          } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }finally{
            //finally block used to close resources
            try{
               if(stmt!=null)
                  conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
               if(conn!=null)
                  conn.close();
            }catch(SQLException se){
               se.printStackTrace();
            }//end finally try
         }//end try
        
        
          
        return new GoogleCredential.Builder()
                .setClientSecrets(clientSecret)
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .build()
                .setRefreshToken(refresh)
                .setAccessToken(access);
    }

    /**
     * Store OAuth 2.0 credentials in the application's database.
     *
     * @param userId User's ID.
     * @param credentials The OAuth 2.0 credentials to store.
     */
    static void storeCredentials(Credential credentials) {
        // TODO: Implement this method to work with your database.
        // Store the credentials.getAccessToken() and credentials.getRefreshToken()
        // string values in your database.
        //throw new UnsupportedOperationException();
            
        String insertCredencial = "insert into comunidade_do_livro.credencial(refresh, access)\n" +
                                  "values('"+ credentials.getRefreshToken() +"', '"+credentials.getAccessToken()+"');";
        
        Connection conn = null;
        Statement stmt = null;
        
          try {
              conn = database.Connection.getConnection();
              stmt = conn.createStatement();
              stmt.executeUpdate(insertCredencial);
          } catch (URISyntaxException ex) {
              ex.printStackTrace();
          } catch (SQLException ex) {
              ex.printStackTrace();
          }finally{
            //finally block used to close resources
            try{
               if(stmt!=null)
                  conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
               if(conn!=null)
                  conn.close();
            }catch(SQLException se){
               se.printStackTrace();
            }//end finally try
         }//end try
    }

    /**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secret.json.
     */
    static GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            InputStream in
                    = new FileInputStream(CLIENTSECRET_LOCATION);
            GoogleClientSecrets clientSecret
                    = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecret, SCOPES)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        }
        return flow;
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param authorizationCode Authorization code to exchange for OAuth 2.0
     * credentials.
     * @return OAuth 2.0 credentials.
     * @throws CodeExchangeException An error occurred.
     */
    static Credential exchangeCode(String authorizationCode)
            throws CodeExchangeException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(authorizationCode)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            return flow.createAndStoreCredential(response, null);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            throw new CodeExchangeException(null);
        }

    }

    /**
     * Send a request to the UserInfo API to retrieve the user's information.
     *
     * @param credentials OAuth 2.0 credentials to authorize the request.
     * @return User's information.
     * @throws NoUserIdException An error occurred.
     */
    static Userinfoplus getUserInfo(Credential credentials)
            throws NoUserIdException {
        Oauth2 userInfoService = new Oauth2.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        Userinfoplus userInfo = null;
        try {
            userInfo = userInfoService.userinfo().get().execute();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
        }
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        } else {
            throw new NoUserIdException();
        }
    }

    /**
     * Retrieve the authorization URL.
     *
     * @param emailAddress User's e-mail address.
     * @param state State for the authorization URL.
     * @return Authorization URL to redirect the user to.
     * @throws IOException Unable to load client_secret.json.
     */
    public static String getAuthorizationUrl(String emailAddress, String state)
            throws IOException {
        GoogleAuthorizationCodeRequestUrl urlBuilder = getFlow()
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .setState(state);
        urlBuilder.set("user_id", emailAddress);
        return urlBuilder.build();
    }

    /**
     * Retrieve credentials using the provided authorization code.
     *
     * This function exchanges the authorization code for an access token and
     * queries the UserInfo API to retrieve the user's e-mail address. If a
     * refresh token has been retrieved along with an access token, it is stored
     * in the application database using the user's e-mail address as key. If no
     * refresh token has been retrieved, the function checks in the application
     * database for one and returns it if found or throws a
     * NoRefreshTokenException with the authorization URL to redirect the user
     * to.
     *
     * @param authorizationCode Authorization code to use to retrieve an access
     * token.
     * @param state State to set to the authorization URL in case of error.
     * @return OAuth 2.0 credentials instance containing an access and refresh
     * token.
     * @throws NoRefreshTokenException No refresh token could be retrieved from
     * the available sources.
     * @throws IOException Unable to load client_secret.json.
     */
    public static Credential getCredentials(String authorizationCode, String state)
            throws CodeExchangeException, NoRefreshTokenException, IOException {
        String emailAddress = "1932thx@gmail.com";
        try {
            Credential credentials = getStoredCredentials();
            /*if (credentials == null || credentials.getRefreshToken() == null) {
                credentials = exchangeCode(authorizationCode);
                
                if (credentials.getRefreshToken() != null) {
                    Userinfoplus userInfo = getUserInfo(credentials);
                    storeCredentials(credentials);
                }
            }
            
        } catch (NoUserIdException e) {
            e.printStackTrace();
      */
            return credentials;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // No refresh token has been retrieved.
        String authorizationUrl = getAuthorizationUrl(emailAddress, state);
        throw new NoRefreshTokenException(authorizationUrl);
    }

}
