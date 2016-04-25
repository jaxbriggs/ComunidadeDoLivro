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
        
        //===================================================================================
        //                              Get by user with index
        //===================================================================================
        String userIdAndIndex = request.getParameter("getAllTransacoesByUserWithIndex");
        
        //===================================================================================
        //                              Get by transacaoId
        //===================================================================================
        String transacaoIdToAdd = request.getParameter("getTransacaoById");
        
        //===================================================================================
        //                              Get proximos by transacaoId
        //===================================================================================
        String getNextTransacoes = request.getParameter("getNextTransacoes");
        
        //===================================================================================
        //                              Get transacoes by livro
        //===================================================================================
        String getTransacoesByBook = request.getParameter("getTransacoesByBook");
        
        //Faz a consulta conforme os parametros enviados
        
        //Por usuario
        if(userIdAndIndex != null){
            
            //Pega os dados
            Integer userId = null;
            Integer booksLimit = null;
            Integer filtro = null;
            
            JsonObject jobj = new Gson().fromJson(userIdAndIndex, JsonObject.class);
            
            userId = jobj.get("userId").getAsInt();
            booksLimit = jobj.get("booksLimit").getAsInt();
            filtro = jobj.get("filtro").getAsInt();
            
            TransacaoDAO tdao = new TransacaoDAO();
            List<Transacao> transacoes = new ArrayList<Transacao>();
            String qtdLivros = "";
            
            try {
                transacoes = tdao.getLivrosByUsuario(userId, booksLimit, filtro);
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
                result = ObjectJson.getObjectJson(transacoes);
            }
        } 
        
        //Por codigo da transacao
        else if(transacaoIdToAdd != null){
            //Pega os dados
            Integer transacaoId = null;
            
            JsonObject jobj = new Gson().fromJson(transacaoIdToAdd, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            
            TransacaoDAO tdao = new TransacaoDAO();
            List<Transacao> transacao = null;
            
            try {
                transacao = tdao.getTransacaoById(transacaoId);
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
            
            if(transacao == null){
                result = "{\"resultado\":\"nada\"}";
            } else {
                result = ObjectJson.getObjectJson(transacao);
            }
        }
        
        //Pega os proximos a partir do id fornecido
        else if(getNextTransacoes != null){
            //Pega os dados
            Integer rowNum = null;
            Integer booksLimit = null, userId = null, filtroId = null;
           
            JsonObject jobj = new Gson().fromJson(getNextTransacoes, JsonObject.class);
            
            rowNum = jobj.get("rowNum").getAsInt();
            booksLimit = jobj.get("booksLimit").getAsInt();
            userId = jobj.get("userId").getAsInt();
            filtroId = jobj.get("filtroId").getAsInt();
            
            TransacaoDAO tdao = new TransacaoDAO();
            List<Transacao> transacoes = null;
            
            try {
                transacoes = tdao.getNextLivrosByLastTransacao(rowNum, booksLimit, userId, filtroId);
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
            
            if(transacoes == null){
                result = "{\"resultado\":\"nada\"}";
            } else {
                result = ObjectJson.getObjectJson(transacoes);
            }
        }
        
        //Pega os proximos a partir do id fornecido
        else if(getTransacoesByBook != null){
            //Pega os dados
            String livroTexto = null;
            Integer booksLimit = null, userId = null;
           
            JsonObject jobj = new Gson().fromJson(getTransacoesByBook, JsonObject.class);
            
            livroTexto = jobj.get("bookText").getAsString();
            booksLimit = jobj.get("booksLimit").getAsInt();
            userId = jobj.get("userId").getAsInt();
            
            TransacaoDAO tdao = new TransacaoDAO();
            List<Transacao> transacoes = null;
            
            try {
                transacoes = tdao.getTransacoesByLivro(livroTexto, userId, booksLimit);
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
            
            if(transacoes == null){
                result = "{\"resultado\":\"nada\"}";
            } else {
                result = ObjectJson.getObjectJson(transacoes);
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
