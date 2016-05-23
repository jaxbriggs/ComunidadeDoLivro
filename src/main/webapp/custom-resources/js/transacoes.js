$(document).ready (function(){

	//Botao que cancela transações
	$(".cancelarTransacao").unbind('click');
	$(".cancelarTransacao").click(function(){
        var idTransacaoCancelada = this.id.substring(17, this.id.length);
        var idCandidatoDeseleito = $("#myTrans" + idTransacaoCancelada).find($('#donatarioId' + idTransacaoCancelada)).val();
        cancelarTransacao(idTransacaoCancelada, idCandidatoDeseleito);
	});

	//Botão que desiste de transações
	$(".desistirTransacao").unbind('click');
	$(".desistirTransacao").click(function(){
        var idTransacaoDesistida = this.id.substring(17, this.id.length);
        var idCandidatoDesistente = $("#userId").val();
        desistirTransacao(idTransacaoDesistida, idCandidatoDesistente);
	});

	//Botão que confirma o recebimento do livro e finaliza a transação
	$(".confirmarRecebimento").unbind('click');
	$(".confirmarRecebimento").click(function(){
        var idTransacaoFinalizada = this.id.substring(20, this.id.length);
        confirmarRecebimentoDoLivro(idTransacaoFinalizada);
	});

	//Botão que reabre a transação
	$(".reabrirTransacao").unbind('click');
	$(".reabrirTransacao").click(function(){
        var idTransacaoReaberta = this.id.substring(16, this.id.length);
        reabrirTransacao(idTransacaoReaberta);
	});

});

function cancelarTransacao(transacaoId, candidatoId){
	jQuery.ajax({
        url: '/eleicao_transacao',
        type: 'GET',
        contentType: 'application/json',
        data: {deselegerCandidatoByIdAndTransacao: JSON.stringify({transacaoId:transacaoId, candidatoId:candidatoId})},   
        dataType: 'json',
        success: function(data){
            if(data.success){
                $("#myTrans" + transacaoId).remove();
            } else {
            	alert("Ops, Falha ao cancelar transacao!");
            }
        },
        fail: function() {
            alert("Ops, Falha ao cancelar transacao!");
        },
        always: function() {
            console.log( "complete" );
        }
    });
}

function desistirTransacao(transacaoId, candidatoId){
	jQuery.ajax({
        url: '/eleicao_transacao',
        type: 'GET',
        contentType: 'application/json',
        data: {desistirOnTransacao: JSON.stringify({transacaoId:transacaoId, candidatoId:candidatoId})},   
        dataType: 'json',
        success: function(data){
            if(data.success){
            	$("#myTrans" + transacaoId).remove();    
            } else {
            	alert("Ops, Falha ao desistir da transacao!");	
            }
        },
        fail: function() {
            alert("Ops, Falha ao desistir da transacao!");
        },
        always: function() {
            console.log( "complete" );
        }
    });
}

function confirmarRecebimentoDoLivro(transacaoId){
	jQuery.ajax({
        url: '/eleicao_transacao',
        type: 'GET',
        contentType: 'application/json',
        data: {confirmarRecebimento: JSON.stringify({transacaoId:transacaoId})},   
        dataType: 'json',
        success: function(data){
            if(data.success){
            	console.log(data);
            } else {
            	alert("Ops, Falha ao confirmar o recebimento do livro!");	
            }
        },
        fail: function() {
            alert("Ops, Falha ao confirmar o recebimento do livro!");
        },
        always: function() {
            console.log( "complete" );
        }
    });
}


function reabrirTransacao(transacaoId){
	jQuery.ajax({
        url: '/eleicao_transacao',
        type: 'GET',
        contentType: 'application/json',
        data: {reabrirTransacao: JSON.stringify({transacaoId:transacaoId})},   
        dataType: 'json',
        success: function(data){
            if(data.success){
            	console.log(data);
            } else {
            	alert("Ops, Falha ao reabrir transacao!");	
            }
        },
        fail: function() {
            alert("Ops, Falha ao reabrir transacao!");	
        },
        always: function() {
            console.log( "complete" );
        }
    });
}


