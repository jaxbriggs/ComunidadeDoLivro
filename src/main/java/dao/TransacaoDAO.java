/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Transacao;

/**
 *
 * @author carlos
 */
public class TransacaoDAO {
    
    public List<Transacao> getLivrosByUsuario(Integer cdUsuario) throws URISyntaxException, SQLException {
        
        List<Transacao> transacoes = new ArrayList<Transacao>();
        
        String query = "select t1.cd_transacao," +
                        "t1.cd_livro_transacao," +
                        "t1.ic_transacao_finalizada_sim_nao," +
                        "t1.ic_transacao_autorizada_sim_nao," +
                        "t1.ic_transacao_ativa_sim_nao," +
                        "t1.qt_livro_transacao," +
                        "t1.ds_observacao_livro_transacao," +
                        "t1.dt_cadastro_transacao," +
                        "t1.dt_transacao_finalizada" +
                        " from comunidade_do_livro.transacao t1" +
                        " where t1.cd_doador_usuario_transacao = " + cdUsuario + " and" +
                        " t1.cd_usuario_cadastrante = " + cdUsuario +  
                        " order by t1.dt_cadastro_transacao desc;";
        
        Connection conn = database.Connection.getConnection();
        ResultSet rs;
        Statement stmt = null;
        Transacao t = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()){
                t = new Transacao();
                
                t.setCdTransacao(rs.getInt("cd_transacao"));
                
                //Pega o livro da transacao
                LivroDAO livroDao = new LivroDAO();
                t.setLivro(livroDao.getLivroByCodigo(rs.getInt("cd_livro_transacao")));
                
                t.setIsFinalizada(rs.getBoolean("ic_transacao_finalizada_sim_nao"));
                t.setIsAutorizada(rs.getBoolean("ic_transacao_autorizada_sim_nao"));
                t.setIsAtivada(rs.getBoolean("ic_transacao_ativa_sim_nao"));
                t.setQtLivroTransacao(rs.getInt("qt_livro_transacao"));
                t.setDescricao(rs.getString("ds_observacao_livro_transacao"));
                t.setDataCadastro(rs.getDate("dt_cadastro_transacao"));
                t.setDataFinalizacao(rs.getDate("dt_transacao_finalizada"));
                
                transacoes.add(t);
            }
            
            return transacoes;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    public Integer registerTransacao(Transacao t) throws SQLException, URISyntaxException{
        
        StringBuilder insertTransacao = new StringBuilder(); 
        insertTransacao.append("INSERT INTO comunidade_do_livro.transacao("); 
        insertTransacao.append("cd_doador_usuario_transacao,");
        insertTransacao.append("cd_donatario_usuario_transacao,");
        insertTransacao.append("cd_usuario_cadastrante,");
        insertTransacao.append("cd_livro_transacao,");
        insertTransacao.append("ic_transacao_finalizada_sim_nao,");
        insertTransacao.append("ic_transacao_autorizada_sim_nao,");
        insertTransacao.append("ic_transacao_ativa_sim_nao,");
        insertTransacao.append("qt_livro_transacao,");
        insertTransacao.append("ds_observacao_livro_transacao,");
        insertTransacao.append("dt_cadastro_transacao,");
        insertTransacao.append("dt_transacao_finalizada");
        insertTransacao.append(") VALUES (");
        insertTransacao.append(t.getDoador() != null ? t.getDoador().getId() : null);
        insertTransacao.append(",");
        insertTransacao.append(t.getDonatario() != null ? t.getDonatario().getId() : null);
        insertTransacao.append(",");
        insertTransacao.append(t.getCadastrante() != null ? t.getCadastrante().getId() : null);
        insertTransacao.append(",");
        insertTransacao.append(t.getLivro().getCodigo());
        insertTransacao.append(",");
        insertTransacao.append(t.getIsFinalizada());
        insertTransacao.append(",");
        insertTransacao.append(t.getIsAutorizada());
        insertTransacao.append(",");
        insertTransacao.append(t.getIsAtivada());
        insertTransacao.append(",");
        insertTransacao.append(t.getQtLivroTransacao());
        insertTransacao.append(",");
        insertTransacao.append(t.getDescricao());
        insertTransacao.append(",");
        insertTransacao.append(t.getDataCadastro());
        insertTransacao.append(",");
        insertTransacao.append(t.getDataFinalizacao());
        insertTransacao.append(");");
        
        Connection conn = database.Connection.getConnection();
        int rs;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeUpdate(insertTransacao.toString());
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    public int alterarDoacaoLivroTransacao(int transacaoId, boolean doarSimNao, int qtdDoada) throws SQLException, URISyntaxException{
        StringBuilder updateTransacao = new StringBuilder(); 
        updateTransacao.append("UPDATE comunidade_do_livro.transacao "); 
        updateTransacao.append("SET ic_transacao_ativa_sim_nao = ");
        updateTransacao.append(doarSimNao);
        updateTransacao.append(", qt_livro_transacao = ");
        updateTransacao.append(qtdDoada);
        updateTransacao.append(" WHERE cd_transacao = ");
        updateTransacao.append(transacaoId);
        updateTransacao.append(";");
        
        Connection conn = database.Connection.getConnection();
        int rs;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeUpdate(updateTransacao.toString());
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    public int removerDoacaoLivroTransacao(int transacaoId) throws URISyntaxException, SQLException {
        StringBuilder deleteTransacao = new StringBuilder(); 
        deleteTransacao.append("DELETE FROM comunidade_do_livro.transacao ");
        deleteTransacao.append("where cd_transacao = ");
        deleteTransacao.append(transacaoId);
        deleteTransacao.append(";");
        
        Connection conn = database.Connection.getConnection();
        int rs;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeUpdate(deleteTransacao.toString());
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
} 
