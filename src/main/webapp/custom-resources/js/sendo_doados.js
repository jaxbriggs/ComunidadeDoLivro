var livroClicado = {transacaoId:null, livroData:null};
$(document).ready(function(){
    //Aplica o efeito de slider na div com as imagens dos livros sendo doados
    $('.livrosSlider').slick({
        lazyLoad: 'ondemand',
        slidesToShow: 5,
        slidesToScroll: 1,
        arrows: true,
        draggable: true,
        fade: false,
        focusOnSelect: false,
        infinite: true,
        swipe: true,
        swipeToSlide: true,
        accessibility: false,
    });
    
    //Configura o tooltip dos livros
    $('.livroListaItem').bind({
        mouseenter: function(e) {
            $('#'+$(this).attr("id")).tooltip('show');
        },
        mouseleave: function(e) {
            $('#'+$(this).attr("id")).tooltip('hide');
        }
   });
   
   //Configura a selecao de filtros
   /*
    * 1 - Genero (Padrao)
    * 2 - Titulo
    * 3 - Autor
    */
   $('.filtroOPtion').click(function(e){       
       $('#hiddenFiltro').val($(this).attr("id").substr($(this).attr("id").length - 1));
       $('.filtroOPtion').removeClass('active');
       $("#"+$(this).attr("id")).addClass("active");
   });
    
    
    //Configura o listener do botao pesquisar
    $('#btnPesquisarLivrosEmDoacao').bind('click', function(e){
        $('#sendoDoadosCarrousel').fadeOut('fast');
        
        //TODO Pesquisa
    });
    
    //Configura o listener do botao limpar resultados
    $('#btnLimparResultadosPesquisa').bind('click', function(e){
        $('#sendoDoadosPesquisa').empty();
        $('#txtPesquisaLivrosEmDoacao').val("");
        $('#sendoDoadosCarrousel').fadeIn('fast');
    });
    
    //Configura o listener do botao atualizar listas
    $('#atualizarListas').click(function(){
        location.reload();
    });
    
    //Configura evento que responde ao click na imagem da capa do livro 
    $(".livroListaItem").bind("click", function(){
       var livroId = $(this).attr("id");
       getClickedBookDetails(livroId);
    });
});

//FUNCOES
function getClickedBookDetails(transacaoId){
    getTransacaoById(transacaoId);
}

//Busca no servidor as informacoes da transacao pelo id
function getTransacaoById(transacaoId){
    if(livroClicado.transacaoId !== transacaoId){
        jQuery.ajax({
            url: '/transactions',
            type: 'GET',
            contentType: 'application/json',
            data: {getTransacaoById: JSON.stringify({transacaoId:transacaoId})},   
            dataType: 'json',
            success: function(data){
                livroClicado.transacaoId = transacaoId;
                livroClicado.livroData = data;
                buildDoacaoModal(livroClicado.livroData);
            },
            fail: function() {
                console.log( "error" ); //Implementar
            },
            always: function() {
                
            }
        });
    } else {
        buildDoacaoModal(livroClicado.livroData);
    }
}

