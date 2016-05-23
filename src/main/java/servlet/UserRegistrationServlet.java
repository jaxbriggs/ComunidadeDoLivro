/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

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
        
        String userIdStr = request.getParameter("userId");
        String cpfOrCnpjString = request.getParameter("cpfOrCnpjString");
        
        if(userIdStr != null){
            
            response.setContentType("application/json");
            
            boolean sucesso = false;
            Integer userId = Integer.parseInt(userIdStr);
            
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
            
            String alteracaoUsuario =  
            "update comunidade_do_livro.usuario" +
            " set nm_usuario = '"+request.getParameter("name")+"'," +
            " nm_login_usuario = '"+request.getParameter("userLogin")+"'," +
            " nm_email_usuario = '"+request.getParameter("userEmail")+"'," +
            " cd_senha_usuario = '"+senhaStr.toString()+"'," +
            " cd_cpf_usuario = "+(cpfOrCnpjString.equals("cpf") ? "'"+request.getParameter("cpfOrCnpj")+"'" : null)+"," +
            " cd_cnpj_usuario = '"+(cpfOrCnpjString.equals("cnpj") ? "'"+request.getParameter("cpfOrCnpj")+"'" : null)+"'," +
            " cd_telefone_usuario = '"+request.getParameter("tel")+"'," +
            " cd_celular_usuario = '"+request.getParameter("cel")+"'" +
            " where cd_usuario = "+userId+";";
            
            String alteracaoEndereco = 
            "update comunidade_do_livro.endereco" +
            " set cd_codigo_postal_endereco = '"+request.getParameter("cep")+"'," +
            " nm_rua_endereco = '"+request.getParameter("rua")+"'," +
            " cd_numero_endereco = "+request.getParameter("numero")+"," +
            " nm_bairro_endereco = '"+request.getParameter("bairro")+"'," +
            " nm_cidade_endereco = '"+request.getParameter("cidade")+"'," +
            " sg_unidade_federativa_endereco = '"+request.getParameter("uf")+"'" +
            " where cd_endereco = (" +
            " select cd_endereco_usuario" +
            " from comunidade_do_livro.usuario" +
            " where cd_usuario = " + userId +
            " );";
            
            try {
                Connection conn = database.Connection.getConnection();
                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(alteracaoUsuario);
                    stmt.executeUpdate(alteracaoEndereco);
                    sucesso = true;
                }  catch (SQLException ex) {
                    ex.printStackTrace();
                    sucesso = false;
                }  finally {
                    try {
                        stmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        sucesso = false;
                        ex.printStackTrace();
                    }
                }
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();
            
            String r = "{\"success\":"+ String.valueOf(sucesso) +"}";
            
            out.print(r);
            out.flush();
        } else {
            ServletOutputStream out;

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
                request.setAttribute("login",request.getParameter("userLogin"));
                request.setAttribute("password",request.getParameter("signInPassword"));
                RequestDispatcher rd = request.getRequestDispatcher("login");
                rd.forward(request,response);
            } catch (SQLException ex) {
                out = response.getOutputStream();
                out.print("Erro ao realizar cadastro!");
                response.sendRedirect("/index.jsp");
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                out = response.getOutputStream();
                out.print("Erro ao realizar cadastro!");
                response.sendRedirect("/index.jsp");
                ex.printStackTrace();
            }
        }
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
