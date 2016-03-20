/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.Endereco;
import dao.UserDAO;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */

@WebServlet(
        name = "UserRegistrationServlet", 
        urlPatterns = {"/cadastrarUsuario"}
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
        
        //Tira o hash da senha
        byte[] senha = null;
        try {
            MessageDigest d = MessageDigest.getInstance("MD5");
            senha = d.digest(request.getParameter("signInPassword").getBytes(StandardCharsets.ISO_8859_1));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        StringBuilder senhaStr = new StringBuilder();
            for (int i = 0; i < senha.length; i++)
                senhaStr.append(Integer.toString((senha[i] & 0xff) + 0x100, 16).substring(1));
        
        //Saber se usuario e uma pessoa ou instituicao
        boolean isUserPerson = request.getParameter("pessoa-inst").equals("pessoa");
        
        Endereco userAddress = 
                new Endereco (  request.getParameter("cep"), 
                                request.getParameter("rua"), 
                                Integer.parseInt(request.getParameter("numero")), 
                                request.getParameter("bairro"), 
                                request.getParameter("cidade"), 
                                request.getParameter("uf")
                );
        
        //TODO: Implementar condicionamento de ser admin ou nao
        
        User newUser = 
                new User(
                        request.getParameter("name"), 
                        userAddress, 
                        request.getParameter("userEmail"), 
                        senhaStr.toString(), 
                        false, 
                        true, 
                        isUserPerson ? request.getParameter("cpfOrCnpj") : null, 
                        !isUserPerson ? request.getParameter("cpfOrCnpj") : null, 
                        request.getParameter("tel"), 
                        request.getParameter("cel"), 
                        request.getParameter("userLogin")
                );
        
        //Implementar retorno ao usuario em caso de erro
        UserDAO userDao = new UserDAO();
        try {
            userDao.registerUser(newUser);
            out.print("Usuario cadastrado!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
