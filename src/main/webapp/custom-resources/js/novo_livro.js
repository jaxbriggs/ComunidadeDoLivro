var semCapa = "https://docs.google.com/uc?id=0B03XPxH14xDTZS05WTNCNTJPZW8&export=download";

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

//Disabilita e habilita TODOS os campos do form
function disableLivroFormFields(habDisab, exceptFields){
    
    if($.inArray(publicacao, exceptFields) === -1){
        publicacao.prop('disabled', habDisab);
    }
    
    if($.inArray(titulo, exceptFields) === -1){
        titulo.prop('disabled', habDisab);
    }
    
    if($.inArray(autor, exceptFields) === -1){
        autor.prop('disabled', habDisab);
    }
    
    if($.inArray(editora, exceptFields) === -1){
        editora.prop('disabled', habDisab);
    }
    
    if($.inArray(descricao, exceptFields) === -1){
        descricao.prop('disabled', habDisab);
    }
    
    if($.inArray(genero, exceptFields) === -1){
        genero.prop('disabled', habDisab);
    }
    
    if($.inArray(idioma, exceptFields) === -1){
        idioma.prop('disabled', habDisab);
    }
    
    if($.inArray(paginas, exceptFields) === -1){
        paginas.prop('disabled', habDisab);
    }
    
    if($.inArray(capa, exceptFields) === -1){
        $("#capaPicker").prop('disabled', habDisab);
    }
    
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
    
    $("#alertCadastroFalha").hide();
    
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
    $('#imgCapa').attr('src', semCapa);
    
    //Configura o click do botao cadastrar livro para submeter o form de cadastro
    $('#cadastrarNovoLivro').click(function(){
         $("#novo_livro_form").submit();
     });
    
    //Abre o modal para cadastrar livro
    $('#btnNovoLivro').click(function(){
        $('#modal_novo_livro').modal('toggle') 
     });     
    
     var patt = /(^\d{9}[\d|X]$)|(^\d{12}[\d|X]$)/g;
     //Faz a consulta na API do google     
    $('#isbn').on('input',function(){
        if($("#isbn").val().length >= 4 && patt.test($("#isbn").val())){
            getLivrosByISBN($("#isbn").val());
        } else {
            limpaCampos();
        }
    });
    
    //Altera a imagem da capa exibida ao escolher atraves do input
    $("#capaPicker").change(function(){
        readURL(this);
    });
    
    //Reseta informacoes do modal quando cancelado
    $('.modal').on('hidden.bs.modal', function(){
        $(this).find('form')[0].reset();
        $('#imgCapa').attr('src', semCapa);
        disableLivroFormFields(false, []);
    });
    
    //Manda a requisicao para o servlet que cadastra o livro
    $("#novo_livro_form").submit(function( event ) {
        event.preventDefault();
        
        disableLivroFormFields(false, []);
        
        alert($("#userId").val());
        
        //Servico que grava a imagem no Drive
        if($('#capaPicker').val() !== ""){
        
            var data = new FormData();
            data.append('capa', jQuery('#capaPicker')[0].files[0]);

            jQuery.ajax({
                url: '/drive',
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function(data){
                    if(!$.isEmptyObject(data)){
                        $('#capa').val(data.link);
                        var posting = $.post('/cadastrar_livro', $("#novo_livro_form").serialize());
                        posting.done(function( data ) {
                            if(data !== null && !$.isEmptyObject(data) && data.success === true){
                                //Sucesso ao cadastrar livro
                                //disableLivroFormFields(true, []);
                                $("#alertCadastroSucesso").alert();
                                $("#alertCadastroSucesso").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#alertCadastroSucesso").hide();
                                });
                                $('#modal_novo_livro').modal('toggle');
                            } else {
                                //Erro ao cadastrar livro
                                $("#alertCadastroFalha").show();
                            }
                        });
                    } else {
                        //Exibir erro ao gravar a imagem
                        $("#alertCadastroFalha").show();
                    }
                }
            });        
        } else {
            var posting = $.post('/cadastrar_livro', $("#novo_livro_form").serialize());
        
            posting.done(function( data ) {
                if(data !== null && !$.isEmptyObject(data) && data.success === true){
                    //Verifica o retorno da API sobre o retorno
                    //disableLivroFormFields(true, []);
                    $("#alertCadastroSucesso").alert();
                    $("#alertCadastroSucesso").fadeTo(2000, 500).slideUp(500, function(){
                        $("#alertCadastroSucesso").hide();
                    });
                    $('#modal_novo_livro').modal('toggle');
                } else {
                    $("#alertCadastroFalha").show();
                }
            });
        }
        
        /*var posting = $.post('/cadastrar_livro', $("#novo_livro_form").serialize());
        
        posting.done(function( data ) {
            if(data !== null){
                //Verifica o retorno da API sobre o retorno
                disableLivroFormFields(true, []);
            } else {
                $('#myModal').modal('toggle');
            }
        });*/
        
    });
});

