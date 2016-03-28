/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dao.LivroDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.ObjectJson;
import model.Livro;

/**
 *
 * @author carlos
 */
@WebServlet(
    name = "BookSearchServlet", 
    urlPatterns = {"/busca_livro"}
)
public class BookSearchServlet extends HttpServlet{
     public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doGet(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        
        response.setContentType("application/json");
        
        Livro result = new Livro();
        LivroDAO dao = new LivroDAO();
        try {
            //Faz a consulta por isbn ou titulo
            if(isbn != null){
                result = dao.getLivroByISBN(isbn);
            } else {
                //Implementar
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //Transforma o usuario em um objeto JSON
        String livroJson = ObjectJson.getObjectJson(result);
        
        PrintWriter out = response.getWriter();
        
        //Retorna o JSON
        out.print(livroJson);
        out.flush();
        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
