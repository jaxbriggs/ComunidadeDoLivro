$(document).ready (function(){
    
    //Esconde a mensagem de livro cadastrado com sucesso
    $("#alertCadastroSucesso").hide();

    //Configura o tootip do remover livro
    $('[data-toggle="remover"]').tooltip();
    
    //Tooltip para o checkbox de doacao
    $("label").tooltip({'trigger':'hover', 'title': "Ligar/Desligar Doação"}); 

    //Configura o evento de doacao quando o checkbox de doacao e alterado
    $("input[class='toggle']").change(function(e) {
        if(this.checked){
            var qtdLivrosDoados = prompt("Quantos volumes deste livro serão doados?", "1");
            if(qtdLivrosDoados != null){
                doarLivro($(this).attr("id"), this.checked, qtdLivrosDoados);
            } else {
                $(this).attr('checked', !this.checked); 
            }
        } else {
            doarLivro($(this).attr("id"), this.checked, "0");
        }      
    });

    //Configura o evento de delecao de transacao quando a imagem de remover e clicada
    $(".remocaoLivro").click(function() {
        removerTransaco($(this).attr("id").substr($(this).attr("id").length - 1));
    });
});

//Metodo que implementa a chamada exucutada pelo picker de colocar em doacao
function doarLivro(trasacaoId, doarSimNao,qtdLivrosDoados){
    jQuery.ajax({
        url: '/alterar_livro',
        type: 'GET',
        contentType: 'application/json',
        data: {doacaoJson: JSON.stringify({tId:trasacaoId, doar:doarSimNao, qtd:qtdLivrosDoados})},   
        dataType: 'json',
        success: function(data){
            //implementar
        },
        fail: function() {
            alert( "error" );
        },
        always: function() {
            alert( "complete" );
        }
    });   
}

//Metodo que manda a transacao a ser removida para o servidor
function removerTransaco(trasacaoId){
    jQuery.ajax({
        url: '/alterar_livro',
        type: 'GET',
        contentType: 'application/json',
        data: {remocaoJson: JSON.stringify({tId:trasacaoId, remover:true})},   
        dataType: 'json',
        success: function(data){
            alert(data);
        },
        fail: function() {
            alert( "error" );
        },
        always: function() {
            alert( "complete" );
        }
    });
}
