/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Transacao;

/**
 *
 * @author carlos
 */
public class TransacaoDAO {
    public Transacao registerTransacao(Transacao t){
        
        String insertTransacao = "INSERT INTO comunidade_do_livro.TRANSACAO(\n" +
                                 "  cd_doador_usuario_transacao,\n" +
                                 "  cd_donatario_usuario_transacao,\n" +
                                 "  cd_livro_transacao,\n" +
                                 "  ic_transacao_finalizada_sim_nao,\n" +
                                 "  ic_transacao_autorizada_sim_nao,\n" +
                                 "  ic_transacao_ativa_sim_nao,\n" +
                                 "  qt_livro_transacao,\n" +
                                 "  ds_observacao_livro_transacao\n" +
                                 ") VALUES (\n" +
                                 t.getCdDoador() + ",\n" +
                                 t.getCdDonatario() + ",\n" +
                                 t.getLivro() + ",\n" +
                                 t.getCdDoador() + ",\n" +
                                 t.getCdDoador() + ",\n" +
                                 t.getCdDoador() + ",\n" +
                                 t.getCdDoador() + ",\n" +
                                 t.getCdDoador() + ",\n" +
                                 ");";
        return null;
    } 
}
