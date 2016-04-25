<%@ page import="model.User" %>
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
  
  //Pega os livros em doacao para exibicao 
  ArrayList<ArrayList<TransacaoDAO.GenericTransacao>> booksPerGenre = null;
  try{
      
    TransacaoDAO tDao = new TransacaoDAO();
    booksPerGenre = tDao.getAllTransacoesAtivasIDsPicsGenre();
      
  } catch(URISyntaxException ex){
    booksPerGenre = null;
    ex.printStackTrace();
  } catch(SQLException ex){
    booksPerGenre = null;  
    ex.printStackTrace();
  } catch(Exception ex) {
    booksPerGenre = null;  
    ex.printStackTrace();  
  }
%>

<link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
<link rel="stylesheet" type="text/css" href="../slick/slick.css">
<link rel="stylesheet" type="text/css" href="../slick/slick-theme.css">
<link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">
<style>
    /*Slider de livros sendo doados*/
    /*Estilizaca das setas de transicao*/
    .slick-prev:before, .slick-next:before { 
        color:black !important;
    }
    .slick-prev:before
    {
        content: '<';
        font-size: 30px;
        font-weight: bold;
    }
    [dir='rtl'] .slick-prev:before
    {
        content: '>';
        font-size: 30px;
        font-weight: bold;
    }
    .slick-next:before
    {
        content: '>';
        font-size: 30px;
        font-weight: bold;
    }
    [dir='rtl'] .slick-next:before
    {
        content: '<';
        font-size: 30px;
        font-weight: bold;
    }
</style>
    <!-- MODAL DOACAO INFO -->
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
    <!-- FIM MODAL DOACAO INFO -->
    
    <!-- MENSAGEM BEM VINDO -->
    <div class="container-fluid" style="margin-bottom: 20%;">
        <div class="row">
            <%if(user == null){%>
                <div style="font-weight: bold; font-size: medium; padding: 1%;">
                    Seja bem-vindo, Visitante.
                </div>
            <%} else {%>
                <div style="font-weight: bold; font-size: medium; padding: 1%;">
                    Seja bem-vindo, <%= user.getName() %>.
                </div>
            <%}%>
        </div>
        <!-- FIM MENSAGEM BEM VINDO -->
        
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
                            <a class="navbar-brand"><span><i class="fa fa-shopping-basket"></i></span> Livros em Doação</a>
                        </div>

                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul class="nav navbar-nav" style="float: right;">
                                <form class="navbar-form navbar-right" role="search">
                                    <div class="form-group">
                                      <!-- campo hidden que contem o filtro da pesquisa -->
                                      <input type="hidden" id="hiddenFiltro" value="1">
                                      <input type="text" id="txtPesquisaLivrosEmDoacao" class="form-control" placeholder="Pesquisa">
                                    </div>
                                    <button type="button" id="btnPesquisarLivrosEmDoacao" class="btn btn-default">Pesquisar</button>
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
                 <h2>Livros Disponíveis Para Doação</h2>
            </div>
            
            <!-- DIV DE PESQUISA -->
            <div class="col-xs-12" id="sendoDoadosPesquisa" style="padding: 0; margin: 0 auto;">
            </div>
            <!-- FIM DIV DE PESQUISA -->
            
            <div class="col-xs-12" id="sendoDoadosCarrousel" style="padding: 0; margin: 0;">
                <div class="row">
                    <div class="col-xs-offset-10 col-xs-2" style="padding-bottom: 2%;">
                        <a id="atualizarListas" title="Atualizar listas"><i class="fa fa-refresh fa-3x" aria-hidden="true"></i></a>
                    </div>
                </div>
                <!-- SLICK CARROUSSEL -->
                <%if(!(booksPerGenre.isEmpty() || booksPerGenre == null)){
                    for(ArrayList<TransacaoDAO.GenericTransacao> gl : booksPerGenre){%>                    
                        <div class="panel panel-primary" style="margin: 0 auto; width: 80%; height: 38%;">
                            <div class="panel-heading">
                                <h3 class="panel-title"><%= gl.get(0).getGenre() %></h3>
                            </div>  
                            <div class="panel-body">
                                <div class="livrosSlider" style="text-align: center; margin: 0 auto; width: 70%; height: 70%;">
                                    <%for(TransacaoDAO.GenericTransacao t : gl){%>
                                        <div>
                                            <a class="livroListaItem" id='<%= t.getCdTransacao() %>' data-toggle="tooltip" data-placement="top" title="<%= t.getTitulo() %>">
                                                <img data-lazy="<%= t.getImgLink() %>" style="display: inline;"  height="202" width="148">
                                            </a>
                                        </div>
                                    <%}%>
                                </div>
                            </div>  
                        </div>
                    <% 
                        out.println("<br><br>");
                    }
                }%>
                <!-- FIM SLICK CARROUSSEL -->
            </div>
        </div>
    </div>
<script src="jquery/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="../slick/slick.min.js"></script>
<script src="../custom-resources/js/sendo_doados.js"></script>    
<script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>

