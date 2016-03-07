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
import model.Endereco;
import model.User;

/**
 *
 * @author carlos
 */
public class UserDAO extends GeneralDAO{
    
    public boolean registerUser(User u) throws SQLException, URISyntaxException{
        
        int enderecoId = -1;
        
        if(registerAddress(u.getEndereco())){
            
            //Pega o endereco que acabou de ser cadastrado
            String query = "SELECT cd_endereco\n" +
                                    "FROM comunidade_do_livro.ENDERECO\n" +
                                    "WHERE cd_codigo_postal_endereco = ? AND\n" +
                                    "      nm_rua_endereco = ? AND \n" +
                                    "      cd_numero_endereco = ? AND \n" +
                                    "      nm_bairro_endereco = ? AND \n" +
                                    "      nm_cidade_endereco = ? AND  \n" +
                                    "      sg_unidade_federativa_endereco = ?;";
            
            Connection conn1 = database.Connection.getConnection();
            ResultSet rs = null;
            PreparedStatement pstmt1 = null;
            try {
                pstmt1 = conn1.prepareStatement(query);
                pstmt1.setString(1, u.getEndereco().getCep());
                pstmt1.setString(2, u.getEndereco().getRua());
                pstmt1.setInt(3, u.getEndereco().getNumero());
                pstmt1.setString(4, u.getEndereco().getBairro());
                pstmt1.setString(5, u.getEndereco().getCidade());
                pstmt1.setString(6, u.getEndereco().getEstado());
                rs =  pstmt1.executeQuery();
                while (rs.next()) {
                    enderecoId = rs.getInt("cd_endereco");
                }
            }  catch (SQLException ex) {
                ex.printStackTrace();
            }  finally {
                rs.close();
                pstmt1.close();
                conn1.close();
            }    
        }
        
        String insertUsuario =  "INSERT INTO comunidade_do_livro.USUARIO (cd_endereco_usuario, \n" +
                                "		      nm_usuario,\n" +
                                "		      nm_login_usuario, \n" +
                                "		      nm_email_usuario, \n" +
                                "		      cd_senha_usuario, \n" +
                                "		      ic_ativo_usuario_sim_nao, \n" +
                                "		      ic_admin_usuario_sim_nao,\n" +
                                "		      cd_cpf_usuario,\n" +
                                "		      cd_cnpj_usuario,\n" +
                                "		      cd_telefone_usuario,\n" +
                                "		      cd_celular_usuario)\n" +
                                "VALUES (?, \n" +
                                "	 ?,\n" +
                                "	 ?, \n" +
                                "	 ?, \n" +
                                "	 ?, \n" +
                                "	 ?, \n" +
                                "	 ?, \n" +
                                "	 ?,\n" +
                                "	 ?,\n" +
                                "	 ?,\n" +
                                "	 ?);";
        
        Connection conn2 = database.Connection.getConnection();
        PreparedStatement pstmt2 = null;
        try {
            pstmt2 = conn2.prepareStatement(insertUsuario);
            pstmt2.setInt(1, enderecoId == -1 ? null : enderecoId);
            pstmt2.setString(2, u.getName());
            pstmt2.setString(3, u.getLogin());
            pstmt2.setString(4, u.getEmail());
            pstmt2.setString(5, u.getSenha());
            pstmt2.setBoolean(6, u.isIsAtivo());
            pstmt2.setBoolean(7, u.isIsAdmin());
            pstmt2.setString(8, u.getCpf());
            pstmt2.setString(9, u.getCnpj());
            pstmt2.setString(10, u.getTelefone());
            pstmt2.setString(11, u.getCelular());
            
            return pstmt2.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            pstmt2.close();
            conn2.close();
        }
        
        return false;
    }
    
    private boolean registerAddress(Endereco e) throws SQLException {
        String insertEndereco = "INSERT INTO comunidade_do_livro.ENDERECO (cd_codigo_postal_endereco, nm_rua_endereco, cd_numero_endereco, nm_bairro_endereco, nm_cidade_endereco, sg_unidade_federativa_endereco)\n" +
                                "    VALUES (?, ?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(insertEndereco);
            pstmt.setString(1, e.getCep());
            pstmt.setString(2, e.getRua());
            pstmt.setInt(3, e.getNumero());
            pstmt.setString(4, e.getBairro());
            pstmt.setString(5, e.getCidade());
            pstmt.setString(6, e.getEstado());
            pstmt.execute();
            return true;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            pstmt.close();
            conn.close();
        }
        
        return false;
        
    }
}
