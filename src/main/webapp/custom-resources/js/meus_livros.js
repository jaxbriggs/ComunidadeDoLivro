//Global scope
var qtdPerPage = 5; //Quantidade de livros por pagina
var pageNumber = 1;

$(document).ready (function(){

    //Pega todas as transacoes do usuario e monta a lista dinamicamente
    getAllTransacoesByUserWithLimit($("#userId").val(), qtdPerPage, 1, $("#filtroId").val());

    //Esconde a mensagem de livro cadastrado com sucesso
    $("#alertCadastroSucesso").hide(); 

    //Configura a paginacao
    paginate();
});

//Metodo que implementa a chamada exucutada pelo picker de colocar em doacao
function doarLivro(trasacaoId, doarSimNao, qtdLivrosDoados){
    jQuery.ajax({
        url: '/alterar_livro',
        type: 'GET',
        contentType: 'application/json',
        data: {doacaoJson: JSON.stringify({tId:trasacaoId, doar:doarSimNao, qtd:qtdLivrosDoados})},   
        dataType: 'json',
        success: function(data){
            getTransacaoById(data.transacaoId, 4);
        },
        fail: function() {
            //implementar
            alert( "error" );
        },
        always: function(data) {
            console.log(data);
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
            if(data.success === true){
                //Implementar
                buildBooksList("book"+data.transacaoId, 3);
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

function getAllTransacoesByUserWithLimit(userId, limit, operation, filtro){

    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getAllTransacoesByUserWithIndex: JSON.stringify({userId:userId, booksLimit:limit, filtro:filtro})},   
        dataType: 'json',
        success: function(data){
            if(!(data.resultado === "nada")){
                $("#alertSemLivros").hide();
                buildBooksList(data, operation);
            } else {
                //Limpa o container paginado
                $("#meusLivrosPaginator").empty();

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

function getTransacaoById(transacaoId, operation){
    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getTransacaoById: JSON.stringify({transacaoId:transacaoId})},   
        dataType: 'json',
        success: function(data){
            if(!(data.resultado === "nada")){
                buildBooksList(data, operation);
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

function getNextTransacoes(rowNum, limit, userId, operation, filtroId){
    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getNextTransacoes: JSON.stringify({rowNum:rowNum, booksLimit:limit, userId:userId, filtroId:filtroId})},   
        dataType: 'json',
        success: function(data){
            if(!(data.resultado === "nada") && !jQuery.isEmptyObject(data)){
                buildBooksList(data, operation);
            } else {
                $(window).unbind('scroll');
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

function getTransacoesByBook(bookText, userId, booksLimit, operation){
    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getTransacoesByBook: JSON.stringify({bookText:bookText, userId:userId, booksLimit:booksLimit})},   
        dataType: 'json',
        success: function(data){
            if(!(data.resultado === "nada")){
                if(!(data.length === 0)){
                    $('#alertConsultaSemResultados').hide();
                    buildBooksList(data, operation);    
                } else {
                    $('#alertConsultaSemResultados').show();
                }                
            } else {
                $('#alertConsultaSemResultados').show();
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

//Possible values for operation 
//1 - Build
//2 - Add
//3 - Remove
//4 - Edit
//5 - Anexar
//6 - Build Pesquisa
function buildBooksList(data, operation){
    var numPerPage = qtdPerPage;
    var html = "";

    if(typeof data === "object"){
        for(i = 0; i < data.length; i++){

        if(data[i].livro.capaLink === ""){
            data[i].livro.capaLink = "https://docs.google.com/uc?id=0B03XPxH14xDTZS05WTNCNTJPZW8&export=download";
        }

        //INICIO DA ROW DO LIVRO
        html +=
        "<div class=\"livroRow row\" id=\"book"+data[i].cdTransacao+"\" style=\"padding-top:10px;\">" + 
                "<div class=\"col-xs-2 visible-lg\" style=\"text-align: right;\">" +
                    "<a class=\"img-thumbnail\">" +
                        "<img id=\"imgCapa\" src=\""+data[i].livro.capaLink+"\" height=\"211px\" width=\"128px\" />" +
                    "</a>" +
                "</div>" +
                "<div class=\"col-xs-10\" style=\"align-items: center;\">" +
                    "<div style=\"\">" +
                        "<h4><b class=\"livroTitulo\">"+data[i].livro.titulo;
                        
                        if(data[i].livro.isbn !== ""){
                            html +=
                            " (" + data[i].livro.isbn + ")";
                        }

                        html +=
                        "</b></h4>" +
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
                        "<p id=\"tags"+data[i].cdTransacao+"\" style=\"float: bottom; padding-top: 2%;\">";

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
                                "<span class=\"label label-warning qtdTag\" id=\"qtd"+data[i].cdTransacao+"\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Clique para editar\">Quantidade: "+data[i].qtLivroTransacao+"</span>\n" + 
                                "<span class=\"label label-success\">Disponível Para Doação</span>\n";
                            }

                        html +=  
                        "<div style=\"float:right; margin-top:-3%;\" class=\"row\">" + 
                            "<div class=\"col-xs-6\">" +
                                "<a title='Remover' data-toggle=\"remover\" class=\"remocaoLivro\" id=\"rem"+data[i].cdTransacao+"\"><img height=\"32px\" width=\"32px\" src=\"../custom-resources/img/delete_img.png\"/></a>" +
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
            "</div>";
        }

        var phtml = $.parseHTML(html);
    }

    if(operation === 1){ //BUIILD
        paginate();

        $("#meusLivrosPaginator").empty();
        $("#meusLivrosPaginator").append(phtml);

        //Configura o campo de texto do meuno superior para executar consultar ao ser modificado
        if($("#meusLivrosPaginator").children().length !== 0){
            $("#meusLivrosSearchTxt").unbind("input");
            $("#meusLivrosSearchTxt").bind("input", function() {
                consultarMeusLivros($(this).val());
            });

            $("#drop-filtro > li > a").unbind('click');
            //Configura o filtro
            $("#drop-filtro > li > a").click(function(){
                //Configura a paginacao
                paginate();

                switch($(this).text()) {
                    case "A..Z":
                        $("#filtroId").val(1);
                        break;
                    case "Z..A":
                        $("#filtroId").val(2);
                        break;
                    case "Recentes..Antigos":
                        $("#filtroId").val(3);
                        break;
                    case "Antigos..Recentes":
                        $("#filtroId").val(4);
                        break;
                    case "Em doação":
                        $("#filtroId").val(5);
                        break;
                    case "Em transação":
                        $("#filtroId").val(6);
                        break;
                    default:
                        $("#filtroId").val(3);
                }

                getAllTransacoesByUserWithLimit($("#userId").val(), qtdPerPage, 1, $("#filtroId").val());
            });
        }

    } else if(operation === 2){ //ADD

        $("#alertSemLivros").hide();
        $("#meusLivrosPaginator").prepend(phtml);

    } else if(operation === 3){ //REMOVE

        $("#meusLivrosPaginator").children().remove('#'+data);

        if($("#meusLivrosSearchTxt").val() !== ""){
            if($("#meusLivrosPaginator").children().length === 0){
                $("#meusLivrosSearchTxt").val("");
                consultarMeusLivros("");
            }
        } else {
            if($("#meusLivrosPaginator").children().length < qtdPerPage){
                $("#meusLivrosSearchTxt").val("");
                consultarMeusLivros("");
            }
        }

        if($("#meusLivrosPaginator").children().length === 0){
            $("#meusLivrosSearchTxt").unbind("input");
            $("#drop-filtro > li > a").unbind('click');
        }

    } else if(operation === 4){ //EDIT

        $( '#book'+data[0].cdTransacao ).replaceWith( phtml );

    } else if(operation === 5){ //ANEXAR

        $("#meusLivrosPaginator").append(phtml);

    } else if(operation === 6){ //BUILD PESQUISA

        //Remove infinite scroll
        $(window).unbind('scroll');

        $("#meusLivrosPaginator").append(phtml);
    }


    //Configura o tootip do remover livro
    $("#meusLivrosPaginator").find($('[data-toggle="remover"]')).tooltip();

    //Tooltip para o checkbox de doacao
    $("#meusLivrosPaginator").find($("label[class='toggleLabel']")).tooltip({'trigger':'hover', 'title': "Ligar/Desligar Doação"});

    $("#meusLivrosPaginator").find($("input[class='toggle']")).unbind( "change" );

    //Configura o evento de doacao quando o checkbox de doacao e alterado
    $("#meusLivrosPaginator").find($("input[class='toggle']")).change(function() {
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

  $('[data-toggle="tooltip"]').tooltip();
  $('[data-toggle="tooltip"]').click(function(){
    $("#meusLivrosPaginator").find($("#"+this.id.replace("qtd", ""))).change();
    $("#meusLivrosPaginator").find($("input[class='toggle']")).unbind( "change" );
  });
} 

function consultarMeusLivros(text){
    if(text.length > 0){
        if(text.length % 4 === 0){
            $("#meusLivrosPaginator").empty();
            //Faz a consulta de livros e monta o resultado da consulta dinamicamente
            getTransacoesByBook(text, $("#userId").val(), 50, 6);
        }
    } else {
        paginate();

        $("#meusLivrosPaginator").empty();
        $('#alertConsultaSemResultados').hide();
        getAllTransacoesByUserWithLimit($("#userId").val(), qtdPerPage, 2, $("#filtroId").val());
    }
}

function paginate(){
    $(window).unbind('scroll');

    pageNumber = 1;

    //Configura a paginacao
    $(window).scroll(function (event) {
        if(Math.ceil($(window).scrollTop() + $(window).height()) == $(document).height()) {
           getNextTransacoes(qtdPerPage*pageNumber++, qtdPerPage, $("#userId").val(), 5, $("#filtroId").val());
        }
    });
}

