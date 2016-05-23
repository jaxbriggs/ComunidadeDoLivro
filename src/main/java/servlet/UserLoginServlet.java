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
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.ObjectJson;


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
        String login = request.getParameter("login") == null ? (String)request.getAttribute("login") : request.getParameter("login");
        String senha = request.getParameter("password") == null ? (String)request.getAttribute("password") : request.getParameter("password");
        
        //Faz a consulta do usuario
        UserDAO dao = new UserDAO();
        User user = new User();
        try {
            user = dao.getUserByLoginOrEmail(login, senha);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        
        if(request.getAttribute("login") == null && request.getAttribute("password") == null){
            //Transforma o usuario em um objeto JSON
            String userJson = ObjectJson.getObjectJson(user);

            PrintWriter out = response.getWriter();

            out.print(userJson);
            out.flush();
        } else {
            String userJson = ObjectJson.getObjectJson(user);
            request.setAttribute("userJson",userJson);
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request,response);
        }
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
