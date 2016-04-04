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
var isbn;

/*==========================================================================*/
/*              Disabilita e habilita TODOS os campos do form               */
/*==========================================================================*/
function disableLivroFormFields(habDisab, exceptFields){
    
    if($.inArray(publicacao, exceptFields) === -1){
        publicacao.prop('disabled', habDisab);
    }
    
    if($.inArray(isbn, exceptFields) === -1){
        isbn.prop('disabled', habDisab);
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

//Exibe a imagem selecionada no elemento src
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
    isbn = $('#isbn');
    
    //Configura o click do botao cadastrar livro para submeter o form de cadastro
    $('#cadastrarNovoLivro').click(function(){
         $("#novo_livro_form").submit();
     });
    
    //Abre o modal para cadastrar livro
    $('#btnNovoLivro').click(function(){
        $('#modal_novo_livro').modal('toggle') 
     });     
    
    //Faz a consulta na API do google por ISBN
    var patt = /(^\d{9}[\d|X]$)|(^\d{12}[\d|X]$)/g;
    $('#isbn').on('input',function(){
        if($("#isbn").val().length >= 4 && patt.test($("#isbn").val())){
            getLivrosByISBN($("#isbn").val());
        } else {
            limpaCampos([isbn]);
        }
    });
    
    //Faz a consulta do livro baseado no titulo
    $('#titulo').on('input',function(){
        if($("#titulo").val().length % 5 === 0){
            getLivrosByTitulo($("#titulo").val());
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
        $("#alertCadastroFalha").hide();
    });
    
    //Manda a requisicao para o servlet que cadastra o livro
    $("#novo_livro_form").submit(function( event ) {
        event.preventDefault();
        
        disableLivroFormFields(false, []);
        
        //alert($("#userId").val());
        
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
                                $('#modal_novo_livro').modal('toggle');
                                $("#alertCadastroSucesso").alert();
                                $("#alertCadastroSucesso").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#alertCadastroSucesso").hide();
                                });
                            } else {
                                //Erro ao cadastrar livro
                                $("#alertCadastroFalha").show();
                                $("#alertCadastroFalha").fadeTo(2000, 500).slideUp(500, function(){
                                    $("#alertCadastroFalha").hide();
                                });
                            }
                        });
                    } else {
                        //Exibir erro ao gravar a imagem
                        $("#alertCadastroFalha").show();
                        $("#alertCadastroFalha").fadeTo(2000, 500).slideUp(500, function(){
                            $("#alertCadastroFalha").hide();
                        });
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
                    $("#alertCadastroFalha").fadeTo(2000, 500).slideUp(500, function(){
                        $("#alertCadastroFalha").hide();
                    });
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

function limpaCampos(exceptFields){
    publicacao.val('');
    if($.inArray(titulo, exceptFields) === -1){
        titulo.val('');
    }
    autor.val('');
    editora.val('');
    descricao.val('');
    genero.val('');
    idioma.val('');
    paginas.val('');
    if($.inArray(isbn, exceptFields) === -1){
        isbn.val('');
    }

    disableLivroFormFields(false, []);
    
    $('#imgCapa').attr('src', semCapa);
    $('#capaPicker').val("");
    $('#capa').val("");
}

/*==========================================================================*/
/*         Funcao que pega os dados retonados da API [ISBN]                 */
/*==========================================================================*/
function setLivroData(data){
    
    var missingManadatoryFields = [];
    
    missingManadatoryFields.push(isbn);
    
    limpaCampos([isbn]);
       
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
        autor.val(data.items[0].volumeInfo.authors.join(", "));
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
        genero.val(data.items[0].volumeInfo.categories.join(", "));
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

/*==========================================================================*/
//    Funcao que pega os dados de livro retonados do servidor [ISBN]
/*==========================================================================*/
function setLivroDataFromServer(data, callerElement){
    
    var exception = [];
    exception.push(callerElement);
    
    limpaCampos(exception);
    
    if(callerElement === titulo && $.type(data.dataPublicacao) === "undefined"){
        exception.push(publicacao);
    }
    
    publicacao.val(data.dataPublicacao);
    
    titulo.val(data.titulo);
    
    if(callerElement !== isbn){
        isbn.val(data.isbn);
    }
    
    if(callerElement === titulo && $.type(data.autor) === "undefined"){
        exception.push(autor);
    }
    
    autor.val(data.autor);

    if(callerElement === titulo && $.type(data.editora) === "undefined"){
        exception.push(editora);
    }
    
    editora.val(data.editora);
    
    if(callerElement === titulo && $.type(data.descricao) === "undefined"){
        exception.push(descricao);
    }
    
    descricao.val(data.descricao);
    
    if(callerElement === titulo && $.type(data.genero) === "undefined"){
        exception.push(genero);
    }
    
    genero.val(data.genero);
    
    if(callerElement === titulo && $.type(data.idioma) === "undefined"){
        exception.push(idioma);
    }
    
    idioma.val(data.idioma);
    
    if(callerElement === titulo && $.type(data.qtdPaginas) === "undefined"){
        exception.push(paginas);
    }
    
    paginas.val(data.qtdPaginas);
    
    if(callerElement === titulo && $.type(data.capaLink) === "undefined"){
        exception.push(capa);
    }
    
    capa.val(data.capaLink);
    
    if(data.capaLink !== ""){ 
        $('#imgCapa').attr('src', data.capaLink)
    } else {
        $('#imgCapa').attr('src', semCapa)
    }
    
    disableLivroFormFields(true, exception);
}

/*==========================================================================*/
//                  Faz requisicao do livro por [ISBN]
/*==========================================================================*/
function getLivrosByISBN(texto){
    
    /*var str = "The best things in life are free";
      var patt = new RegExp("(^\d{9}[\d|X]$)|(^\d{12}[\d|X]$)");
      var res = patt.test(str);*/
    
    var gettingFromServer = $.get('/busca_livro', {isbn : texto});
    
    gettingFromServer.done(function( data ) {
        if(!$.isEmptyObject(data)){
            setLivroDataFromServer(data, isbn);
        } else {
            var getting = $.get('/api_livros', {isbn : texto});
            getting.done(function( data ) {
                if(!$.isEmptyObject(data)){
                    setLivroData(data);
                } else {
                    disableLivroFormFields(false, []);
                    limpaCampos([isbn]);
                }
            });
        }
    });

    
};


//TITULO

/*==========================================================================*/
//                   Faz requisicao do livro [TITULO]
/*==========================================================================*/
var resetar = false;
function getLivrosByTitulo(texto){    
    
    var gettingFromServer = $.get('/busca_livro', {titulo : texto});
    
    gettingFromServer.done(function( data ) {
        if(!$.isEmptyObject(data)){
            var lista = data;
            
            $.each(data, function(index, value){
                //lista.push(data[index].titulo + " (" + data[index].isbn + ")");
                lista.push({label:data[index].titulo + " - " + data[index].autor, value:data[index]});
            });
            
            $( "#titulo" ).autocomplete({
                minLength: 1,
                source: lista,
                appendTo: "#tituloWrapper",
                select: function( event, ui ) {
                    event.preventDefault();
                    setLivroDataFromServer(ui.item.value, titulo);
                    resetar = true;
                }
            }).on("change", function () {
                $(this).autocomplete("search", "");
            }).on("input", function(){
                if(resetar){
                    limpaCampos([titulo]);
                    resetar = false;
                }
            });            
            
        } 
    });
};
