/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import apis.books.GoogleBooks;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dao.LivroDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.ObjectJson;
import model.Livro;

/**
 *
 * @author carlos
 */
@WebServlet(
    name = "BookSearchServlet", 
    urlPatterns = {"/busca_livro"}
)
public class BookSearchServlet extends HttpServlet{
     public void init() throws ServletException
    {
        // Do nothing
    }
    
    @Override
    public void doGet(HttpServletRequest request,
                   HttpServletResponse response)
    throws ServletException, IOException {
        
        //Prepara resposta do servidor
        PrintWriter out = response.getWriter();
        
        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        
        response.setContentType("application/json");
        
        Livro singleResult = null;
        List<Livro> multipleResults = null;
        
        LivroDAO dao = new LivroDAO();
        try {
            //Faz a consulta por isbn ou titulo
            if(isbn != null){
                singleResult = new Livro();
                singleResult = dao.getLivroByISBN(isbn);
            } else {
                multipleResults = new ArrayList<Livro>();
                
                List<Livro> livrosFromServer = dao.getLivrosByTitulo(titulo);
                multipleResults.addAll(livrosFromServer);
                
                if(multipleResults.size() < 15){
                    List<Livro> livrosFromApi = getLivrosFromApi(titulo);
                    for(Livro l : livrosFromApi){
                        if(multipleResults.size() < 15 && !multipleResults.contains(l)){
                            multipleResults.add(l);
                        }
                    }
                }
            }    
            
        } catch (Exception ex) {
            out.flush();
            //ex.printStackTrace();
        }
        
        String livroJson, livrosJson = null;
        if(singleResult != null){
            //Transforma o usuario em um objeto JSON
            livroJson = ObjectJson.getObjectJson(singleResult);
            out.print(livroJson);
        } else if(multipleResults != null){
            livrosJson = ObjectJson.getObjectsJson(multipleResults);
            out.print(livrosJson);
        }
        
        //Retorna o JSON
        out.flush();
        
    }
    
    private List<Livro> getLivrosFromApi(String titulo) throws Exception {
        
        //Faz a consulta na API => Retorno: Lista de Livros
        String livrosJson = GoogleBooks.getLivros(titulo.replace(" ", "+"), "intitle");
        
        if(livrosJson != null){
            JsonElement jElt = new JsonParser().parse(livrosJson);
            JsonObject jObj = jElt.getAsJsonObject();
            JsonArray jArray = jObj.getAsJsonArray("items");

            Livro l = null;
            List<Livro> livros = new ArrayList<Livro>();

            //Entra no for para transformar os livros em objetos
            if(jArray != null){
                for(int i = 0; i < jArray.size(); i++){

                    l = new Livro();

                    jObj = jArray.get(i).getAsJsonObject();

                    //Titulo
                    l.setTitulo(jObj.get("volumeInfo").getAsJsonObject().get("title") == null ? null : 
                                jObj.get("volumeInfo").getAsJsonObject().get("title").getAsString());

                    //Editora
                    l.setEditora(jObj.get("volumeInfo").getAsJsonObject().get("publisher") == null ? null : 
                                 jObj.get("volumeInfo").getAsJsonObject().get("publisher").getAsString());
                    //ISBN
                    l.setIsbn(jObj.get("volumeInfo").getAsJsonObject().getAsJsonArray("industryIdentifiers") == null ? null : 
                            (
                              jObj.get("volumeInfo").getAsJsonObject().getAsJsonArray("industryIdentifiers").get(0).getAsJsonObject().get("identifier") == null ? null :
                              jObj.get("volumeInfo").getAsJsonObject().getAsJsonArray("industryIdentifiers").get(0).getAsJsonObject().get("identifier").getAsString()
                            )
                    );

                    //Capa
                    l.setCapaLink(jObj.get("volumeInfo").getAsJsonObject().get("imageLinks") == null ? null : (
                                    jObj.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail") == null ? "" :
                                        jObj.get("volumeInfo").getAsJsonObject().get("imageLinks").getAsJsonObject().get("thumbnail").getAsString()
                                    )
                                  );
                    //Idioma
                    l.setIdioma(jObj.get("volumeInfo").getAsJsonObject().get("language") == null ? null :
                                jObj.get("volumeInfo").getAsJsonObject().get("language").getAsString()
                            );

                    //Autores
                    if(jObj.getAsJsonObject("volumeInfo").getAsJsonArray("authors") == null){
                        l.setAutor(null);
                    } else {
                        Gson converter = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        List<String> list = converter.fromJson(jObj.getAsJsonObject("volumeInfo").getAsJsonArray("authors"), type);
                        String autores = Arrays.toString(list.toArray());
                        autores = autores.replace("[", "").replace("]", "");
                        l.setAutor(autores);
                    }

                    //Data de publicacao
                    if(jObj.get("volumeInfo").getAsJsonObject().get("publishedDate") == null){
                        l.setDataPublicacao(null);
                    } else {
                        l.setDataPublicacao(jObj.get("volumeInfo").getAsJsonObject().get("publishedDate").getAsString());
                    }

                    //Descricao
                    if(jObj.get("volumeInfo").getAsJsonObject().get("description") == null){
                        l.setDescricao(null);
                    } else {
                        l.setDescricao(jObj.get("volumeInfo").getAsJsonObject().get("description").getAsString());
                    }

                    //Numero de paginas
                    if(jObj.get("volumeInfo").getAsJsonObject().get("pageCount") == null){
                        l.setQtdPaginas(null);
                    } else {
                        l.setQtdPaginas(jObj.get("volumeInfo").getAsJsonObject().get("pageCount").getAsInt());
                    }

                    //Genero
                    if(jObj.getAsJsonObject("volumeInfo").getAsJsonArray("categories") == null){
                        l.setGenero(null);
                    } else {
                        Gson converter = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        List<String> list = converter.fromJson(jObj.getAsJsonObject("volumeInfo").getAsJsonArray("categories"), type);
                        String generos = Arrays.toString(list.toArray());
                        generos = generos.replace("[", "").replace("]", "");
                        l.setGenero(generos);
                    }

                    livros.add(l);
                }
            }
            return livros;
        }

        return null;
    }
    
    
    @Override
     public void destroy() {
        // Finalization code...
     }
}
