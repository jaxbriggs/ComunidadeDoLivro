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
       console.log($('#hiddenFiltro').val());
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
    $('#doacaoInfoModal').modal('toggle');
}

//Busca no servidor as informacoes da transacao pelo id
function getTransacaoById(transacaoId){
    jQuery.ajax({
        url: '/transactions',
        type: 'GET',
        contentType: 'application/json',
        data: {getTransacaoById: JSON.stringify({transacaoId:transacaoId})},   
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
