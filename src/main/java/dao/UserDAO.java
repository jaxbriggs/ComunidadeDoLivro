/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public User getUserByLoginOrEmail(String loginOrEmail, String senha) throws SQLException {
        String query = "select  u.cd_usuario, \n" +
                        "       u.nm_usuario,\n" +
                        "       u.nm_login_usuario,\n" +
                        "       u.nm_email_usuario,\n" +
                        "       u.cd_senha_usuario,\n" +
                        "       u.ic_ativo_usuario_sim_nao,\n" +
                        "       u.ic_admin_usuario_sim_nao,\n" +
                        "       u.cd_cpf_usuario,\n" +
                        "       u.cd_cnpj_usuario, \n" +
                        "       u.cd_telefone_usuario,\n" +
                        "       u.cd_celular_usuario,\n" +
                        "       e.cd_codigo_postal_endereco,\n" +
                        "       e.nm_rua_endereco,\n" +
                        "       e.cd_numero_endereco,\n" +
                        "       e.nm_bairro_endereco,\n" +
                        "       e.nm_cidade_endereco,\n" +
                        "       e.sg_unidade_federativa_endereco\n" +
                        "from comunidade_do_livro.usuario u\n" +
                        "inner join comunidade_do_livro.endereco e on (u.cd_endereco_usuario = e.cd_endereco)\n" +
                        "where (\n" +
                        "	(u.nm_login_usuario = ? and u.cd_senha_usuario = ?) or\n" +
                        "        (u.nm_email_usuario = ? and u.cd_senha_usuario = ?)\n" +
                        "      );";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            
            //Tira o hash da senha digitada
            StringBuilder senhaStr = new StringBuilder();
            try { 
                byte[] senhaCripto = null;
                MessageDigest d = MessageDigest.getInstance("MD5");
                senhaCripto = d.digest(senha.getBytes(StandardCharsets.ISO_8859_1));
                for (int i = 0; i < senhaCripto.length; i++)
                    senhaStr.append(Integer.toString((senhaCripto[i] & 0xff) + 0x100, 16).substring(1)); 
            } catch (NoSuchAlgorithmException ex){
                ex.printStackTrace();
            }
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, loginOrEmail);
            pstmt.setString(2, senhaStr.toString());
            pstmt.setString(3, loginOrEmail);
            pstmt.setString(4, senhaStr.toString());
            
            //Faz o select
            ResultSet rs = pstmt.executeQuery();
            User u = null;
            Endereco endereco = null;
            while (rs.next()) {
                //Pega o endereco
                endereco = new Endereco();
                endereco.setBairro(rs.getString("nm_bairro_endereco"));
                endereco.setCep(rs.getString("cd_codigo_postal_endereco"));
                endereco.setCidade(rs.getString("nm_cidade_endereco"));
                endereco.setEstado(rs.getString("sg_unidade_federativa_endereco"));
                endereco.setNumero(Integer.parseInt(rs.getString("cd_numero_endereco")));
                endereco.setRua(rs.getString("nm_rua_endereco"));
                        
                //Configura o usuario
                u = new User();
                u.setId(rs.getInt("cd_usuario"));
                u.setCelular(rs.getString("cd_celular_usuario"));
                u.setCnpj(rs.getString("cd_cnpj_usuario"));
                u.setCpf(rs.getString("cd_cpf_usuario"));
                u.setEmail(rs.getString("nm_email_usuario"));
                u.setEndereco(endereco);
                u.setIsAdmin(rs.getBoolean("ic_admin_usuario_sim_nao"));
                u.setIsAtivo(rs.getBoolean("ic_ativo_usuario_sim_nao"));
                u.setLogin(rs.getString("nm_login_usuario"));
                u.setName(rs.getString("nm_usuario"));
                u.setSenha(rs.getString("cd_senha_usuario"));
                u.setTelefone(rs.getString("cd_telefone_usuario"));
            }
            
            return u;
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
    
    public User getUserById(Integer id) throws SQLException {
        String query = "select  u.cd_usuario, \n" +
                        "       u.nm_usuario,\n" +
                        "       u.nm_login_usuario,\n" +
                        "       u.nm_email_usuario,\n" +
                        "       u.cd_senha_usuario,\n" +
                        "       u.ic_ativo_usuario_sim_nao,\n" +
                        "       u.ic_admin_usuario_sim_nao,\n" +
                        "       u.cd_cpf_usuario,\n" +
                        "       u.cd_cnpj_usuario, \n" +
                        "       u.cd_telefone_usuario,\n" +
                        "       u.cd_celular_usuario,\n" +
                        "       e.cd_codigo_postal_endereco,\n" +
                        "       e.nm_rua_endereco,\n" +
                        "       e.cd_numero_endereco,\n" +
                        "       e.nm_bairro_endereco,\n" +
                        "       e.nm_cidade_endereco,\n" +
                        "       e.sg_unidade_federativa_endereco\n" +
                        "from comunidade_do_livro.usuario u\n" +
                        "inner join comunidade_do_livro.endereco e on (u.cd_endereco_usuario = e.cd_endereco)\n" +
                        "where u.cd_usuario = " + id + ";";
        
        Connection conn = null;
        Statement stmt = null;
        try {
            
            //Prepara o statement para a consulta
            conn = database.Connection.getConnection();
            stmt = conn.createStatement();
            
            //Faz o select
            ResultSet rs = stmt.executeQuery(query);
            User u = null;
            Endereco endereco = null;
            while (rs.next()) {
                //Pega o endereco
                endereco = new Endereco();
                endereco.setBairro(rs.getString("nm_bairro_endereco"));
                endereco.setCep(rs.getString("cd_codigo_postal_endereco"));
                endereco.setCidade(rs.getString("nm_cidade_endereco"));
                endereco.setEstado(rs.getString("sg_unidade_federativa_endereco"));
                endereco.setNumero(Integer.parseInt(rs.getString("cd_numero_endereco")));
                endereco.setRua(rs.getString("nm_rua_endereco"));
                        
                //Configura o usuario
                u = new User();
                u.setId(rs.getInt("cd_usuario"));
                u.setCelular(rs.getString("cd_celular_usuario"));
                u.setCnpj(rs.getString("cd_cnpj_usuario"));
                u.setCpf(rs.getString("cd_cpf_usuario"));
                u.setEmail(rs.getString("nm_email_usuario"));
                u.setEndereco(endereco);
                u.setIsAdmin(rs.getBoolean("ic_admin_usuario_sim_nao"));
                u.setIsAtivo(rs.getBoolean("ic_ativo_usuario_sim_nao"));
                u.setLogin(rs.getString("nm_login_usuario"));
                u.setName(rs.getString("nm_usuario"));
                u.setSenha(rs.getString("cd_senha_usuario"));
                u.setTelefone(rs.getString("cd_telefone_usuario"));
            }
            
            return u;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } finally {
            stmt.close();
            conn.close();
        }
        
        return null;
    }
}
