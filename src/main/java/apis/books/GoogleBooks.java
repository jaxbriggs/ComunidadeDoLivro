/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apis.books;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author carlos
 */
public class GoogleBooks {
    
    private static final String API_KEY = "AIzaSyAqbArP79_mtNuxpRLHRZUQg666X7GhZp0";
    
    // HTTP GET request
    public static String getLivros(String texto, String param) throws Exception {

            /*
                intitle: Returns results where the text following this keyword is found in the title.
                inauthor: Returns results where the text following this keyword is found in the author.
                inpublisher: Returns results where the text following this keyword is found in the publisher.
                subject: Returns results where the text following this keyword is listed in the category list of the volume.
                isbn: Returns results where the text following this keyword is the ISBN number.
                lccn: Returns results where the text following this keyword is the Library of Congress Control Number.
                oclc: Returns results where the text following this keyword is the Online Computer Library Center number.
            */
            

            String url = "https://www.googleapis.com/books/v1/volumes?q=" + param + ":" + texto.trim()  + "&maxResults=15&orderBy=relevance&projection=full&fields=items(volumeInfo(authors%2Ccategories%2CcontentVersion%2Cdescription%2CimageLinks%2Fthumbnail%2CindustryIdentifiers%2CinfoLink%2Clanguage%2CmainCategory%2CpageCount%2CprintedPageCount%2CpublishedDate%2Cpublisher%2CsamplePageCount%2CseriesInfo%2Csubtitle%2Ctitle))&key=" + API_KEY;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            return  response.toString();

    }
}
