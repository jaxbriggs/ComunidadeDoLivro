/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author carlos
 */

@WebServlet(
        name = "UserRegistrationServlet", 
        urlPatterns = {"/cadastrar"}
    )
public class UserRegistrationServlet extends HttpServlet {
    
    
    
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        out.write("Connection succeeded!".getBytes());
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
