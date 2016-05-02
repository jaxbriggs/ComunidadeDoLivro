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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.Transacao;

/**
 *
 * @author carlos
 */
public class TransacaoDAO {
    
    //Consultas
    public List<Transacao> getLivrosByUsuario(Integer cdUsuario, Integer limit, Integer filtro) throws URISyntaxException, SQLException {
        
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
                        " inner join comunidade_do_livro.livro l1 on ( l1.cd_livro = t1.cd_livro_transacao )" +
                        " where t1.cd_doador_usuario_transacao = " + cdUsuario + " and" +
                        " t1.cd_usuario_cadastrante = " + cdUsuario +                      
                        " order by";
                        
                        switch(filtro){
                            case 1:
                                query +=
                                " l1.nm_titulo_livro asc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                            case 2:
                                query +=
                                " l1.nm_titulo_livro desc "+ (limit != null ? ("limit " + limit + ";") : ";");    
                                break;
                            case 3:
                                query +=
                                " t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                            case 4:
                                query +=
                                " t1.dt_cadastro_transacao asc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                            case 5:
                                query +=
                                " t1.ic_transacao_ativa_sim_nao desc, t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                            case 6:
                                query +=
                                " t1.ic_transacao_autorizada_sim_nao desc, t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                            default:
                                query +=
                                " t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit " + limit + ";") : ";");
                                break;
                        }
        
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
                
                //Pega a lista de usuarios que se cadidataram a este livro
                StringBuilder candidatosIdsQuery = new StringBuilder();
                candidatosIdsQuery.append("select t.cd_usuario_cadastrante");
                candidatosIdsQuery.append(" from comunidade_do_livro.transacao t");
                candidatosIdsQuery.append(" where t.cd_livro_transacao = ");
                candidatosIdsQuery.append(rs.getInt("cd_livro_transacao"));
                candidatosIdsQuery.append(" and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante <> t.cd_doador_usuario_transacao and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante = t.cd_donatario_usuario_transacao;");
                
                ResultSet candidatosRs = null;
                Statement candidatosStmt = null;
                candidatosStmt = conn.createStatement();
                candidatosRs = candidatosStmt.executeQuery(candidatosIdsQuery.toString());
                List<Integer> candidatos = new ArrayList<Integer>();
                while(candidatosRs.next()){
                    candidatos.add(candidatosRs.getInt(1));
                }
                
                //Atribui os candidatos
                t.setCandidatosIds(candidatos);
                
                
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
    
    public List<Transacao> getNextLivrosByLastTransacao(Integer threshold, Integer limit, Integer cdUsuario, Integer filtro) throws URISyntaxException, SQLException {
        List<Transacao> transacoes = new ArrayList<Transacao>();
        
        String query = "select t1.cd_transacao," +
                        " t1.cd_livro_transacao," +
                        " t1.ic_transacao_finalizada_sim_nao," +
                        " t1.ic_transacao_autorizada_sim_nao," +
                        " t1.ic_transacao_ativa_sim_nao," +
                        " t1.qt_livro_transacao," +
                        " t1.ds_observacao_livro_transacao," +
                        " t1.dt_cadastro_transacao," +
                        " t1.dt_transacao_finalizada" +
                        " from comunidade_do_livro.transacao t1" +
                        " inner join comunidade_do_livro.livro l1 on ( l1.cd_livro = t1.cd_livro_transacao )" +
                        " where t1.cd_doador_usuario_transacao = " + cdUsuario + " and" +
                        " t1.cd_usuario_cadastrante = " + cdUsuario +
                        " order by";
                        
                        switch(filtro){
                            case 1:
                                query +=
                                " l1.nm_titulo_livro asc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                            case 2:
                                query +=
                                " l1.nm_titulo_livro desc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");    
                                break;
                            case 3:
                                query +=
                                " t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                            case 4:
                                query +=
                                " t1.dt_cadastro_transacao asc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                            case 5:
                                query +=
                                " t1.ic_transacao_ativa_sim_nao desc, t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                            case 6:
                                query +=
                                " t1.ic_transacao_autorizada_sim_nao desc, t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                            default:
                                query +=
                                " t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit "+limit+" offset "+threshold+";") : ";");
                                break;
                        }
        
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
                
                //Pega a lista de usuarios que se cadidataram a este livro
                StringBuilder candidatosIdsQuery = new StringBuilder();
                candidatosIdsQuery.append("select t.cd_usuario_cadastrante");
                candidatosIdsQuery.append(" from comunidade_do_livro.transacao t");
                candidatosIdsQuery.append(" where t.cd_livro_transacao = ");
                candidatosIdsQuery.append(rs.getInt("cd_livro_transacao"));
                candidatosIdsQuery.append(" and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante <> t.cd_doador_usuario_transacao and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante = t.cd_donatario_usuario_transacao;");
                
                ResultSet candidatosRs = null;
                Statement candidatosStmt = null;
                candidatosStmt = conn.createStatement();
                candidatosRs = candidatosStmt.executeQuery(candidatosIdsQuery.toString());
                List<Integer> candidatos = new ArrayList<Integer>();
                while(candidatosRs.next()){
                    candidatos.add(candidatosRs.getInt(1));
                }
                
                //Atribui os candidatos
                t.setCandidatosIds(candidatos);
                
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
    
    public List<Transacao> getTransacoesByLivro(String textoLivro, Integer userId, Integer limit) throws URISyntaxException, SQLException {
        List<Transacao> transacoes = new ArrayList<Transacao>();
        
        String query = "select t1.cd_transacao," +
                        " t1.cd_livro_transacao," +
                        " t1.ic_transacao_finalizada_sim_nao," +
                        " t1.ic_transacao_autorizada_sim_nao," +
                        " t1.ic_transacao_ativa_sim_nao," +
                        " t1.qt_livro_transacao," +
                        " t1.ds_observacao_livro_transacao," +
                        " t1.dt_cadastro_transacao," +
                        " t1.dt_transacao_finalizada" +
                        " from comunidade_do_livro.transacao t1" +
                        " inner join comunidade_do_livro.livro l1 on ( t1.cd_livro_transacao = l1.cd_livro )" +
                        " where t1.cd_doador_usuario_transacao = "+userId+" and" +
                        " t1.cd_usuario_cadastrante = "+userId+" and" +
                        " (" +
                            " lower(l1.nm_titulo_livro) like '%"+textoLivro+"%' or" +
                            " lower(l1.nm_autor_livro) like '%"+textoLivro+"%' or" +
                            " l1.cd_isbn_livro::varchar like '%"+textoLivro+"%'" +
                        " )" +
                        " order by t1.dt_cadastro_transacao desc "+ (limit != null ? ("limit " + limit + ";") : ";");
        
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
                
                //Pega a lista de usuarios que se cadidataram a este livro
                StringBuilder candidatosIdsQuery = new StringBuilder();
                candidatosIdsQuery.append("select t.cd_usuario_cadastrante");
                candidatosIdsQuery.append(" from comunidade_do_livro.transacao t");
                candidatosIdsQuery.append(" where t.cd_livro_transacao = ");
                candidatosIdsQuery.append(rs.getInt("cd_livro_transacao"));
                candidatosIdsQuery.append(" and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante <> t.cd_doador_usuario_transacao and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante = t.cd_donatario_usuario_transacao;");
                
                ResultSet candidatosRs = null;
                Statement candidatosStmt = null;
                candidatosStmt = conn.createStatement();
                candidatosRs = candidatosStmt.executeQuery(candidatosIdsQuery.toString());
                List<Integer> candidatos = new ArrayList<Integer>();
                while(candidatosRs.next()){
                    candidatos.add(candidatosRs.getInt(1));
                }
                
                //Atribui os candidatos
                t.setCandidatosIds(candidatos);
                
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
    
    public List<Transacao> getTransacaoById(Integer cdTransacao) throws URISyntaxException, SQLException {
        List<Transacao> transacoes = new ArrayList<Transacao>();
        Transacao transacao = null;
        
        String query = "select t1.cd_transacao," +
                        "t1.cd_livro_transacao," +
                        "t1.ic_transacao_finalizada_sim_nao," +
                        "t1.ic_transacao_autorizada_sim_nao," +
                        "t1.ic_transacao_ativa_sim_nao," +
                        "t1.qt_livro_transacao," +
                        "t1.ds_observacao_livro_transacao," +
                        "t1.dt_cadastro_transacao," +
                        "t1.dt_transacao_finalizada," +
                        "t1.cd_doador_usuario_transacao," +
                        "t1.cd_usuario_cadastrante" +
                        " from comunidade_do_livro.transacao t1" +
                        " where t1.cd_transacao = " + cdTransacao + ";";
        
        Connection conn = database.Connection.getConnection();
        ResultSet rs;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()){
                transacao = new Transacao();
                transacao.setCdTransacao(rs.getInt("cd_transacao"));
                
                //Pega o livro da transacao
                LivroDAO livroDao = new LivroDAO();
                transacao.setLivro(livroDao.getLivroByCodigo(rs.getInt("cd_livro_transacao")));
                
                //Pega a lista de usuarios que se cadidataram a este livro
                StringBuilder candidatosIdsQuery = new StringBuilder();
                candidatosIdsQuery.append("select t.cd_usuario_cadastrante");
                candidatosIdsQuery.append(" from comunidade_do_livro.transacao t");
                candidatosIdsQuery.append(" where t.cd_livro_transacao = ");
                candidatosIdsQuery.append(rs.getInt("cd_livro_transacao"));
                candidatosIdsQuery.append(" and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante <> t.cd_doador_usuario_transacao and");
                candidatosIdsQuery.append(" t.cd_usuario_cadastrante = t.cd_donatario_usuario_transacao;");
                
                ResultSet candidatosRs = null;
                Statement candidatosStmt = null;
                candidatosStmt = conn.createStatement();
                candidatosRs = candidatosStmt.executeQuery(candidatosIdsQuery.toString());
                List<Integer> candidatos = new ArrayList<Integer>();
                while(candidatosRs.next()){
                    candidatos.add(candidatosRs.getInt(1));
                }
                
                //Atribui os candidatos
                transacao.setCandidatosIds(candidatos);
                
                transacao.setIsFinalizada(rs.getBoolean("ic_transacao_finalizada_sim_nao"));
                transacao.setIsAutorizada(rs.getBoolean("ic_transacao_autorizada_sim_nao"));
                transacao.setIsAtivada(rs.getBoolean("ic_transacao_ativa_sim_nao"));
                transacao.setQtLivroTransacao(rs.getInt("qt_livro_transacao"));
                transacao.setDescricao(rs.getString("ds_observacao_livro_transacao"));
                transacao.setDataCadastro(rs.getDate("dt_cadastro_transacao"));
                transacao.setDataFinalizacao(rs.getDate("dt_transacao_finalizada"));
                
                //Pega o doador
                UserDAO userDao = new UserDAO();
                transacao.setDoador(userDao.getUserById(rs.getInt("cd_doador_usuario_transacao")));
                
                //Pega o cadastrante
                transacao.setCadastrante(userDao.getUserById(rs.getInt("cd_usuario_cadastrante")));
            }
            
            transacoes.add(transacao);
            return transacoes;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    //Retorna todos os livros em doacao sem discrimar
    public ArrayList<ArrayList<GenericTransacao>> getAllTransacoesAtivasIDsPicsGenre(Integer userId) throws URISyntaxException, SQLException {
        
        String query = "select t.cd_transacao," +
                        " l.im_capa_livro," +
                        " l.nm_genero_livro," +
                        " l.nm_titulo_livro" +
                        " from comunidade_do_livro.transacao t" +
                        " inner join comunidade_do_livro.livro l on (t.cd_livro_transacao = l.cd_livro)" +
                        " where t.ic_transacao_ativa_sim_nao = true and" +
                        " t.cd_donatario_usuario_transacao is null";
        
                        if(userId != null){
                            query +=
                            " and t.cd_doador_usuario_transacao != " + userId;
                        }
                        
                        query +=
                        " order by 3;";
        
        Connection conn = database.Connection.getConnection();
        ResultSet rs;
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            ArrayList<GenericTransacao> books = new ArrayList<GenericTransacao>();
            GenericTransacao book;
            
            while(rs.next()){
                book = new GenericTransacao();
                book.setGenre(rs.getString(3).equals("") ? "Sem Gênero" : rs.getString(3));
                book.setCdTransacao(rs.getInt(1));
                book.setImgLink(rs.getString(2).equals("") ? "https://docs.google.com/uc?id=0B03XPxH14xDTZS05WTNCNTJPZW8&export=download" : rs.getString(2));
                book.setTitulo(rs.getString(4));
                
                books.add(book);
            }
            
            ArrayList<String> uniqueGenreList = new ArrayList<String>();
            for(GenericTransacao b : books){
                if(!uniqueGenreList.contains(b.getGenre())){
                    uniqueGenreList.add(b.getGenre());
                }
            }
            
            //Lista de livros por genero retonada
            ArrayList<ArrayList<GenericTransacao>> lista = new ArrayList<ArrayList<GenericTransacao>>();
            ArrayList<GenericTransacao> perGenreLista;
            for(String g : uniqueGenreList){
                //Lista referente a um unico genero
                perGenreLista = new ArrayList<GenericTransacao>();
                for(GenericTransacao b : books){
                    if(g.equals(b.getGenre())){
                        perGenreLista.add(b);
                    }
                }
                
                lista.add(perGenreLista);
            }
            
            Collections.sort(lista, new Comparator<ArrayList<GenericTransacao>>(){
                @Override
                public int compare(ArrayList<GenericTransacao> o1, ArrayList<GenericTransacao> o2) {
                    return o2.size() - o1.size();
                }
                
            });
            //TESTE
            /*
            System.out.println("----------------------------------------");
            for(ArrayList<GenericTransacao> l : lista){
                for(GenericTransacao t : l){
                    System.out.println("Gen: " + t.getGenre());
                    System.out.println("Cod: " + t.getCdTransacao());
                    System.out.println("Img: " + t.getImgLink());
                }
                System.out.println("----------------------------------------");
            }
            */
            
            return lista;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    public List<Transacao> getUserTransacoes(Integer userId) throws URISyntaxException, SQLException {
        List<Transacao> transacoes = new ArrayList<Transacao>();
        Transacao transacao = null;
        
        StringBuilder query = new StringBuilder();
        query.append("select t1.cd_transacao,");
        query.append("t1.cd_livro_transacao,");
        query.append("t1.ic_transacao_finalizada_sim_nao,");
        query.append("t1.ic_transacao_autorizada_sim_nao,");
        query.append("t1.ic_transacao_ativa_sim_nao,");
        query.append("t1.qt_livro_transacao,");
        query.append("t1.ds_observacao_livro_transacao,");
        query.append("t1.dt_cadastro_transacao,");
        query.append("t1.dt_transacao_finalizada,");
        query.append("t1.cd_doador_usuario_transacao,");
        query.append("t1.cd_usuario_cadastrante");
        query.append(" from comunidade_do_livro.transacao t1");
        query.append(" where t1.cd_doador_usuario_transacao <> ");
        query.append(userId);
        query.append(" and t1.cd_usuario_cadastrante = ");
        query.append(userId);
        query.append(" and t1.cd_donatario_usuario_transacao = ");
        query.append(userId);
        query.append(" and t1.cd_doador_usuario_transacao is not null;");
        
        Connection conn = database.Connection.getConnection();
        ResultSet rs;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query.toString());

            while(rs.next()){
                transacao = new Transacao();
                transacao.setCdTransacao(rs.getInt("cd_transacao"));
                
                //Pega o livro da transacao
                LivroDAO livroDao = new LivroDAO();
                transacao.setLivro(livroDao.getLivroByCodigo(rs.getInt("cd_livro_transacao")));
                
                
                //Pega os donatarios das transacoes
                StringBuilder transacaoOrigemQuery = new StringBuilder();
                transacaoOrigemQuery.append("select t.cd_donatario_usuario_transacao");
                transacaoOrigemQuery.append(" from comunidade_do_livro.transacao t");
                transacaoOrigemQuery.append(" where t.cd_livro_transacao = ");
                transacaoOrigemQuery.append(rs.getInt("cd_livro_transacao"));
                transacaoOrigemQuery.append(" and t.cd_doador_usuario_transacao = ");
                transacaoOrigemQuery.append(rs.getInt("cd_doador_usuario_transacao"));
                transacaoOrigemQuery.append(" and t.cd_usuario_cadastrante <> ");
                transacaoOrigemQuery.append(rs.getInt("cd_usuario_cadastrante"));
                transacaoOrigemQuery.append(" and t.ic_transacao_finalizada_sim_nao = ");
                transacaoOrigemQuery.append(rs.getBoolean("ic_transacao_finalizada_sim_nao"));
                transacaoOrigemQuery.append(" and t.ic_transacao_autorizada_sim_nao = ");
                transacaoOrigemQuery.append(rs.getBoolean("ic_transacao_autorizada_sim_nao"));
                transacaoOrigemQuery.append(" and t.ic_transacao_ativa_sim_nao = ");
                transacaoOrigemQuery.append(rs.getBoolean("ic_transacao_ativa_sim_nao"));
                transacaoOrigemQuery.append(";");
                
                ResultSet origemRs = null;
                Statement origemStmt = null;
                origemStmt = conn.createStatement();
                origemRs = origemStmt.executeQuery(transacaoOrigemQuery.toString());
                UserDAO uDao = new UserDAO();
                while(origemRs.next()){
                    transacao.setDonatario(uDao.getUserById(origemRs.getInt(1)));
                }
                
                transacao.setIsFinalizada(rs.getBoolean("ic_transacao_finalizada_sim_nao"));
                transacao.setIsAutorizada(rs.getBoolean("ic_transacao_autorizada_sim_nao"));
                transacao.setIsAtivada(rs.getBoolean("ic_transacao_ativa_sim_nao"));
                transacao.setQtLivroTransacao(rs.getInt("qt_livro_transacao"));
                transacao.setDescricao(rs.getString("ds_observacao_livro_transacao"));
                transacao.setDataCadastro(rs.getDate("dt_cadastro_transacao"));
                transacao.setDataFinalizacao(rs.getDate("dt_transacao_finalizada"));
                
                //Pega o doador
                UserDAO userDao = new UserDAO();
                transacao.setDoador(userDao.getUserById(rs.getInt("cd_doador_usuario_transacao")));
                
                //Pega o cadastrante
                transacao.setCadastrante(userDao.getUserById(rs.getInt("cd_usuario_cadastrante")));
                
                transacoes.add(transacao);
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
    
    //Cadastros
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
        insertTransacao.append(") RETURNING cd_transacao;");
        
        Connection conn = database.Connection.getConnection();
        Integer rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet resSet = stmt.executeQuery(insertTransacao.toString());
            while(resSet.next()){
                rs = resSet.getInt(1);
            }
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    //Alteracoes
    public Integer alterarDoacaoLivroTransacao(int transacaoId, boolean doarSimNao, int qtdDoada) throws SQLException, URISyntaxException{
        StringBuilder updateTransacao = new StringBuilder(); 
        updateTransacao.append("UPDATE comunidade_do_livro.transacao "); 
        updateTransacao.append("SET ic_transacao_ativa_sim_nao = ");
        updateTransacao.append(doarSimNao);
        updateTransacao.append(", qt_livro_transacao = ");
        updateTransacao.append(qtdDoada);
        updateTransacao.append(" WHERE cd_transacao = ");
        updateTransacao.append(transacaoId);
        updateTransacao.append(" RETURNING cd_transacao;");
        
        Connection conn = database.Connection.getConnection();
        Integer rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet resSet = stmt.executeQuery(updateTransacao.toString());
            while(resSet.next()){
                rs = resSet.getInt(1);
            }
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    //Delecoes
    public Integer removerDoacaoLivroTransacao(int transacaoId) throws URISyntaxException, SQLException {
        StringBuilder deleteTransacao = new StringBuilder(); 
        deleteTransacao.append("DELETE FROM comunidade_do_livro.transacao ");
        deleteTransacao.append("where cd_transacao = ");
        deleteTransacao.append(transacaoId);
        deleteTransacao.append("RETURNING cd_transacao;");
        
        Connection conn = database.Connection.getConnection();
        Integer rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet resSet = stmt.executeQuery(deleteTransacao.toString());
            while(resSet.next()){
                rs = resSet.getInt(1);
            }
            return rs;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    /**
     * Cadastra uma transacao apontando o interesse de um usuario em um livro disponibilizado previamente em uma transacao
     * 
     * @param userId Usuario candidato
     * @param qtdLivrosCandidatados Quantidade de livros desejada pelo candidadto
     * @param transacaoId Codigo da transacao na qual o quandidadate esta interessado
     * @return
     * @throws SQLException
     * @throws URISyntaxException 
     */
    public int candidatarTransacaoOp0(Integer userId, Integer qtdLivrosCandidatados, Integer transacaoId) throws SQLException, URISyntaxException{
        StringBuilder query = new StringBuilder();
        query.append("insert into comunidade_do_livro.transacao (");
        query.append("cd_doador_usuario_transacao,");
        query.append("cd_donatario_usuario_transacao,");
        query.append("cd_usuario_cadastrante,");
        query.append("cd_livro_transacao,");
        query.append("ic_transacao_finalizada_sim_nao,");
        query.append("ic_transacao_autorizada_sim_nao,");
        query.append("ic_transacao_ativa_sim_nao,");
        query.append("qt_livro_transacao,");
        query.append("ds_observacao_livro_transacao,");
        query.append("dt_cadastro_transacao,");
        query.append("dt_transacao_finalizada");
        query.append(")");
        query.append("select cd_doador_usuario_transacao,");
        query.append(userId);
        query.append(",");
        query.append(userId);
        query.append(",");
        query.append("cd_livro_transacao,");
        query.append("ic_transacao_finalizada_sim_nao,");
        query.append("ic_transacao_autorizada_sim_nao,");
        query.append("ic_transacao_ativa_sim_nao,");
        query.append(qtdLivrosCandidatados);
        query.append(",");
        query.append("ds_observacao_livro_transacao,");
        query.append("dt_cadastro_transacao,");
        query.append("dt_transacao_finalizada");
        query.append(" from comunidade_do_livro.transacao where cd_transacao = ");
        query.append(transacaoId);
        query.append(";");

        Connection conn = database.Connection.getConnection();
        Integer rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(query.toString());
        }  catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }  finally {
            stmt.close();
            conn.close();
        }
    }
    
    //Classe para facilitar a consulta inicial de livros para doacao
    public class GenericTransacao {
        private String genre;
        private Integer cdTransacao;
        private String imgLink;
        private String titulo;

        public GenericTransacao() {
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public Integer getCdTransacao() {
            return cdTransacao;
        }

        public void setCdTransacao(Integer cdTrsanacao) {
            this.cdTransacao = cdTrsanacao;
        }

        public String getImgLink() {
            return imgLink;
        }

        public void setImgLink(String imgLink) {
            this.imgLink = imgLink;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
        
    }
    
} 
