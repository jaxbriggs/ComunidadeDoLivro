<%
  User user = null;
  if(session.getAttribute("user") != null){
    user = ((User)session.getAttribute("user"));
  }
%>
<meta charset="ISO-8859-1"/>
<form>
    <input type="hidden" id="userId" value="<%= user != null ? user.getId() : "" %>">
</form>
<script src="jquery/jquery-1.12.1.min.js"></script>
<script src="custom-resources/js/jquery.redirect.js"></script>
<script src="custom-resources/js/index-script.js"></script>
<script src="custom-resources/js/ajax/requests.js"></script>
<script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        
<!-- ======================================================================================== -->
<!-- ================================= MODALS =============================================== -->
    <!-- ERRO LOGIN -->
    <div class="modal fade" id="myModal" role="dialog">
      <div class="modal-dialog modal-sm">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Usuário não encontrado</h4>
          </div>
          <div class="modal-body">
            <p>Verfique seu login e/ou senha</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
          </div>
        </div>

      </div>
    </div>
    
    <!--LOGIN-->
    <div class="modal fade" id="modal_login" tabindex="-1" role="dialog" aria-labelledby="Login">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="exampleModalLabel">Login</h4>
            </div>
            <div class="modal-body">
                <form id="signInForm"> 
                    <fieldset class="form-group">
                      <label for="login">Login</label>
                      <input type="text" class="form-control" id="login" name="login" placeholder="Digite o login" required/>
                    </fieldset>
                    <fieldset class="form-group">
                      <label for="password">Senha</label>
                      <input type="password" class="form-control" id="password" name="password" placeholder="Senha" required/>
                    </fieldset>
                    <p style="text-align: center;"><button id="btnEntrar" type="submit" class="btn btn-primary">Entrar</button></p>
                </form>
            </div>            
          </div>
        </div>
      </div>
    
    <!--CADASTRO-->
    <div class="modal fade" id="modal_cadastro" tabindex="-1" role="dialog" aria-labelledby="Cadastro">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="exampleModalLabel">Cadastro</h4>
            </div>
            <div class="modal-body">
                <form id="signUpForm" method="post" action="${pageContext.request.contextPath}/cadastrarUsuario"/>
                    <legend>Cadastro Básico</legend>
                     <fieldset class="form-group">
                        <label for="login">Eu sou uma: </label>
                        <div class="radio-inline">
                            <label>
                              <input type="radio" name="pessoa-inst" id="pessoaRadio" name="pessoaRadio" value="pessoa" checked/>
                              Pessoa Física
                            </label>
                        </div>
                        <div class="radio-inline">
                          <label>
                            <input type="radio" name="pessoa-inst" id="optionsRadios2" name="optionsRadios2" value="inst"/>
                            Instituição
                          </label>
                        </div>
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="name">Nome <span style="color: red">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Digite o nome" required checked="checked"/>
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="userEmail">Email <span style="color: red">*</span></label>
                        <input type="email" class="form-control" id="userEmail" name="userEmail" placeholder="Digite o email" required/>
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="userLogin">Login de usuário <span style="color: red">*</span></label>
                      <input type="text" class="form-control" id="userLogin" name="userLogin" placeholder="Digite o login" required/>
                    </fieldset>
                    <fieldset class="form-group">
                      <label for="signInPassword">Senha <span style="color: red">*</span></label>
                      <input type="password" class="form-control" id="signInPassword" name="signInPassword" placeholder="Senha" required/>
                    </fieldset>
                    <fieldset class="form-group">
                      <label for="signInPassword2">Confirmação da senha <span style="color: red">*</span></label>
                      <input type="password" class="form-control" id="signInPassword2" name="signInPassword2" placeholder="Senha" required/>
                    </fieldset>
                    <legend>Cadastro Avançado</legend>
                    <fieldset class="form-group">
                        <label id="lblCpfOrCnpj" for="cpfOrCnpj"></label>
                      <input type="text" class="form-control" id="cpfOrCnpj" name="cpfOrCnpj" placeholder="" />
                    </fieldset>
                    <fieldset class="form-group col-xs-6" style="padding: 0;">
                        <label for="tel">Telefone</label>
                      <input type="text" class="form-control" id="tel" name="tel" placeholder="Digite o telefone" />
                    </fieldset>
                    <fieldset class="form-group col-xs-6" style="padding: 0 0 0 2%;">
                        <label for="cel">Celular</label>
                      <input type="text" class="form-control" id="cel" name="cel" placeholder="Digite o celular"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-12" style="padding: 0;">
                        <label for="cep">CEP</label>
                      <input type="text" class="form-control" id="cep" name="cep" placeholder="Digite o cep"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-9" style="padding: 0;">
                        <label for="rua">Rua</label>
                        <input type="text" class="form-control" id="rua" name="rua" size="60"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-3" style="padding: 0 2%;">
                        <label for="numero">Número</label>
                        <input type="number" class="form-control" id="numero" name="numero" size="60"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-12" style="padding: 0;">
                        <label for="bairro">Bairro</label>
                        <input type="text" class="form-control" id="bairro" name="bairro" size="40"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-10" style="padding: 0;">
                        <label for="cidade">Cidade</label>
                        <input type="text" class="form-control" id="cidade" name="cidade" size="40"/>
                    </fieldset>
                    <fieldset class="form-group col-xs-2">
                        <label for="uf">Estado</label>
                        <input type="text" class="form-control" id="uf" name="uf" size="2"/>
                    </fieldset>
                    <p class="col-xs-12" style="text-align: center;"><button type="submit" class="btn btn-primary">Cadastrar</button></p>
                </form>
            </div>            
          </div>
        </div>
      </div>
<!-- ======================================================================================== -->

