/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.UserJson;


/**
 *
 * @author carlos
 */

@WebServlet(
        name = "UserLoginServlet", 
        urlPatterns = {"/login"}
    )
public class UserLoginServlet extends HttpServlet {
    
   
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        //Um JSON sera retornado
        response.setContentType("application/json");
        
        //Pega os campos digutados
        String login = request.getParameter("login");
        String senha = request.getParameter("password");
        
        //Faz a consulta do usuario
        UserDAO dao = new UserDAO();
        User user = new User();
        try {
            user = dao.getUserByLoginOrEmail(login, senha);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        
        //Transforma o usuario em um objeto JSON
        String userJson = UserJson.getUserJson(user);
        
        PrintWriter out = response.getWriter();
        
        out.print(userJson);
        out.flush();
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
