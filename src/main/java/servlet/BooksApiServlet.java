/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carlos
 */
@WebServlet(
        name = "BooksApiServlet", 
        urlPatterns = {"/api_livros"}
    )
public class BooksApiServlet extends HttpServlet{
    
    private static final String API_KEY = "AIzaSyAqbArP79_mtNuxpRLHRZUQg666X7GhZp0";
    
    /*
    public static void main(String[] args) throws Exception {

		BooksApiServlet http = new BooksApiServlet();

		System.out.println("Testing 1 - Send Http GET request");
		System.out.println(http.getLivros("9780262140874", "isbn"));

	}*/
    
      public void init() throws ServletException
    {
        // Do nothing
    }    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String isbn = req.getParameter("isbn");
        String titulo = req.getParameter("titulo");
        
        //Um JSON sera retornado
        resp.setContentType("application/json");
        
        String result = "";
        try {
            //Faz a consulta por isbn ou titulo
            if(isbn != null){
                result = getLivros(isbn, "isbn");
            } else {
                result = getLivros(isbn, "intitle");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        PrintWriter out = resp.getWriter();
        
        out.print(result);
        out.flush();
        
    }  
    
    // HTTP GET request
    private String getLivros(String texto, String param) throws Exception {

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
        
     @Override
     public void destroy() {
        // Finalization code...
     }
    
}
