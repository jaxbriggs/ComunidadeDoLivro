<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="model.User" %>
<%@ page import="model.Livro" %>
<%@ page import="model.Transacao" %>
<%@ page import="dao.TransacaoDAO" %>

<%
    List<Transacao> transacoes = new ArrayList<Transacao>();
    
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }
    
    if(session.getAttribute("user") != null){
%>

<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@page contentType="text/html; charset=ISO-8859-1" %>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="../jquery/jquery-ui-1.11.4/themes/smoothness/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="../custom-resources/css/general.css">
        <link rel="stylesheet" type="text/css" href="../custom-resources/css/toggle.css">
        <script src="jquery/jquery-1.12.1.min.js"></script>
        <script src="../jquery/jquery-ui-1.11.4/jquery-ui.min.js"></script>
        <script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <script src="../custom-resources/js/ajax/requests.js"></script>
        <script src="../custom-resources/js/novo_livro.js"></script>
        <script src="../custom-resources/js/meus_livros.js"></script>        
    </head>

    <body>
        <!--MODAL NOVO LIVRO-->
        <div class="modal fade" id="modal_novo_livro" tabindex="-1" role="dialog" aria-labelledby="modalNovoLivro">
            <div class="modal-dialog" role="document" style="margin-bottom: 5%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="exampleModalLabel">Cadastro de Livro</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin: 0 auto;">
                            <div class="col-xs-12">
                                <div class="alert alert-danger" role="alert" id="alertCadastroFalha">
                                    <strong>Aviso!</strong> Ocorreu um erro durante o cadastro do livro.
                                </div>
                            </div>
                        </div>
                        <form id="novo_livro_form" enctype="multipart/form-data" action="/drive" method="post">
                            <div class="row">  
                                <div class="form-group col-xs-6">
                                    <label for="isbn" class="control-label"><span style="color: red">*</span> ISBN:</label>
                                    <input type="text" class="form-control" id="isbn" name="isbn" required="true"  maxlength="13" pattern="(^\d{9}[\d|X]$)|(^\d{12}[\d|X]$)">
                                </div>
                                <div class='form-group col-xs-6'>
                                    <label for="publicacao" class="control-label">Data de Publicação:</label>                            
                                    <input type="text" class="form-control" id="publicacao" name="publicacao">
                                </div>
                            </div>
                            <div class='row'>
                                <div class="form-group col-xs-12">
                                    <label for="titulo" class="control-label"><span style="color: red">*</span> Título:</label>
                                    <span id="tituloWrapper"><input type="text" class="form-control" id="titulo" name="titulo" required></span>
                                </div>
                            </div>
                            <div class='row'>
                                <div class="form-group col-xs-6">
                                    <label for="autor" class="control-label"><span style="color: red">*</span> Autor:</label>
                                    <input type="text" class="form-control" id="autor" name="autor" required>
                                </div>
                                <div class="form-group col-xs-6">
                                    <label for="editora" class="control-label">Editora:</label>
                                    <input type="text" class="form-control" id="editora" name="editora">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <label for="descricao" class="control-label">Descrição:</label>
                                    <textarea class="form-control" id="descricao" name="descricao"></textarea>
                                </div>
                            </div>
                            <div class='row'>
                                <div class="form-group col-xs-5">
                                    <label for="genero" class="control-label"><span style="color: red">*</span> Gênero:</label>
                                    <input type="text" class="form-control" id="genero" name="genero" required>
                                </div>
                                <div class="form-group col-xs-5">
                                    <label for="idioma" class="control-label"><span style="color: red">*</span> Idioma:</label>
                                    <input type="text" class="form-control" id="idioma" name="idioma" required>
                                </div>
                                <div class="form-group col-xs-2">
                                    <label for="qdtPaginas" class="control-label">Páginas:</label>
                                    <input type="text" class="form-control" id="qtdPaginas" name="qtdPaginas">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-xs-3">
                                    <a class="thumbnail">
                                        <img id="imgCapa" src="" height="211px" width="128px" />
                                    </a>                            
                                </div>
                                <div class="form-group col-xs-9">
                                    <label for="capa" class="control-label">Imagem de Capa:</label>
                                    <input type="file" id="capaPicker" name="capaPicker"/>
                                    <p class="help-block">Selecione a foto da capa</p>
                                </div>
                            </div>
                            <!--campo hidden para enviar a capa-->
                            <input type="hidden" id="capa" name="capa" value="" />
                            <!--campo hidden para enviar o id do usuario-->
                            <input type="hidden" id="userId" name="userId" value="<%= ((User)session.getAttribute("user")) != null ? ((User)session.getAttribute("user")).getId() : null %>" />
                            <!--campo hidden para enviar o filtro-->
                            <input type="hidden" id="filtroId" name="filtroId" value="3" />
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" id="cadastrarNovoLivro">Cadastrar</button>
                    </div>
                </div>
            </div>
        </div>
        <!--FIM MODAL NOVO LIVRO-->

        <!-- MENU SUPERIOR DE CADASTRO E CONSULTA DE LIVROS -->                  
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand"><span><i class="fa fa-archive"></i></span> Meus Livros</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav col-xs-10">
                        <li class="col-xs-5"><a id="btnNovoLivro"><span><i class="fa fa-plus fa-lg"></i></span> Novo Livro </a></li>
                        <li class="col-xs-6" style="display: table; margin: 1% 0;">
                            <span style="width: 1%;" class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
                            <!-- Campo de texto para pesquisa em meus livros -->
                            <input type="text" id="meusLivrosSearchTxt" class="form-control" placeholder="Título, ISBN ou Autor">
                        </li>
                        <li class="dropdown col-xs-1" style="display: table; float: right;">
                            <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Filtro <span class="caret"></span></a>
                            <ul class="dropdown-menu" id="drop-filtro">
                              <li><a value="1">A..Z</a></li>
                              <li><a value="2">Z..A</a></li>
                              <li role="separator" class="divider"></li>
                              <li><a value="3">Recentes..Antigos</a></li>
                              <li><a value="4">Antigos..Recentes</a></li>
                              <li role="separator" class="divider"></li>
                              <li><a value="5">Em doação</a></li>
                              <li><a value="6">Em transação</a></li>
                            </ul>
                          </li>
                    </ul>                
                </div>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    <!-- FIM MENU SUPERIOR -->

    <div class="container-fluid"><!-- INICIO CONTAINER LIVROS -->
        <!-- ALERTS -->
        <div class="row">
            <div class="col-xs-12">
                <div class="alert alert-success" role="alert" id="alertCadastroSucesso" style="display: none;">
                    <strong>Aviso!</strong> O livro foi cadastrado com sucesso.
                </div>
                <div class="alert alert-info col-xs-offset-3 col-xs-6" role="alert" id="alertSemLivros" style="display: none; text-align: center;">
                    <strong>Sem livros!</strong> Não há livros cadastrados no momento.
                </div>
                <div class="alert alert-info col-xs-offset-3 col-xs-6" role="alert" id="alertConsultaSemResultados" style="display: none; text-align: center;">
                    <strong>Ops!</strong> A consulta não retornou nenhum resultado.
                </div>
            </div>
        </div>
        <!-- FIM ALERTS-->

        <!-- LISTA DE LIVROS CADASTRADOS -->
    <div class="row" style="margin-bottom: 10%;">
        <div class="col-xs-offset-1 col-xs-10">
            <div id="meusLivrosPaginator" >
                
            </div>
        </div>
    </div>
    <!-- FIM DA LISTA DE LIVROS CADASTRADOS -->
</div> <!-- FIM CONTAINER LIVROS -->
</body>
<%} else {
        response.sendRedirect("/index.jsp");
}%>
</html>
