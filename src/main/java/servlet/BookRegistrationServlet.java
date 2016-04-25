/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dao.LivroDAO;
import dao.TransacaoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Livro;
import model.Transacao;
import model.User;

/**
 *
 * @author carlos
 */

@WebServlet(
    name = "BookRegistrationServlet", 
    urlPatterns = {"/cadastrar_livro"}
)
public class BookRegistrationServlet extends HttpServlet {
    public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doPost(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        response.setContentType("application/json");
        
        //Pega os parametros do livro
        String isbn = request.getParameter("isbn");
        String publicacao = request.getParameter("publicacao");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String editora = request.getParameter("editora");
        String descricao = request.getParameter("descricao");
        String genero = request.getParameter("genero");
        String idioma = request.getParameter("idioma");
        Integer usuarioId = Integer.parseInt(request.getParameter("userId"));
        
        Integer qtdPaginas = null;
        try{
            qtdPaginas = Integer.parseInt(request.getParameter("qtdPaginas"));
        } catch(NumberFormatException ex) {
            qtdPaginas = null;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        String capa = request.getParameter("capa");
        
        Livro livro = new Livro();
        livro.setAutor(autor);
        livro.setCapaLink(capa);
        livro.setDataPublicacao(publicacao);
        livro.setDescricao(descricao);
        livro.setEditora(editora);
        livro.setGenero(genero);
        livro.setIdioma(idioma);
        livro.setIsbn(isbn);
        livro.setQtdPaginas(qtdPaginas);
        livro.setTitulo(titulo);
        
        LivroDAO dao = new LivroDAO();
        
        //Grava o livro no banco
        boolean sucesso = false;
        Integer cdTransacao = null;
        Integer cdLivro = null;
        try {
            cdLivro = dao.registerLivro(livro);
            if(cdLivro != null){
                sucesso = false;
            } 
            sucesso = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            sucesso = false;
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            sucesso = false;
        }
        
        //Grava o livro como uma transacao nao iniciada
        if(sucesso){
            
            Livro livroAux = null;
            try {
                livroAux = dao.getLivroByISBN(isbn);
            } catch (SQLException ex) {
                sucesso = false;
                ex.printStackTrace();
            }
            
            Transacao t = new Transacao();
            User doador = new User();
            doador.setId(usuarioId);
            t.setDoador(doador);
            t.setDonatario(null);
            t.setCadastrante(doador);
            t.setDescricao(null);
            t.setIsAtivada(false);
            t.setIsAutorizada(false);
            t.setIsFinalizada(false);
            t.setQtLivroTransacao(0);
            t.setLivro(livroAux);
            t.setDataCadastro(new Date());
            t.setDataFinalizacao(null);
            
            TransacaoDAO tDao = new TransacaoDAO();
            try {
                cdTransacao = tDao.registerTransacao(t);
            } catch (SQLException ex) {
                ex.printStackTrace();
                sucesso = false;
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
                sucesso = false;
            }
        }
        
        
        //Retorna o JSON
        PrintWriter out = response.getWriter();
        
        String r = "{\"success\":"+ String.valueOf(sucesso) +", \"cdTransacao\":"+cdTransacao+"}";
        out.print(r);
        out.flush();        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
