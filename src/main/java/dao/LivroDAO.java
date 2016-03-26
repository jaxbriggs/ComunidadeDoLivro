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
import model.Livro;

/**
 *
 * @author carlos
 */
public class LivroDAO {
    
    public boolean registerLivro(Livro l) throws SQLException, URISyntaxException {
        
        if(!isLivroISBNAlreadyRegistered(l)){
            String insertLivro = "INSERT INTO comunidade_do_livro.Livro(\n" +
                                 "	nm_titulo_livro,\n" +
                                 "	nm_autor_livro,\n" +
                                 "	nm_editora_livro,\n" +
                                 "	dt_publicacao_livro,\n" +
                                 "	ds_livro,\n" +
                                 "	qt_paginas_livro,\n" +
                                 "	nm_genero_livro,\n" +
                                 "	im_capa_livro,\n" +
                                 "	nm_idioma_livro,\n" +
                                 "	cd_isbn_livro \n" +
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
                pstmt.setInt(6, l.getQtdPaginas());
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
}

