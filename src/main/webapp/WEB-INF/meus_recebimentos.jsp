<%@ page import="model.User" %>
<%@ page import="model.Transacao" %>
<%@ page import="dao.TransacaoDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%
  User user = null;
  if(session.getAttribute("user") != null){
    user = ((User)session.getAttribute("user"));
  }
  
  //Pega as transacoes abertas do usuario
  List<Transacao> userTransactions = null;
  if(user != null){
    try{
      TransacaoDAO tDao = new TransacaoDAO();
      userTransactions = tDao.getUserTransacoes(user.getId());
    } catch(URISyntaxException ex){
      userTransactions = null;
      ex.printStackTrace();
    } catch(SQLException ex){
      userTransactions = null;  
      ex.printStackTrace();
    } catch(Exception ex) {
      userTransactions = null;  
      ex.printStackTrace();  
    }
  }
 
%>

<link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
<link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">

    <!-- MODAL DOACAO INFO -->
    <div id="modalDoacaoContainer">
        <!--
        <div class="modal fade" id="doacaoInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Informações da doação</h4>
              </div>
              <div class="modal-body">
                  <div class="container-fluid">
                      <div class="row">
                          <div class="col-xs-3">
                            <a class="thumbnail">
                                <img src="../custom-resources/img/teste/content (1).jpg" alt="...">
                            </a>
                          </div>
                          <div class="col-xs-9">
                            <p style="font-size: medium; text-align: center;"><b>Titulo BLA BLA (456465465465)</b></p>
                            <p><b style="font-size: small;">Por</b>: Jack Bauer <b style="font-size: small;">Nº de Páginas:</b> 227 <b style="font-size: small;">Idioma:</b> en</p>
                            <p>Este livro tem como objetivo, mostrar como a morte se instalou no mundo, a partir de uma situação criada pelo homem, que gerou graves conseqüências para toda humanidade, bem como, para universo físico. Procuro também, mostrar as Este...</p>
                            <p>
                              <span class="label label-danger">Fiction</span>
                              <span class="label label-warning">Quantidade: 1</span>
                            </p>
                          </div>
                      </div>
                      <div class="row">
                          <div class="col-xs-12">
                              <div class="row">
                                  <div class="col-xs-12">
                                      <p style="float: left;"><b style="font-size: medium;">Doador: </b><a>Carlos Henrique</a></p>
                                      <p style="float: right;"><b style="font-size: medium;">Nº de Candidatos: </b>30</p>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
                <button type="button" class="btn btn-primary">Candidatar-se</button>
              </div>
            </div>
          </div>
        </div>
        -->
    </div>
    <!-- FIM MODAL DOACAO INFO -->
    
   
    <div class="container-fluid" style="margin-bottom: 20%;">     
        <div class="row">
            <div class="col-xs-12" style="padding: 0;">
                <!-- MENU SUPERIOR DE CADASTRO E CONSULTA DE LIVROS -->                  
                <nav class="navbar navbar-default">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <a class="navbar-brand"><span><i class="fa fa-exchange"></i></span> Minhas Transações</a>
                        </div>

                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul class="nav navbar-nav" style="float: right;">
                                <form class="navbar-form navbar-right" role="search">
                                    <div class="form-group">
                                      <!-- campo hidden que contem o filtro da pesquisa -->
                                      <input type="hidden" id="hiddenFiltro" value="1">
                                      <input type="hidden" id="userId" value="<%= user != null ? user.getId() : "" %>">
                                      <input type="text" id="txtPesquisaLivrosEmDoacao" class="form-control" placeholder="Pesquisa">
                                    </div>
                                    <button type="button" id="btnPesquisarTransacoes" class="btn btn-default">Pesquisar</button>
                                    <button type="button" id="btnLimparResultadosPesquisa" class="btn btn-default">Limpar Resultados</button>
                                </form>
                                <li><a>Filtrar por: </a></li>
                                <li class="filtroOPtion active" id="filtro1"><a>Gênero</a></li>
                                <li class="filtroOPtion" id="filtro2"><a>Título</a></li>
                                <li class="filtroOPtion" id="filtro3"><a>Autor</a></li>
                            </ul>
                        </div>
                    </nav>
                </div>
                <!-- FIM MENU SUPERIOR -->
        </div>
        <div class="row">
            <div class="col-xs-12" style=" text-align: center; margin-bottom: 3%;">
                 <h2>Minhas Transações</h2>
            </div>
            <!-- DIV DE PESQUISA -->
            <div class="col-xs-12" id="transacoesPesquisa" style="padding: 0; margin: 0 auto;">
            </div>
            <!-- FIM DIV DE PESQUISA -->
            
            <div class="col-xs-offset-1 col-xs-10" id="transacoesConteudo" style="padding: 0;">
                <%
                    if(userTransactions != null){
                        for(Transacao t : userTransactions){%>
                        <div class="row" style="padding-top: 2%;">
                            <div class="col-xs-2 visible-lg" style="text-align: right;">
                                <a class="img-thumbnail"><img id="imgCapa" src="<%= t.getLivro().getCapaLink() %>" height="211px" width="128px"></a>
                            </div>
                            <div class="col-xs-10" style="align-items: center;">
                                <div style="">
                                    <h4><b class="livroTitulo"><%= t.getLivro().getTitulo() %> (<%= t.getLivro().getIsbn() %>)</b></h4>
                                    <p><b>Por:</b> <%= t.getLivro().getAutor() %> <b>Nº de Páginas:</b> <%= t.getLivro().getQtdPaginas() %> <b>Idioma:</b> <%= t.getLivro().getIdioma() %></p>
                                </div>
                                <div style="text-align: justify;"><%= t.getLivro().getDescricao().length() > 339 ? 
                                                                        t.getLivro().getDescricao().substring(0, 339) + "..." :
                                                                        t.getLivro().getDescricao()%></div>
                                <div>
                                    <p id="" style="float: bottom; padding-top: 1%;">
                                        <span style=""><b>Doador: </b> <%= t.getDoador().getName() %></span>
                                        <span style="padding-left: 5%;"><b>Donatário: </b> <%= t.getDonatario() != null ? t.getDonatario().getName() : "?" %></span>
                                    </p>
                                    <p id="" style="float: bottom;">
                                        <span style=""><b>Iniciada em: </b> <%= t.getDataCadastro() %></span>
                                        <span style="padding-left: 5%;"><b>Finalizada em: </b> <%= t.getDataFinalizacao() == null ? "?" : t.getDataFinalizacao() %></span>
                                    </p>
                                    <!--<div style="float:right; margin-top:-3%;" class="row">
                                        <div class="col-xs-6">
                                            <a data-original-title="Remover" title="" data-toggle="remover" class="remocaoLivro" id="rem452">
                                                <img src="../custom-resources/img/delete_img.png" height="32px" width="32px">
                                            </a>
                                        </div>
                                        <div class="col-xs-6" style="margin-top: -22px;">
                                            <input name="452" id="452" class="toggle" type="checkbox">
                                            <label title="" data-original-title="" for="452" class="toggleLabel"></label>
                                        </div>
                                    </div>-->
                                </div>
                                <div style="padding-top: 0.5%;">
                                    <button type="button" class="btn btn-danger">Cancelar</button>
                                    
                                    <%//if(){%>    
                                        <button type="button" class="btn btn-primary">Confirmar Recebimento</button>
                                    <%//}%>
                                    
                                    <%//if(){%>  
                                        <button type="button" class="btn btn-success">Comentarios</button>
                                    <%//}%>
                                    
                                    <%if(t.getDataFinalizacao() != null){%>    
                                        <button type="button" class="btn btn-warning">Reabrir</button>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                <% }
                }
                %>
            </div>
        </div>
    </div>
<script src="jquery/jquery-1.12.1.min.js"></script>
<script src="../custom-resources/js/transacoes.js"></script>    
<script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>

