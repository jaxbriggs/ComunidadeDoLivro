/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Livro;

/**
 *
 * @author carlos
 */
public class LivroDAO {
    
    public boolean registerLivro(Livro l) throws SQLException, URISyntaxException {
        
        if(!isLivroISBNAlreadyRegistered(l)){
            String insertLivro = "INSERT INTO comunidade_do_livro.Livro\n" +
                                 "(\n" +
                                 "	nm_titulo_livro,\n" +
                                 "	nm_autor_livro,\n" +
                                 "	nm_editora_livro,\n" +
                                 "	dt_publicacao_livro,\n" +
                                 "	ds_livro,\n" +
                                 "	qt_paginas_livro,\n" +
                                 "	nm_genero_livro,\n" +
                                 "	im_capa_livro,\n" +
                                 "	nm_idioma_livro,\n" +
                                 "	cd_isbn_livro\n" +
                                 ") VALUES (?,?,?,?,?,?,?,?,?,?);";

            Connection conn = database.Connection.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(insertLivro);
                pstmt.setString(1, l.getTitulo());
                pstmt.setString(2, l.getAutor());
                pstmt.setString(3, l.getEditora());
                pstmt.setString(4, l.getDataPublicacao());
                pstmt.setString(5, l.getDescricao());
                if(l.getQtdPaginas() == null){
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                } else {
                    pstmt.setInt(6, l.getQtdPaginas());
                }
                pstmt.setString(7, l.getGenero());
                pstmt.setString(8, l.getCapaLink());
                pstmt.setString(9, l.getIdioma());
                pstmt.setString(10, l.getIsbn());

                return !pstmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                pstmt.close();
                conn.close();
            }
            return false;
        } else {
            return true;
        }                
    }
    
    public boolean isLivroISBNAlreadyRegistered(Livro l) throws SQLException {
        String query = "select count(*) \n" +
                        "from comunidade_do_livro.Livro\n" +
                        "where cd_isbn_livro = ?;";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, l.getIsbn());
            
            //Faz o select
            ResultSet rs = pstmt.executeQuery();
            int qtLivrosRetornados = 0;
            while (rs.next()) {
                //Pega a quantidade de livros retornada
                qtLivrosRetornados = rs.getInt(1);
            }
            
            return qtLivrosRetornados > 0 ? true : false;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } finally {
            pstmt.close();
            conn.close();
        }
         return false;
    }
    
    public Livro getLivroByISBN(String isbn) throws SQLException{
        Livro livro;
        
        StringBuilder builder = new StringBuilder();
        builder.append("select cd_livro, ");
        builder.append("cd_isbn_livro, ");
        builder.append("nm_titulo_livro, ");
        builder.append("nm_autor_livro, ");
        builder.append("nm_editora_livro, ");
        builder.append("dt_publicacao_livro, ");
        builder.append("ds_livro, ");
        builder.append("qt_paginas_livro, ");
        builder.append("nm_genero_livro, ");
        builder.append("im_capa_livro, ");
        builder.append("nm_idioma_livro ");
        builder.append("from comunidade_do_livro.livro ");
        builder.append("where cd_isbn_livro = ?");
        
        String query = builder.toString();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, isbn);
            
            //Faz o select
            ResultSet rs = pstmt.executeQuery();
            
            livro = new Livro();
            
            while (rs.next()) {
                //Pega o livro retornado
                livro.setAutor(rs.getString("nm_autor_livro"));
                livro.setCapaLink(rs.getString("im_capa_livro"));
                livro.setCodigo(rs.getInt("cd_livro"));
                livro.setDataPublicacao(rs.getString("dt_publicacao_livro"));
                livro.setDescricao(rs.getString("ds_livro"));
                livro.setEditora(rs.getString("nm_editora_livro"));
                livro.setGenero(rs.getString("nm_genero_livro"));
                livro.setIdioma(rs.getString("nm_idioma_livro"));
                livro.setIsbn(rs.getString("cd_isbn_livro"));
                livro.setQtdPaginas(rs.getInt("qt_paginas_livro"));
                livro.setTitulo(rs.getString("nm_titulo_livro"));
            }
            
            return livro;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } finally {
            pstmt.close();
            conn.close();
        }
         return null;
    }
    
    public Livro getLivroByCodigo(Integer codigo) throws SQLException{
        Livro livro;
        
        StringBuilder builder = new StringBuilder();
        builder.append("select cd_livro, ");
        builder.append("cd_isbn_livro, ");
        builder.append("nm_titulo_livro, ");
        builder.append("nm_autor_livro, ");
        builder.append("nm_editora_livro, ");
        builder.append("dt_publicacao_livro, ");
        builder.append("ds_livro, ");
        builder.append("qt_paginas_livro, ");
        builder.append("nm_genero_livro, ");
        builder.append("im_capa_livro, ");
        builder.append("nm_idioma_livro ");
        builder.append("from comunidade_do_livro.livro ");
        builder.append("where cd_livro = ?");
        
        String query = builder.toString();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, codigo);
            
            //Faz o select
            ResultSet rs = pstmt.executeQuery();
            
            livro = new Livro();
            
            while (rs.next()) {
                //Pega o livro retornado
                livro.setAutor(rs.getString("nm_autor_livro"));
                livro.setCapaLink(rs.getString("im_capa_livro"));
                livro.setCodigo(rs.getInt("cd_livro"));
                livro.setDataPublicacao(rs.getString("dt_publicacao_livro"));
                livro.setDescricao(rs.getString("ds_livro"));
                livro.setEditora(rs.getString("nm_editora_livro"));
                livro.setGenero(rs.getString("nm_genero_livro"));
                livro.setIdioma(rs.getString("nm_idioma_livro"));
                livro.setIsbn(rs.getString("cd_isbn_livro"));
                livro.setQtdPaginas(rs.getInt("qt_paginas_livro"));
                livro.setTitulo(rs.getString("nm_titulo_livro"));
            }
            
            return livro;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } finally {
            pstmt.close();
            conn.close();
        }
         return null;
    }
    
    public List<Livro> getLivrosByTitulo(String titulo) throws SQLException{
        List<Livro> livros;
        
        StringBuilder builder = new StringBuilder();
        builder.append("select cd_livro, ");
        builder.append("cd_isbn_livro, ");
        builder.append("nm_titulo_livro, ");
        builder.append("nm_autor_livro, ");
        builder.append("nm_editora_livro, ");
        builder.append("dt_publicacao_livro, ");
        builder.append("ds_livro, ");
        builder.append("qt_paginas_livro, ");
        builder.append("nm_genero_livro, ");
        builder.append("im_capa_livro, ");
        builder.append("nm_idioma_livro ");
        builder.append("from comunidade_do_livro.livro ");
        builder.append("where lower(nm_titulo_livro) like lower(?)");
        
        String query = builder.toString();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + titulo + "%");
            
            //Faz o select
            ResultSet rs = pstmt.executeQuery();
            
            livros = new ArrayList<Livro>();
            Livro livro = null;
            
            while (rs.next()) {
                livro = new Livro();
                //Pega o livro retornado
                livro.setAutor(rs.getString("nm_autor_livro"));
                livro.setCapaLink(rs.getString("im_capa_livro"));
                livro.setCodigo(rs.getInt("cd_livro"));
                livro.setDataPublicacao(rs.getString("dt_publicacao_livro"));
                livro.setDescricao(rs.getString("ds_livro"));
                livro.setEditora(rs.getString("nm_editora_livro"));
                livro.setGenero(rs.getString("nm_genero_livro"));
                livro.setIdioma(rs.getString("nm_idioma_livro"));
                livro.setIsbn(rs.getString("cd_isbn_livro"));
                livro.setQtdPaginas(rs.getInt("qt_paginas_livro"));
                livro.setTitulo(rs.getString("nm_titulo_livro"));
                
                livros.add(livro);
            }
            
            return livros;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } finally {
            pstmt.close();
            conn.close();
        }
         return null;
    }
}

