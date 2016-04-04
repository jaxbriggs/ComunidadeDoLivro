/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import apis.books.GoogleBooks;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
    
    /*
    public static void main(String[] args) throws Exception {

		BooksApiServlet http = new BooksApiServlet();

		System.out.println("Testing 1 - Send Http GET request");
		System.out.println(http.getLivros("harry", "intitle"));

	}
    */
    
      public void init() throws ServletException
    {
        // Do nothing
    }    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String isbn = req.getParameter("isbn");
        
        //Um JSON sera retornado
        resp.setContentType("application/json");
        
        String result = "";
        try {
            //Faz a consulta por isbn
            if(isbn != null){
                result = GoogleBooks.getLivros(isbn, "isbn");
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        PrintWriter out = resp.getWriter();
        
        out.print(result);
        out.flush();
        
    }  
        
     @Override
     public void destroy() {
        // Finalization code...
     }
    
}
