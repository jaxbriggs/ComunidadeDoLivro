/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.TransacaoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.ObjectJson;
import model.Transacao;
/**
 *
 * @author carlos
 */

@WebServlet(
        name = "TransactionsServlet", 
        urlPatterns = {"/transactions"}
    )
public class TransactionsServlet extends HttpServlet {
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doGet(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        //Retorno do servidor
        PrintWriter out = response.getWriter();
        
        //Um JSON sera retornado
        response.setContentType("application/json");
        
        //Variavel que reporta o sucesso
        String result = "{\"resultado\":\"erro-parametros\"}";
        
        
        //Pega o json
        
        //Get by user with index
        String userIdAndIndex = request.getParameter("getAllTransacoesByUserWithIndex");
        
        String transactionId = request.getParameter("transactionId");
        String bookIsbn = request.getParameter("bookIsbn");
        String bookTitle = request.getParameter("bookTitle");
        
        //Faz a consulta conforme os parametros enviados
        
        //Por usuario
        if(userIdAndIndex != null){
            
            //Pega os dados
            Integer userId = null;
            Integer booksLimit = null;
            
            JsonObject jobj = new Gson().fromJson(userIdAndIndex, JsonObject.class);
            
            userId = jobj.get("userId").getAsInt();
            booksLimit = jobj.get("booksLimit").getAsInt();
            
            TransacaoDAO tdao = new TransacaoDAO();
            List<Transacao> transacoes = new ArrayList<Transacao>();
            String qtdLivros = "";
            
            try {
                transacoes = tdao.getLivrosByUsuario(userId, booksLimit);
                if(transacoes != null){
                    qtdLivros = ",{\"qtdLivros\":\"" + tdao.getAmountOfBooksByUser(userId) + "\"}]";
                }
            } catch (SQLException ex) {
                result = "{\"resultado\":\"erro-banco\"}";
                ex.printStackTrace();
                out.print(result);
                out.flush();
                return;
            } catch (URISyntaxException ex) {
                result = "{\"resultado\":\"erro-banco\"}";
                ex.printStackTrace();
                out.print(result);
                out.flush();
                return;
            }
            
            if(transacoes.isEmpty()){
                result = "{\"resultado\":\"nada\"}";
            } else {
                result = ObjectJson.getObjectJson(transacoes).replace("]", qtdLivros);
            }
        }
        
        out.print(result);
        out.flush();
        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