<nav class="navbar navbar-default no-margin">
    <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header fixed-brand">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"  id="menu-toggle">
              <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
            </button>
            <table id="header" border="0">
                <tr>
                    <td align="center" style="width: 35%;"><a><img src="../custom-resources/img/logo.png" width="55" height="55"/></a></td>
                    <td align="left" style="width: 65%; padding-left:1%;"><h1 id="title">Comunidade do Livro</h1></td>
                </tr>
            </table>
        </div>
    
        <!--<div class="navbar-header fixed-brand">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"  id="menu-toggle">
              <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span>
            </button>
            <a class="navbar-brand" href="index.jsp"><i class="fa fa-book fa-4"></i> COMUNIDADE DO LIVRO</a>
        </div><!-- navbar-header-->

        <div class="collapse navbar-collapse pull-left" id="bs-example-navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li class="active" ><button class="navbar-toggle collapse in" data-toggle="collapse" id="menu-toggle-2"> <span class="glyphicon glyphicon-th-large" aria-hidden="true"></span></button></li>
                    </ul>
        </div><!-- bs-example-navbar-collapse-1 -->
        
        
        
        <%if(session.getAttribute("user") == null){%>
            <div class="collapse navbar-collapse pull-right">
                <ul class="nav navbar-nav">
                    <li><button id="login_btn" type="button" class="btn btn-default navbar-btn">Login</button></li>
                </ul>            
            </div>

            <div class="collapse navbar-collapse pull-right">
                <ul class="nav navbar-nav">
                    <li><button id="cadastro_btn" type="button" class="btn btn-default navbar-btn">Cadastro</button></li>
                </ul>            
            </div>
        <%} else {%>
            <div class="collapse navbar-collapse pull-right">
                <ul class="nav navbar-nav">
                    <li><a id="imgUserConfigs" role="button" data-toggle="popover" title="<%= user.getName() %>" data-html="true" data-placement="bottom" tabindex="0" data-trigger="focus"><img src="../custom-resources/img/user.png" width="40px" height="40px"/></a></li>
                    <li><button id="sair_btn" onClick="setPick('sair'); submitPickSenderForm();" type="button" class="btn btn-default navbar-btn">Sair</button></li>
                </ul>            
            </div>
        <%}%>
</nav>
<%if(session.getAttribute("user") != null){%>
<div id="wrapper">
    <!-- Sidebar -->
    <div id="sidebar-wrapper">
        <form id="menuPickSenderForm">
            <input type="hidden" name="pick" id="pick" value=""/>
        </form>     
            <ul class="sidebar-nav nav-pills nav-stacked" id="menu">
                <li id="menu_livros">
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-book fa-stack-1x "></i></span> Livros</a>
                       <ul class="nav-pills nav-stacked" style="list-style-type:none;">
                            <li id="menu_meus_livros" onClick="loadPageByPickedMenu('#menu_meus_livros', '/meus_livros');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-archive fa-stack-1x "></i></span>Meus</a></li>
                            <li id="menu_livros_em_doacao" onClick="loadPageByPickedMenu('#menu_livros_em_doacao', '/sendo_doados');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-shopping-basket fa-stack-1x "></i></span>Sendo Doados</a></li>
                            <li id="menu_livros_em_pedidos" onClick="loadPageByPickedMenu('#menu_livros_em_pedidos', '/sendo_pedidos');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-child fa-stack-1x "></i></span>Sendo Pedidos</a></li>
                        </ul>
                </li>
                <li id="transacoes" onClick="loadPageByPickedMenu('#meus_recebimentos', '/meus_recebimentos');">
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-exchange fa-stack-1x "></i></span> Transações</a>
                </li>
                <!--
                <li>
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-exchange fa-stack-1x "></i></span> Transações</a>
                       <ul class="nav-pills nav-stacked" style="list-style-type:none;">
                           <li id="minhas_doacoes" onClick="loadPageByPickedMenu('#minhas_doacoes', '/minhas_doacoes');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-hand-paper-o fa-stack-1x "></i></span>Minhas Doações</a></li>
                            <li id="meus_recebimentos" onClick="loadPageByPickedMenu('#meus_recebimentos', '/meus_recebimentos');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-hand-grab-o fa-stack-1x "></i></span>Meus Recebimentos</a></li>
                        </ul>
                </li>
                -->
                <li>
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-child fa-stack-1x "></i></span> Pedidos</a>
                       <ul class="nav-pills nav-stacked" style="list-style-type:none;">
                           <li id="meus_pedidos" onClick="loadPageByPickedMenu('#meus_pedidos', '/meus_pedidos');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-user fa-stack-1x "></i></span>Meus</a></li>
                            <li id="pedidos_de_outros" onClick="loadPageByPickedMenu('#pedidos_de_outros', '/pedidos_de_outros');"><a><span class="fa-stack fa-lg pull-left"><i class="fa fa-users fa-stack-1x "></i></span>De Outros</a></li>
                        </ul>
                </li>
                <li id="usuarios" onClick="loadPageByPickedMenu('#usuarios', '/usuarios');">
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-users fa-stack-1x "></i></span> Usuários</a>
                </li>
                <li id="configuracoes" onClick="loadPageByPickedMenu('#configuracoes', '/configuracoes');">
                    <a><span class="fa-stack fa-lg pull-left"><i class="fa fa-cog fa-stack-1x "></i></span> Configurações</a>
                </li>
                <!--<li>
                    <a onClick="setPick('sair'); submitPickSenderForm();"><span class="fa-stack fa-lg pull-left"><i class="fa fa-sign-out fa-stack-1x "></i></span> Sair</a>
                </li>-->
            </ul>

    </div><!-- /#sidebar -->
<%}%>
