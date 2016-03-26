var resetNext = false;

//campos do formulario
var publicacao;
var titulo;
var autor;
var editora;
var descricao;
var genero;
var idioma;
var paginas;
var capa;

//Disabilita e habilita campos do form
function disableLivroFormFields(habDisab){
    publicacao.prop('disabled', habDisab);
    titulo.prop('disabled', habDisab);
    autor.prop('disabled', habDisab);
    editora.prop('disabled', habDisab);
    descricao.prop('disabled', habDisab);
    genero.prop('disabled', habDisab);
    idioma.prop('disabled', habDisab);
    paginas.prop('disabled', habDisab);
    $("#capaPicker").prop('disabled', habDisab);
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imgCapa').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

$(document).ready(function() {
    
    //campos do formulario
    publicacao = $('#publicacao');
    titulo = $('#titulo');
    autor = $('#autor');
    editora = $('#editora');
    descricao = $('#descricao');
    genero = $('#genero');
    idioma = $('#idioma');
    paginas = $('#qtdPaginas');
    capa = $("#capa");
    
    //Configura o click do botao cadastrar livro para submeter o form de cadastro
    $('#cadastrarNovoLivro').click(function(){
         $("#novo_livro_form").submit();
     });
    
    //Abre o modal para cadastrar livro
    $('#btnNovoLivro').click(function(){
        $('#modal_novo_livro').modal('toggle') 
     });
     
     //Faz a consulta na API do google
     $('#isbn').on('input',function(e){
        if($("#isbn").val().length >= 4){
            getLivrosByISBN($("#isbn").val());
        }    
    });
    
    $("#capaPicker").change(function(){
        readURL(this);
    });
    
    //Reseta informacoes do modal quando cancelado
    $('.modal').on('hidden.bs.modal', function(){
        $(this).find('form')[0].reset();
        $('#imgCapa').attr('src', '');
        disableLivroFormFields(false);
    });
    
    //Manda a requisicao para o servlet que cadastra o livro
    $("#novo_livro_form").submit(function( event ) {
        
        event.preventDefault();
        
        disableLivroFormFields(false);
        
        //Servico que grava a imagem no Drive
        var postingDrive = $.post('/drive', $("#novo_livro_form").serialize());
        
        var posting = $.post('/cadastrar_livro', $("#novo_livro_form").serialize());
        
        posting.done(function( data ) {
            if(data !== null){
                disableLivroFormFields(true);
            } else {
                $('#myModal').modal('toggle');
            }
        });
        
    });
    
     /*$(function() {
        $( "#publicacao" ).datepicker();
    });*/
});

//Funcao que pega os dados retonados da api
function setLivroDataByISBN(data){
    if(resetNext){
        publicacao.val('');
        titulo.val('');
        autor.val('');
        editora.val('');
        descricao.val('');
        genero.val('');
        idioma.val('');
        paginas.val('');
        
        disableLivroFormFields(false);
        
        $('#imgCapa').attr('src', '');
        resetNext = false;
    }
    
    if(!$.isEmptyObject(data)){
        resetNext = true;
    }
    
    //Data de publicacao
    if(jQuery.type(data.items[0].volumeInfo.publishedDate) === "undefined"){
        publicacao.val("");
    } else {
        publicacao.val(data.items[0].volumeInfo.publishedDate);
    }    
    
    //Titulo
    if($.type(data.items[0].volumeInfo.title) === "undefined"){
        titulo.val("");
    } else {
        titulo.val(data.items[0].volumeInfo.title);
    }
    
    //Autor
    if($.type(data.items[0].volumeInfo.authors[0]) === "undefined"){
        autor.val("");
    } else {
        autor.val(data.items[0].volumeInfo.authors[0]);
    }
    
    //Editora
    if($.type(data.items[0].volumeInfo.publisher) === "undefined"){
        editora.val("");
    } else {
        editora.val(data.items[0].volumeInfo.publisher);
    }
    
    //Descricao
    if($.type(data.items[0].volumeInfo.description) === "undefined"){
        descricao.val("");
    } else {
        descricao.val(data.items[0].volumeInfo.description);
    }
    
    //Genero
    if($.type(data.items[0].volumeInfo.categories[0]) === "undefined"){
        genero.val("");
    } else {
        genero.val(data.items[0].volumeInfo.categories[0]);
    }
    
    //Idioma
    if($.type(data.items[0].volumeInfo.language) === "undefined"){
        idioma.val("");
    } else {
        idioma.val(data.items[0].volumeInfo.language);
    }
    
    //Paginas
    if($.type(data.items[0].volumeInfo.pageCount) === "undefined"){
        paginas.val("");
    } else {
        paginas.val(data.items[0].volumeInfo.pageCount);
    }
    
    //capa
    if($.type(data.items[0].volumeInfo.imageLinks.thumbnail) === "undefined"){
        capa.val("");
        $('#imgCapa').attr('src', "")
    } else {
        capa.val(data.items[0].volumeInfo.imageLinks.thumbnail);
        $('#imgCapa').attr('src', data.items[0].volumeInfo.imageLinks.thumbnail)
    }
    
    disableLivroFormFields(true);
}

//Faz requisicao do livro por isbn
function getLivrosByISBN(texto){
    var getting = $.get('/api_livros', {isbn : texto});

    getting.done(function( data ) {
        if(data !== null){
            //primeiraConsulta = false;
            setLivroDataByISBN(data);
        }
    })
};
