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
                <h4 class="modal-title" id="myModalLabel">Informa��es da doa��o</h4>
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
                            <p><b style="font-size: small;">Por</b>: Jack Bauer <b style="font-size: small;">N� de P�ginas:</b> 227 <b style="font-size: small;">Idioma:</b> en</p>
                            <p>Este livro tem como objetivo, mostrar como a morte se instalou no mundo, a partir de uma situa��o criada pelo homem, que gerou graves conseq��ncias para toda humanidade, bem como, para universo f�sico. Procuro tamb�m, mostrar as Este...</p>
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
                                      <p style="float: right;"><b style="font-size: medium;">N� de Candidatos: </b>30</p>
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
                            <a class="navbar-brand"><span><i class="fa fa-exchange"></i></span> Minhas Transa��es</a>
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
                                <li class="filtroOPtion active" id="filtro1"><a>G�nero</a></li>
                                <li class="filtroOPtion" id="filtro2"><a>T�tulo</a></li>
                                <li class="filtroOPtion" id="filtro3"><a>Autor</a></li>
                            </ul>
                        </div>
                    </nav>
                </div>
                <!-- FIM MENU SUPERIOR -->
        </div>
        <div class="row">
            <div class="col-xs-12" style=" text-align: center; margin-bottom: 3%;">
                 <h2>Minhas Transa��es</h2>
            </div>
            <!-- DIV DE PESQUISA -->
            <div class="col-xs-12" id="transacoesPesquisa" style="padding: 0; margin: 0 auto;">
            </div>
            <!-- FIM DIV DE PESQUISA -->
            
            <div class="col-xs-offset-1 col-xs-10" id="transacoesConteudo" style="padding: 0;">
                <%
                    if(userTransactions != null){
                        for(Transacao t : userTransactions){%>
                        <div class="row" style="padding-top: 2%;" id="myTrans<%= t.getCdTransacao() %>">
                            <form>
                                <input type="hidden" id="donatarioId<%= t.getCdTransacao() %>" value="<%= t.getDonatario().getId() %>">
                            </form>
                            <div class="col-xs-2 visible-lg" style="text-align: right;">
                                <a class="img-thumbnail"><img id="imgCapa" src="<%= t.getLivro().getCapaLink() %>" height="211px" width="128px"></a>
                            </div>
                            <div class="col-xs-10" style="align-items: center;">
                                <div style="">
                                    <h4><b class="livroTitulo"><%= t.getLivro().getTitulo() %> (<%= t.getLivro().getIsbn() %>)</b></h4>
                                    <p><b>Por:</b> <%= t.getLivro().getAutor() %> <b>N� de P�ginas:</b> <%= t.getLivro().getQtdPaginas() %> <b>Idioma:</b> <%= t.getLivro().getIdioma() %></p>
                                </div>
                                <div style="text-align: justify;"><%= t.getLivro().getDescricao().length() > 339 ? 
                                                                        t.getLivro().getDescricao().substring(0, 339) + "..." :
                                                                        t.getLivro().getDescricao()%></div>
                                <div>
                                    <p id="" style="float: bottom; padding-top: 1%;">
                                        <span style=""><b>Doador: </b> <%= t.getDoador().getName() %></span>
                                        <span style="padding-left: 5%;"><b>Donat�rio: </b> <%= t.getDonatario() != null ? t.getDonatario().getName() : "?" %></span>
                                    </p>
                                    <p id="" style="float: bottom;">
                                        <span style=""><b>Iniciada em: </b> <%= t.getDataCadastro() %></span>
                                        <span style="padding-left: 5%;"><b>Finalizada em: </b> <%= t.getDataFinalizacao() == null ? "?" : t.getDataFinalizacao() %></span>
                                        <span style="padding-left: 5%;"><b>Quantidade: </b> <%= t.getQtLivroTransacao() == null ? "?" : t.getQtLivroTransacao() %></span>
                                    </p>
                                </div>
                                <div style="padding-top: 0.5%;">
                                    <%if(t.getCadastrante().getId() != user.getId() && t.getIsAutorizada() && t.getDataFinalizacao() == null){%>    
                                        <button type="button" class="btn btn-danger cancelarTransacao" id="cancelarTransacao<%= t.getCdTransacao()%>">Cancelar</button>
                                    <%}%>
                                    
                                    <%if(t.getCadastrante().getId() == user.getId() && t.getDataFinalizacao() == null){%>    
                                        <button type="button" class="btn btn-default desistirTransacao" id="desistirTransacao<%= t.getCdTransacao()%>">Desistir</button>
                                    <%}%>
                                    
                                    <%if(t.getCadastrante().getId() == user.getId() && t.getIsAutorizada() && t.getDataFinalizacao() == null){%>    
                                        <button type="button" class="btn btn-primary confirmarRecebimento" id="confirmarRecebimento<%= t.getCdTransacao() %>">Confirmar Recebimento</button>
                                    <%}%>
                                    
                                    <%//if(t.getCadastrante().getId() != user.getId() && t.getIsAutorizada() && t.getDataFinalizacao() == null){%>    
                                        <!--<button type="button" class="btn btn-warning">Declarar Envio</button>-->
                                    <%//}%>
                                    
                                    <%if(t.getDataFinalizacao() != null && t.getCadastrante().getId() == user.getId()){%>    
                                        <button type="button" class="btn btn-warning reabrirTransacao" id="reabrirTransacao<%= t.getCdTransacao() %>">Reabrir</button>
                                    <%}%>
                                    
                                    <%if(t.getDataFinalizacao() != null && t.getCadastrante().getId() != user.getId()){%>    
                                        <button type="button" class="btn btn-warning" disabled="true">Transa��o Finalizada</button>
                                    <%}%>
                                    
                                    <%if(t.getIsAutorizada()){%>  
                                        <button type="button" class="btn btn-success">Comentarios</button>
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

