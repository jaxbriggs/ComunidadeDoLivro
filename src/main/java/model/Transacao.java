/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author carlos
 */
public class Transacao implements Serializable {
    private Integer cdTransacao;
    private Integer rowNum;
    private User doador;
    private User donatario;
    private User cadastrante;
    private Boolean isFinalizada;
    private Boolean isAutorizada;
    private Boolean isAtivada;
    private Integer qtLivroTransacao;
    private Livro livro;
    private String descricao;
    private String dataCadastro;
    private String dataFinalizacao;
    
    private transient SimpleDateFormat ft = 
            new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
    
    public Transacao() {
    }

    public Integer getCdTransacao() {
        return cdTransacao;
    }

    public void setCdTransacao(Integer cdTransacao) {
        this.cdTransacao = cdTransacao;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public SimpleDateFormat getFt() {
        return ft;
    }

    public void setFt(SimpleDateFormat ft) {
        this.ft = ft;
    }

    public User getDoador() {
        return doador;
    }

    public void setDoador(User doador) {
        this.doador = doador;
    }

    public User getDonatario() {
        return donatario;
    }

    public void setDonatario(User donatario) {
        this.donatario = donatario;
    }

    public User getCadastrante() {
        return cadastrante;
    }

    public void setCadastrante(User cadastrante) {
        this.cadastrante = cadastrante;
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

    public Integer getQtLivroTransacao() {
        return qtLivroTransacao;
    }

    public void setQtLivroTransacao(Integer qtLivroTransacao) {
        this.qtLivroTransacao = qtLivroTransacao;
    }    

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = 
                dataCadastro != null ? "'" + ft.format(dataCadastro) + "'" : null;
    }

    public String getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(Date dataFinalizacao) {
        this.dataFinalizacao = 
                dataFinalizacao != null ? "'" + ft.format(dataFinalizacao) + "'" : null;
    }
    
    
}