function limpaCampos(){
    publicacao.val('');
    titulo.val('');
    autor.val('');
    editora.val('');
    descricao.val('');
    genero.val('');
    idioma.val('');
    paginas.val('');

    disableLivroFormFields(false, []);
    
    $('#imgCapa').attr('src', semCapa);
    $('#capaPicker').val("");
    $('#capa').val("")
}

//Funcao que pega os dados retonados da api
function setLivroDataByISBN(data){
    
    var missingManadatoryFields = [];
    
    limpaCampos();
       
    //Data de publicacao
    if(jQuery.type(data.items[0].volumeInfo.publishedDate) === "undefined"){
        missingManadatoryFields.push(publicacao);
        publicacao.val("");
    } else {
        publicacao.val(data.items[0].volumeInfo.publishedDate);
    }    

    //Titulo
    if($.type(data.items[0].volumeInfo.title) === "undefined"){
        missingManadatoryFields.push(titulo);
        titulo.val("");
    } else {
        titulo.val(data.items[0].volumeInfo.title);
    }

    //Autor
    if($.type(data.items[0].volumeInfo.authors) === "undefined"){
        missingManadatoryFields.push(autor);
        autor.val("");
    } else {
        autor.val(data.items[0].volumeInfo.authors[0]);
    }

    //Editora
    if($.type(data.items[0].volumeInfo.publisher) === "undefined"){
        missingManadatoryFields.push(editora);
        editora.val("");
    } else {
        editora.val(data.items[0].volumeInfo.publisher);
    }

    //Descricao
    if($.type(data.items[0].volumeInfo.description) === "undefined"){
        missingManadatoryFields.push(descricao);
        descricao.val("");
    } else {
        descricao.val(data.items[0].volumeInfo.description);
    }

    //Genero
    if($.type(data.items[0].volumeInfo.categories) === "undefined"){
        missingManadatoryFields.push(genero);
        genero.val("");
    } else {
        genero.val(data.items[0].volumeInfo.categories[0]);
    }

    //Idioma
    if($.type(data.items[0].volumeInfo.language) === "undefined"){
        missingManadatoryFields.push(idioma);
        idioma.val("");
    } else {
        idioma.val(data.items[0].volumeInfo.language);
    }

    //Paginas
    if($.type(data.items[0].volumeInfo.pageCount) === "undefined"){
        missingManadatoryFields.push(paginas);
        paginas.val("");
    } else {
        paginas.val(data.items[0].volumeInfo.pageCount);
    }
    
    //capa
    if($.type(data.items[0].volumeInfo.imageLinks) === "undefined"){
        missingManadatoryFields.push(capa);
        capa.val("");
        $('#imgCapa').attr('src', semCapa)
    } else {
        capa.val(data.items[0].volumeInfo.imageLinks.thumbnail);
        $('#imgCapa').attr('src', data.items[0].volumeInfo.imageLinks.thumbnail)
    }
    
    disableLivroFormFields(true, missingManadatoryFields);
}

//Funcao que pega os dados retonados do servidor
function setLivroDataByISBNFromServer(data){
    
    limpaCampos();
    
    publicacao.val(data.dataPublicacao);
    
    titulo.val(data.titulo);
   
    autor.val(data.autor);

    editora.val(data.editora);

    descricao.val(data.descricao);

    genero.val(data.genero);

    idioma.val(data.idioma);

    paginas.val(data.qtdPaginas);

    capa.val(data.capaLink);
    
    if(data.capaLink !== ""){ 
        $('#imgCapa').attr('src', data.capaLink)
    } else {
        $('#imgCapa').attr('src', semCapa)
    }
    
    disableLivroFormFields(true, []);
}

//Faz requisicao do livro por isbn
function getLivrosByISBN(texto){
    
    /*var str = "The best things in life are free";
      var patt = new RegExp("(^\d{9}[\d|X]$)|(^\d{12}[\d|X]$)");
      var res = patt.test(str);*/
    
    var gettingFromServer = $.get('/busca_livro', {isbn : texto});
    
    gettingFromServer.done(function( data ) {
        if(!$.isEmptyObject(data)){
            setLivroDataByISBNFromServer(data);
        } else {
            var getting = $.get('/api_livros', {isbn : texto});
            getting.done(function( data ) {
                if(!$.isEmptyObject(data)){
                    setLivroDataByISBN(data);
                } else {
                    disableLivroFormFields(false, []);
                    limpaCampos();
                }
            });
        }
    });

    
};