function buildDoacaoModal(livroData){

    console.log(livroData);

    var userId = $("#userId").val();
    
    var modal = 
        "<div class=\"modal fade\" id=\"doacaoInfoModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\">" +
          "<div class=\"modal-dialog\" role=\"document\">" +
            "<div class=\"modal-content\">" +
              "<div class=\"modal-header\">" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>" +
                "<h4 class=\"modal-title\" id=\"myModalLabel\">Informações da doação</h4>" +
              "</div>" +
              "<div class=\"modal-body\">" +
                  "<div class=\"container-fluid\">" +
                      "<div class=\"row\">" +
                          "<div class=\"col-xs-3\">" +
                            "<a class=\"thumbnail\">";

                                modal += 
                                livroData[0].livro.capaLink === "" ? 
                                    "<img src=\"https://docs.google.com/uc?id=0B03XPxH14xDTZS05WTNCNTJPZW8&export=download\" alt=\"...\">" : 
                                    "<img src=\""+livroData[0].livro.capaLink+"\" alt=\"...\">";

                            modal +=     
                            "</a>" +
                          "</div>" +
                          "<div class=\"col-xs-9\">" +
                            "<p style=\"font-size: medium; text-align: center;\"><b>"+livroData[0].livro.titulo+" ("+livroData[0].livro.isbn+")</b></p>"
                            "<p><b style=\"font-size: small;\"> Por</b>: "+livroData[0].livro.autor;

                            if(livroData[0].livro.qtdPaginas > 0){
                              modal +=
                              "<b style=\"font-size: small;\"> Nº de Páginas:</b> "+livroData[0].livro.qtdPaginas;
                            }

                            modal +=
                            "<b style=\"font-size: small;\"> Idioma:</b> "+livroData[0].livro.idioma+"</p>";
                            
                            if(livroData[0].livro.descricao.length >= 175){
                              modal +=
                              "<p>"+livroData[0].livro.descricao.substr(0, 175)+"...</p>";
                            } else {
                              modal +=
                              "<p>"+livroData[0].livro.descricao+"</p>";
                            }

                            modal += 
                            "<p>" +
                              "<span class=\"label label-danger\">"+livroData[0].livro.genero+"</span> " +
                              "<span class=\"label label-warning\">Quantidade: "+livroData[0].qtLivroTransacao+"</span>" +
                            "</p>" +
                          "</div>" +
                      "</div>" +
                      "<div class=\"row\">" +
                          "<div class=\"col-xs-12\">" +
                              "<div class=\"row\">" +
                                  "<div class=\"col-xs-12\">" +
                                      "<p style=\"float: left;\"><b style=\"font-size: medium;\">Doador: </b><a>"+livroData[0].doador.name+"</a></p>" +
                                      "<p style=\"float: right;\"><b style=\"font-size: medium;\">Nº de Candidatos: </b>"+livroData[0].candidatosIds.length+"</p>" + //Implementar
                                  "</div>" +
                              "</div>" +
                          "</div>" +
                      "</div>" +
                  "</div>" +
              "</div>" +
              "<div class=\"modal-footer\">" +
                "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Fechar</button>"; 

                if($.inArray(parseInt(userId), livroData[0].candidatosIds) !== -1){
                  modal +=
                    "<button id=\"btnViewTransacao\" type=\"button\" class=\"btn btn-success\">Ir para transacao</button>";
                } else {
                  if(userId !== "" && userId != livroData[0].cadastrante.id && userId != livroData[0].doador.id){
                    modal +=
                    "<button id=\"btnCandidatar\" type=\"button\" class=\"btn btn-primary\">Quero este livro</button>";
                  } else {
                    modal +=
                    "<button id=\"btnCandidatar\" type=\"button\" class=\"btn btn-primary\" disabled>Quero este livro</button>";
                  }
                }

              modal +=  
              "</div>" +
            "</div>" +
          "</div>" +
        "</div>";

    $("#modalDoacaoContainer").empty();
    $("#modalDoacaoContainer").append(modal);
    
    $("#modalDoacaoContainer").find($('#doacaoInfoModal')).modal('toggle');

    $("#modalDoacaoContainer").find($('#btnCandidatar')).click(function(e){

      var cdTrans = livroData[0].cdTransacao;
      
      var operation = 0; //Candidatar

      var qtdLivrosCandidatar = prompt("Quantos volumes deste livro você deseja?", livroData[0].qtLivroTransacao);

      if(qtdLivrosCandidatar !== null){
        while(qtdLivrosCandidatar !== null && (qtdLivrosCandidatar <= 0 || qtdLivrosCandidatar > livroData[0].qtLivroTransacao || qtdLivrosCandidatar === "")){
          alert("Quantidade inválida! Por favor forneça uma quantidade coerente.");
          var qtdLivrosCandidatar = prompt("Quantos volumes deste livro você deseja?", livroData[0].qtLivroTransacao);
        }

        if(qtdLivrosCandidatar !== null){
            //Faz a requisicao para se candidatar ao livro
            jQuery.ajax({
              url: '/eleicao_transacao',
              type: 'GET',
              contentType: 'application/json',
              data: {eleicaoJson: JSON.stringify({tId:cdTrans, userId:userId, qtd:qtdLivrosCandidatar, electOp:operation})},   
              dataType: 'json',
              success: function(data){
                  livroClicado.transacaoId = null;
                  $("#modalDoacaoContainer").find($('#doacaoInfoModal')).modal('toggle');
              },
              fail: function() {
                  //implementar
                  alert( "error" );
              },
              always: function(data) {
              }
          });
        }
      }});
} 


