package model;

import java.sql.Timestamp;

/**
 *
 * @author carlos
 */
public class Livro {
    private Integer codigo;
    private String isbn;
    private String autor;
    private String titulo;
    private String editora;
    private String dataPublicacao;
    private String descricao;
    private Integer qtdPaginas;
    private String genero;
    private String capaLink;
    private String idioma;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }    

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQtdPaginas() {
        return qtdPaginas;
    }

    public void setQtdPaginas(Integer qtdPaginas) {
        this.qtdPaginas = qtdPaginas;
    }    

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCapaLink() {
        return capaLink;
    }

    public void setCapaLink(String capaLink) {
        this.capaLink = capaLink;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    @Override
    public boolean equals(Object livro){
        if(livro instanceof Livro){
            /*
            return ((Livro)livro).getAutor().equals(this.autor) &&
                   ((Livro)livro).getCapaLink().equals(this.capaLink) &&
                   ((Livro)livro).getDataPublicacao().equals(this.dataPublicacao) &&
                   ((Livro)livro).getDescricao().equals(this.descricao) &&
                   ((Livro)livro).getEditora().equals(this.editora) &&
                   ((Livro)livro).getGenero().equals(this.genero) &&
                   ((Livro)livro).getIdioma().equals(this.idioma) &&
                   ((Livro)livro).getIsbn().equals(this.isbn) &&
                   ((Livro)livro).getQtdPaginas().equals(this.qtdPaginas);
            */
            try{
                return this.isbn.equals(((Livro) livro).getIsbn());
            } catch(NullPointerException ex){
                return false;
            }
        }
        return false;
    }
}


