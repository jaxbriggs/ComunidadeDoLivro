/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dao.LivroDAO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Livro;

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
        ServletOutputStream out = response.getOutputStream();
        
        String isbn = request.getParameter("isbn");
        String publicacao = request.getParameter("publicacao");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String editora = request.getParameter("editora");
        String descricao = request.getParameter("descricao");
        String genero = request.getParameter("genero");
        String idioma = request.getParameter("idioma");
        Integer qtdPaginas = null;
        try{
            qtdPaginas = Integer.parseInt(request.getParameter("qtdPaginas"));
        } catch(Exception ex) {
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
        
        boolean sucesso = false;
        try {
            sucesso = dao.registerLivro(livro);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        
        //Retorna o JSON
        String r = "{ success : \""+ String.valueOf(sucesso) +"\"}";
        out.print(r);
        out.flush();        
    }
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
