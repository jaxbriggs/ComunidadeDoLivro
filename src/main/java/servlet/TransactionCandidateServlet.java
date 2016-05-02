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
    name = "TransactionCandidateServlet", 
    urlPatterns = {"/eleicao_transacao"}
)
public class TransactionCandidateServlet extends HttpServlet {
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
        
        //Tenta pegar a operacao de eleicao
        String eleicao = request.getParameter("eleicaoJson");
        
        if(eleicao != null){
            Integer transacaoId = null;
            Integer userId = null;
            Integer qtdLivrosCandidatados = null;
            Integer electOpId = null;
            
            JsonObject jobj = new Gson().fromJson(eleicao, JsonObject.class);
            
            userId = jobj.get("userId").getAsInt();
            transacaoId = jobj.get("tId").getAsInt();
            qtdLivrosCandidatados = jobj.get("qtd").getAsInt();
            electOpId = jobj.get("electOp").getAsInt();
            
            TransacaoDAO tDao;
            switch(electOpId){
                case 0: //Candidatar
                    
                    tDao = new TransacaoDAO();
                    try{
                       sucesso = tDao.candidatarTransacaoOp0(userId, qtdLivrosCandidatados, transacaoId) > 0;
                    } catch (SQLException ex) {
                        sucesso = false;
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        sucesso = false;
                        ex.printStackTrace();
                    }
                    
                break;
                    
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
