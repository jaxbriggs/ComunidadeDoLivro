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
import dao.TransacaoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        String retorno = "";
        
        response.setContentType("application/json");
        
        //Tenta pegar a operacao de eleicao
        String eleicao = request.getParameter("eleicaoJson");
        
        //Tenta pegar a operacao de consulta de candidatos por id de transacao
        String getCandidatosByTransacaoId = request.getParameter("getCandidatosByTransacaoId");
        
        //Tenta pegar o id do candidato e da transacao na qual o candidato foi eleito
        String elegerCandidatoByIdAndTransacao = request.getParameter("elegerCandidatoByIdAndTransacao");
        
        //Tenta pegar o id do candidato e o id da transacao da qual deseja-se cancelar sua eleicao previa
        String deselegerCandidatoByIdAndTransacao = request.getParameter("deselegerCandidatoByIdAndTransacao");
        
        //Tenta pegar o id da transacao da qual o usuario esta desistindo
        String desistirOnTransacao = request.getParameter("desistirOnTransacao");
        
        //Tenta pegar o id da transacao da qual o usuario declarou ter recebido o livro
        String confirmarRecebimento = request.getParameter("confirmarRecebimento");
        
        //Tenta pegar o id da transação sendo reaberta
        String reabrirTransacao = request.getParameter("reabrirTransacao");
        
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
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = "{\"success\":"+ String.valueOf(sucesso) +"}";
            out.print(r);
            out.flush();
            
        //Requisicao para consulta de Candidadtos por id da transacao
        } else if(getCandidatosByTransacaoId != null){
            Integer transacaoId = null;
            
            JsonObject jobj = new Gson().fromJson(getCandidatosByTransacaoId, JsonObject.class);
            
            transacaoId = jobj.get("tId").getAsInt();
            
            //Saida
            JsonArray candidatos = new JsonArray();
            
            StringBuilder consulta = new StringBuilder(); 
            consulta.append("select u.nm_usuario,");
            consulta.append(" u.nm_email_usuario,");
            consulta.append(" case");
            consulta.append(" when cd_cnpj_usuario is null then");
            consulta.append(" 'f'");
            consulta.append(" else");
            consulta.append(" 'j'");
            consulta.append(" end,");
            consulta.append(" u.cd_telefone_usuario,");
            consulta.append(" u.cd_celular_usuario,");
            consulta.append(" e.cd_codigo_postal_endereco,");
            consulta.append(" e.nm_rua_endereco,");
            consulta.append(" e.cd_numero_endereco,");
            consulta.append(" e.nm_bairro_endereco,");
            consulta.append(" e.nm_cidade_endereco,");
            consulta.append(" e.sg_unidade_federativa_endereco,");
            consulta.append(" u.cd_usuario,");
            consulta.append(" t.ic_transacao_autorizada_sim_nao,");
            consulta.append(" t.qt_livro_transacao,");
            consulta.append(" t.ic_transacao_finalizada_sim_nao,");
            consulta.append(" t.ic_transacao_ativa_sim_nao");
            consulta.append(" from comunidade_do_livro.transacao t");
            consulta.append(" inner join comunidade_do_livro.usuario u on (t.cd_usuario_cadastrante = u.cd_usuario)");
            consulta.append(" inner join comunidade_do_livro.endereco e on (u.cd_endereco_usuario = e.cd_endereco)");
            consulta.append(" where t.cd_doador_usuario_transacao <> t.cd_usuario_cadastrante and");
            consulta.append(" t.cd_doador_usuario_transacao is not null and");
            consulta.append(" t.cd_donatario_usuario_transacao is not null and");
            consulta.append(" t.cd_doador_usuario_transacao = (");
            consulta.append(" select cd_doador_usuario_transacao");
            consulta.append(" from comunidade_do_livro.transacao");
            consulta.append(" where cd_transacao = ?");
            consulta.append(" ) and");
            consulta.append(" cd_livro_transacao = (");
            consulta.append(" select cd_livro_transacao");
            consulta.append(" from comunidade_do_livro.transacao");
            consulta.append(" where cd_transacao = ?");
            consulta.append(" )");
            consulta.append(" order by t.ic_transacao_autorizada_sim_nao desc;");
            
            try {
                Connection conn = database.Connection.getConnection();
                Integer rs = null;
                PreparedStatement pstmt = null;
                try {
                    pstmt = conn.prepareStatement(consulta.toString());
                    pstmt.setInt(1, transacaoId);
                    pstmt.setInt(2, transacaoId);
                    
                    ResultSet resSet = pstmt.executeQuery();
                    while(resSet.next()){
                        JsonObject candidato = new JsonObject();
                        candidato.addProperty("nm_usuario", resSet.getString(1));
                        candidato.addProperty("nm_email_usuario", resSet.getString(2));
                        candidato.addProperty("cd_cnpj_usuario", resSet.getString(3));
                        candidato.addProperty("cd_telefone_usuario", resSet.getString(4));
                        candidato.addProperty("cd_celular_usuario", resSet.getString(5));
                        candidato.addProperty("cd_codigo_postal_endereco", resSet.getString(6));
                        candidato.addProperty("nm_rua_endereco", resSet.getString(7));
                        candidato.addProperty("cd_numero_endereco", resSet.getInt(8));
                        candidato.addProperty("nm_bairro_endereco", resSet.getString(9));
                        candidato.addProperty("nm_cidade_endereco", resSet.getString(10));
                        candidato.addProperty("sg_unidade_federativa_endereco", resSet.getString(11));
                        candidato.addProperty("cd_usuario", resSet.getInt(12));
                        candidato.addProperty("ic_transacao_autorizada_sim_nao", resSet.getBoolean(13));
                        candidato.addProperty("qt_livro_transacao", resSet.getInt(14));
                        candidato.addProperty("ic_transacao_finalizada_sim_nao", resSet.getBoolean(15));
                        candidato.addProperty("ic_transacao_ativa_sim_nao", resSet.getBoolean(16));
            
                        candidatos.add(candidato);
                    }
                }  catch (SQLException ex) {
                    ex.printStackTrace();
                }  finally {
                    try {
                        pstmt.close();
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(TransactionCandidateServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TransactionCandidateServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();
            
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            
            out.print(gson.toJson(candidatos));
            out.flush();
        
        } else if(elegerCandidatoByIdAndTransacao != null) {
            
            Integer transacaoId = null,
                    candidatoId = null;
            
            JsonObject jobj = new Gson().fromJson(elegerCandidatoByIdAndTransacao, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            candidatoId = jobj.get("candidatoId").getAsInt();
            
            TransacaoDAO tDao = new TransacaoDAO();
            try{
               retorno = tDao.elegerCandidadoInTransacao(transacaoId, candidatoId);
            } catch (SQLException ex) {
                retorno = "{\"success\":\"false\"}";
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                retorno = "{\"success\":\"false\"}";
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = retorno;
            out.print(r);
            out.flush();
            
        } else if(deselegerCandidatoByIdAndTransacao != null) {
            
            Integer transacaoId = null,
                    candidatoId = null;
            
            JsonObject jobj = new Gson().fromJson(deselegerCandidatoByIdAndTransacao, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            candidatoId = jobj.get("candidatoId").getAsInt();
            
            TransacaoDAO tDao = new TransacaoDAO();
            try{
               sucesso = tDao.cancelarEleicaoCandidadoInTransacao(transacaoId, candidatoId);
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = "{\"success\":"+sucesso+"}";
            out.print(r);
            out.flush();
            
        } else if(desistirOnTransacao != null) {
            
            Integer transacaoId = null,
                    candidatoId = null;
            
            JsonObject jobj = new Gson().fromJson(desistirOnTransacao, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            candidatoId = jobj.get("candidatoId").getAsInt();
            
            TransacaoDAO tDao = new TransacaoDAO();
            try{
               sucesso = tDao.desistirDeTransacao(transacaoId, candidatoId);
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = "{\"success\":"+sucesso+"}";
            out.print(r);
            out.flush();
            
        } else if(confirmarRecebimento != null) {
            
            Integer transacaoId = null;
            
            JsonObject jobj = new Gson().fromJson(confirmarRecebimento, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            
            TransacaoDAO tDao = new TransacaoDAO();
            try{
               sucesso = tDao.finalizarTransacao(transacaoId);
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = "{\"success\":"+sucesso+"}";
            out.print(r);
            out.flush();
            
        } else if(reabrirTransacao != null) {
            
            Integer transacaoId = null;
            
            JsonObject jobj = new Gson().fromJson(reabrirTransacao, JsonObject.class);
            
            transacaoId = jobj.get("transacaoId").getAsInt();
            
            TransacaoDAO tDao = new TransacaoDAO();
            try{
               sucesso = tDao.reabrirTransacao(transacaoId);
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            } catch (URISyntaxException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            //Retorna o JSON
            PrintWriter out = response.getWriter();

            String r = "{\"success\":"+sucesso+"}";
            out.print(r);
            out.flush();
            
        }
        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}