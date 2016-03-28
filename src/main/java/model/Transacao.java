/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author carlos
 */
public class Transacao {
    private Integer cdTransacao;
    private User cdDoador;
    private User cdDonatario;
    private Boolean isFinalizada;
    private Boolean isAutorizada;
    private Boolean isAtivada;
    private Boolean qtLivroTransacao;
    private Livro livro;

    public Transacao() {
    }

    public Integer getCdTransacao() {
        return cdTransacao;
    }

    public void setCdTransacao(Integer cdTransacao) {
        this.cdTransacao = cdTransacao;
    }

    public User getCdDoador() {
        return cdDoador;
    }

    public void setCdDoador(User cdDoador) {
        this.cdDoador = cdDoador;
    }

    public User getCdDonatario() {
        return cdDonatario;
    }

    public void setCdDonatario(User cdDonatario) {
        this.cdDonatario = cdDonatario;
    }

    public Boolean getIsFinalizada() {
        return isFinalizada;
    }

    public void setIsFinalizada(Boolean isFinalizada) {
        this.isFinalizada = isFinalizada;
    }

    public Boolean getIsAutorizada() {
        return isAutorizada;
    }

    public void setIsAutorizada(Boolean isAutorizada) {
        this.isAutorizada = isAutorizada;
    }

    public Boolean getIsAtivada() {
        return isAtivada;
    }

    public void setIsAtivada(Boolean isAtivada) {
        this.isAtivada = isAtivada;
    }

    public Boolean getQtLivroTransacao() {
        return qtLivroTransacao;
    }

    public void setQtLivroTransacao(Boolean qtLivroTransacao) {
        this.qtLivroTransacao = qtLivroTransacao;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    
}
