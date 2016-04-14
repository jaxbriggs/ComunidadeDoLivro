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
    name = "BookUpdateServlet", 
    urlPatterns = {"/alterar_livro"}
)
public class BookUpdateServlet extends HttpServlet {
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doGet(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        //Variavel que aponta o resultado das transacoes com o banco
        boolean sucesso = false;
        
        response.setContentType("application/json");
        
        //Tenta pegar os parametros possiveis
        String transacao = request.getParameter("doacaoJson");
        String remocao = request.getParameter("remocaoJson");
        
        //====================================================================================
        //                                      DOACOES                                    
        //====================================================================================
        
        if(transacao != null){
            Integer transacaoId = null;
            Boolean doar = null;
            Integer qtdLivrosSendoDoados = null;
            
            JsonObject jobj = new Gson().fromJson(transacao, JsonObject.class);
            
            doar = jobj.get("doar").getAsBoolean();
            transacaoId = jobj.get("tId").getAsInt();
            qtdLivrosSendoDoados = jobj.get("qtd").getAsInt();
            
            TransacaoDAO dao = new TransacaoDAO();
            
            //Altera a transacao no banco
            sucesso = false;
            try {
                int rs = dao.alterarDoacaoLivroTransacao(transacaoId, doar, qtdLivrosSendoDoados);
                sucesso = rs > 0;
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
        }
        
        
        //====================================================================================
        //                                      REMOVER                                     
        //====================================================================================
        
        if(remocao != null){
            Integer transacaoId = null;
            Boolean remover = null;
            
            JsonObject jobj = new Gson().fromJson(remocao, JsonObject.class);
            
            remover = jobj.get("remover").getAsBoolean();
            transacaoId = jobj.get("tId").getAsInt();
            
            TransacaoDAO dao = new TransacaoDAO();
            
            //Remove a transacao no banco
            sucesso = false;
            try {
                int rs = dao.removerDoacaoLivroTransacao(transacaoId);
                sucesso = rs > 0;
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
        }
        
        //Retorna o JSON
        PrintWriter out = response.getWriter();
        
        String r = "{\"success\":"+ String.valueOf(sucesso) +"}";
        out.print(r);
        out.flush();        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
