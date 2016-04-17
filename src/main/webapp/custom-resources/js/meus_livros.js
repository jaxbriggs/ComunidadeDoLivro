$(document).ready (function(){

    //Pega todas as transacoes do usuario e monta a lista dinamicamente
    getAllTransacoesByUserWithLimit($("#userId").val(), $("#comboQtdPaginacao").val());

    $("#comboQtdPaginacao").change(function(){
        getAllTransacoesByUserWithLimit($("#userId").val(), $("#comboQtdPaginacao").val());        
    });    

    //Esconde a mensagem de livro cadastrado com sucesso
    $("#alertCadastroSucesso").hide();
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
            console.log(data);
        },
        fail: function() {
            console.log( "error" );
        },
        always: function() {
            console.log( "complete" );
        }
    });
}

function getAllTransacoesByUserWithLimit(userId, limit){
    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getAllTransacoesByUserWithIndex: JSON.stringify({userId:userId, booksLimit:limit})},   
        dataType: 'json',
        success: function(data){
            console.log("success");
            var qtdPaginas = Math.ceil((data[data.length-1].qtdLivros)/$("#comboQtdPaginacao").val());
            $("#painelMeusLivrosPaginator").paginate({
                count                   : qtdPaginas, 
                start                   : 1,
                display                 : qtdPaginas,
                border                  : true,
                border_color            : '#BEF8B8',
                text_color              : '#68BA64',
                background_color        : '#E3F2E1',    
                border_hover_color      : '#68BA64',
                text_hover_color        : 'black',
                background_hover_color  : '#CAE6C6', 
                rotate                  : false,
                images                  : false,
                mouse                   : 'press',
                onChange                : function(page){
                                            $('._current','#meusLivrosPaginator').removeClass('_current').hide();
                                            $('#p'+page).addClass('_current').show();
                                          }
            });

            buildBooksList(data);
        },
        fail: function() {
            console.log( "error" );
        },
        always: function() {
            console.log( "complete" );
        }
    });
}

function buildBooksList(data){
    
        var contagem = 1;
        var id = 1;
        var numPerPage = 5;
        var divMaster = "";
        var html = "";

        $("#meusLivrosPaginator").innerHTML = "";

        for(i = 0; i < data.length-1; i++){
            divMaster = "";
            if(contagem % numPerPage == 0 || contagem == 1){
                if(contagem == 1){
                    divMaster = "<div id=\"p" + (id++) + "\" class=\"_current\" style=\"\">";
                } else {
                    divMaster = "<div id=\"p" + (id++) + "\" class=\"\" style=\"\">";
                }
            }
        

        html += divMaster;

        html +=
        "<div class=\"row\" style=\"padding-top:10px;\">" + 
                "<div class=\"col-xs-2 visible-lg\" style=\"text-align: right;\">" +
                    "<a class=\"img-thumbnail\">" +
                        "<img id=\"imgCapa\" src=\""+data[i].livro.capaLink+"\" height=\"211px\" width=\"128px\" />" +
                    "</a>" +
                "</div>" +
                "<div class=\"col-xs-10\" style=\"align-items: center;\">" +
                    "<div style=\"\">" +
                        "<h4><b>"+data[i].livro.titulo+" (" + data[i].livro.isbn + ")</b></h4>" +
                        "<p>";

                        if(!(data[i].livro.autor === "")){
                            html += "<b>Por:</b> " + data[i].livro.autor;
                        }

                        if(!(data[i].livro.qtdPaginas === 0)){
                            html += " <b>Nº de Páginas:</b> " + data[i].livro.qtdPaginas;
                        }

                        if(!(data[i].livro.idioma === 0)){
                            html += " <b>Idioma:</b> " + data[i].livro.idioma;
                        }

                         html +=   
                        "</p>" +
                    "</div>" +
                    "<div style=\"text-align: justify; padding-top: 2%;\">";

                    if(data[i].livro.descricao.length > 495){
                        html += data[i].livro.descricao.substring(0,494) + "...";
                    } else {
                        html += data[i].livro.descricao;
                    }

                    html +=   
                    "</div>" +
                    "<div>" +
                        "<p style=\"float: bottom; padding-top: 2%;\">" +
                            "<span class=\"label label-default\">Default</span>" +
                            "<span class=\"label label-primary\">Primary</span>" +
                            "<span class=\"label label-success\">Success</span>" +
                            "<span class=\"label label-info\">Info</span>" +
                            "<span class=\"label label-warning\">Warning</span>" +
                            "<span class=\"label label-danger\">Danger</span>" +
                        "<div style=\"float:right; margin-top:-3%;\" class=\"row\">" +
                            "<div class=\"col-xs-6\">" +
                                "<a href=\"#\" title='Remover' data-toggle=\"remover\" class=\"remocaoLivro\" id=\"rem"+data[i].cdTransacao+"\"><img height=\"32px\" width=\"32px\" src=\"../custom-resources/img/delete_img.png\"/></a>" +
                            "</div>" +
                            "<div class=\"col-xs-6\" style=\"margin-top: -22px;\">" +
                                "<input type=\"checkbox\" name=\""+data[i].cdTransacao+"\" id=\""+data[i].cdTransacao+"\" class=\"toggle\" ";
                                html += data[i].isAtivada ? "checked>" : ">";
                                html +=
                                "<label for=\""+data[i].cdTransacao+"\" class=\"toggleLabel\"></label>" +
                            "</div>" +
                        "</div>" +
                        "</p>" +
                    "</div>" +
                "</div>" +
                "<hr>" +
            "</div>";
            contagem++;
            if(contagem % numPerPage === 0){
                html += "</div>";
            }
    }
    var phtml = $.parseHTML(html);
    $("#meusLivrosPaginator").append(phtml);

    //Configura o tootip do remover livro
    $("#meusLivrosPaginator").find($('[data-toggle="remover"]')).tooltip();

    //Tooltip para o checkbox de doacao
    $("#meusLivrosPaginator").find($("label[class='toggleLabel']")).tooltip({'trigger':'hover', 'title': "Ligar/Desligar Doação"});

    //Configura o evento de doacao quando o checkbox de doacao e alterado
    $("#meusLivrosPaginator").find($("input[class='toggle']")).change(function(e) {
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
    $("#meusLivrosPaginator").find($(".remocaoLivro")).click(function() {
        removerTransaco($(this).attr("id").substr(3, $(this).attr("id").length - 1));
    });    
}   


