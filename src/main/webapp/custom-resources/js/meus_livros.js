//Global scope
var maxPerPage = 12; //Qtd MAXima de livros por pagina
var minPerPage = 2; //Qtd MINima de livros por pagina

$(document).ready (function(){

    //Pega todas as transacoes do usuario e monta a lista dinamicamente
    getAllTransacoesByUserWithLimit($("#userId").val(), $("#comboQtdPaginacao").val());

    //Configura o select para fazer o select ao trocar a quantidade de itens por pagina
    $("#comboQtdPaginacao").bind("change", function(e) {
        getAllTransacoesByUserWithLimit($("#userId").val(), $("#comboQtdPaginacao").val());
    });

    //Configura o campo de texto do meuno superior para executar consultar ao ser modificado
    $("#meusLivrosSearchTxt").bind("input", function() {
        consultarMeusLivros($(this).val());
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
            //implementar
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
            console.log(data.success === true);
            if(data.success === true){
                getAllTransacoesByUserWithLimit($("#userId").val(), $("#comboQtdPaginacao").val());
            } else {
                //Implementar erro
            }
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
        data: {getAllTransacoesByUserWithIndex: JSON.stringify({userId:userId})},   
        dataType: 'json',
        success: function(data){
            console.log("success");

            if(!(data.resultado === "nada")){
                $("#alertSemLivros").hide();
                buildBooksList(data);

                var qtdPaginas = Math.ceil(data.length/$("#comboQtdPaginacao").val());
                
                if(data.length > minPerPage){ 
                    $("#comboQtdPaginacao").show();
                    $("#porPagLabel").show();
                } else {
                    $("#comboQtdPaginacao").hide();
                    $("#porPagLabel").hide();
                }

                $("#painelMeusLivrosPaginator").paginate({
                    count                   : qtdPaginas, 
                    start                   : 1,
                    display                 : 10,
                    border                  : true,
                    border_color            : 'gray',
                    text_color              : 'white',
                    background_color        : 'gray',    
                    border_hover_color      : 'gray',
                    text_hover_color        : 'gray',
                    background_hover_color  : 'white', 
                    rotate                  : false,
                    images                  : false,
                    mouse                   : 'press',
                    onChange                : function(page){
                                                var results = 0;
                                                for(i = 1; i <= page; i++){
                                                    results += $("#meusLivrosPaginator").find("#p"+i)[0].childElementCount
                                                }
                                                $("#meusLivrosPaginator").find("p")[0].innerHTML = "Resultados: <b>" + results + "</b> de  <b>" + data.length; + "</b>";
                                                $('._current','#meusLivrosPaginator').removeClass('_current').hide();
                                                $('#p'+page).addClass('_current').show();
                                              }
                });
            } else {
                //Esconde as ocoes de paginacao e limpa o container paginado
                $("#comboQtdPaginacao").hide();
                $("#porPagLabel").hide();
                $("#meusLivrosPaginator").get(0).innerHTML = "";

                //Exibe a mensagem de nenhum livro cadastrado
                $("#alertSemLivros").show();
            }            
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
    var contagem = 0;
    var id = 1;
    var numPerPage = parseInt($("#comboQtdPaginacao").val());
    var divMaster = "";
    var html = "";
    var results = 0;

    var emptyPainel = $("#painelMeusLivrosPaginator");
    
    $("#meusLivrosPaginator").get(0).innerHTML = "<p class=\"col-xs-offset-5 col-xs-10\"></p><div id=\"painelMeusLivrosPaginator\" class=\"col-xs-offset-5 col-xs-10\"></div>";

    for(i = 0; i < data.length; i++){

        divMaster = "";            

        if(contagem === 0 || contagem % numPerPage === 0){
            if(contagem === 0){
                if(Math.ceil(data.length/$("#comboQtdPaginacao").val()) === 1){
                    results = data.length;
                } else {
                    results = $("#comboQtdPaginacao").val()
                }

                if(!(typeof $("#meusLivrosPaginator").find("p")[0] === "undefined")){
                    $("#meusLivrosPaginator").find("p")[0].innerHTML = "Resultados: <b>" + results + "</b> de  <b>" + data.length; + "</b>";
                }

                divMaster = "<div id=\"p" + (id++) + "\" class=\"bookCont _current\" style=\"\">";
            } else {
                results = id;
                divMaster = "<div id=\"p" + (id++) + "\" class=\"bookCont\" style=\"display:none;\">";
            }
        }
    

        html += divMaster;

        if(data[i].livro.capaLink === ""){
            data[i].livro.capaLink = "https://docs.google.com/uc?id=0B03XPxH14xDTZS05WTNCNTJPZW8&export=download"
        }

        //INICIO DA ROW DO LIVRO
        html +=
        "<div class=\"livroRow row\" id=\""+(data[i].livro.titulo + " " + data[i].livro.isbn + " " + data[i].livro.autor)+"\" style=\"padding-top:10px;\">" + 
                "<div class=\"col-xs-2 visible-lg\" style=\"text-align: right;\">" +
                    "<a class=\"img-thumbnail\">" +
                        "<img id=\"imgCapa\" src=\""+data[i].livro.capaLink+"\" height=\"211px\" width=\"128px\" />" +
                    "</a>" +
                "</div>" +
                "<div class=\"col-xs-10\" style=\"align-items: center;\">" +
                    "<div style=\"\">" +
                        "<h4><b class=\"livroTitulo\">"+data[i].livro.titulo+" (" + data[i].livro.isbn + ")</b></h4>" +
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
                        "<p style=\"float: bottom; padding-top: 2%;\">";

                            //Verificacao de labels
                            if(data[i].livro.genero !== ""){
                                 html += 
                                 "<span class=\"label label-danger\">"+data[i].livro.genero+"</span>\n";
                            }

                            if(data[i].isAutorizada){
                                html += 
                                "<span class=\"label label-primary\">Em Transação</span>\n";

                                /*
                                "<span class=\"label label-default\">Default</span>\n" +
                                "<span class=\"label label-primary\">Primary</span>\n" +
                                "<span class=\"label label-success\">Success</span>\n" +
                                "<span class=\"label label-info\">Info</span>\n" +
                                "<span class=\"label label-warning\">Warning</span>\n" +
                                "<span class=\"label label-danger\">Danger</span>\n"; 
                                */
                            }

                            if(data[i].isAtivada){
                                html += 
                                "<span class=\"label label-warning\">Quantidade: "+data[i].qtLivroTransacao+"</span>\n" + 
                                "<span class=\"label label-success\">Disponível Para Doação</span>\n";
                            }

                        html +=  
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

function consultarMeusLivros(text){
    if(text.length > 0){
        $("#meusLivrosPaginator").hide();
        $("#comboQtdPaginacao").hide();
        $("#porPagLabel").hide();

        //Faz a consulta na div de livros e monta o resultado da consulta dinamicamente
        var livros = $("#meusLivrosPaginator").find(".livroRow");
        var booksToBeReturned = []; //Array de objetos

        for(i = 0; i < livros.length; i++) {
            if(livros[i].id.toLowerCase().match(new RegExp(text)) !== null){
                booksToBeReturned.push(livros[i]);
            }
        }

        $("#pesquisaResultados").empty();
        if(!(booksToBeReturned.length === 0)){
            $('#alertConsultaSemResultados').hide();
            $(booksToBeReturned).clone().appendTo("#pesquisaResultados");
        } else {
            $('#alertConsultaSemResultados').show();
        }

    } else {
        $("#pesquisaResultados").empty();
        $("#meusLivrosPaginator").show();
        $("#comboQtdPaginacao").show();
        $("#porPagLabel").show();
        $('#alertConsultaSemResultados').hide();
    }
}  


