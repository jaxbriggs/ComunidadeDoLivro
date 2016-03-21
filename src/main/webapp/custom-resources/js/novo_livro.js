var resetNext = false;

$(document).ready(function() {
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
    
    //Reseta informacoes do modal quando cancelado
    $('.modal').on('hidden.bs.modal', function(){
        $(this).find('form')[0].reset();
        $('#imgCapa').attr('src', '');
    });
    
    $("#novo_livro_form").submit(function( event ) {
        event.preventDefault();
        
        var posting = $.post('/cadastrar_livro', $("#signInForm").serialize());

        posting.done(function( data ) {
            if(data !== null){
                $.redirect("/home.jsp", data);
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
    
    var publicacao = $('#publicacao');
    var titulo = $('#titulo');
    var autor = $('#autor');
    var editora = $('#editora');
    var descricao = $('#descricao');
    var genero = $('#genero');
    var idioma = $('#idioma');
    var paginas = $('#qdtPaginas');
    var capa = $('#capa');
    
    if(resetNext){
        publicacao.val('');
        titulo.val('');
        autor.val('');
        editora.val('');
        descricao.val('');
        genero.val('');
        idioma.val('');
        paginas.val('');
        $('#imgCapa').attr('src', '');
        resetNext = false;
    }
    
    if(!$.isEmptyObject(data)){
        resetNext = true;
    }
    
    publicacao.val(data.items[0].volumeInfo.publishedDate);
    titulo.val(data.items[0].volumeInfo.title);
    autor.val(data.items[0].volumeInfo.authors[0]);
    editora.val(data.items[0].volumeInfo.publisher);
    descricao.val(data.items[0].volumeInfo.description);
    genero.val(data.items[0].volumeInfo.categories[0]);
    idioma.val(data.items[0].volumeInfo.language);
    paginas.val(data.items[0].volumeInfo.pageCount);
    $('#imgCapa').attr('src', data.items[0].volumeInfo.imageLinks.thumbnail)
}

//Faz requisicao do livro por isbn
function getLivrosByISBN(texto){
    var getting = $.get('/api_livros', {isbn : texto});

    getting.done(function( data ) {
        if(data !== null){
            primeiraConsulta = false;
            setLivroDataByISBN(data);
        }
    })
};

//Funcao que cadastra livro


