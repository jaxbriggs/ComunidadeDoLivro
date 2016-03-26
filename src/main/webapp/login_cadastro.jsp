<%if(session.getAttribute("user") == null){%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
        <title>Comunidade do Livro</title>
    </head>
    
    <body style="background-color: #D5F2D6">
        <!--Loading resources -->
        <script src="jquery/jquery-1.12.1.min.js"></script>
        <script src="custom-resources/js/jquery.redirect.js"></script>
        <script src="custom-resources/js/index-script.js"></script>
        <script src="custom-resources/js/ajax/requests.js"></script>
        <script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <div class = "container" style = "background-color: white">
            
            <!-- Trigger the modal with a button -->
            <!--<button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Modal</button>-->

            <!-- Modal -->
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
            
            <div class = "row" style="margin: 0 -30px;">
                <div class = "col-xs-12" >
                    <div class="col-sm-3" style="padding-top: 2%; padding-left: 0;"><a href="index.jsp"><img src="custom-resources/img/logo.png" width="200" height="200"/></a></div>
                    <div class="col-sm-9" style="text-align: center; padding-top: 5%;"><h1 id="title">Comunidade do Livro</h1></div>
                </div>
            </div>
                
            <!--LOGIN-FORM-->    
            <div class = "row" style="padding-top: 3%; margin: 0 -30px;">
                <div class = "col-xs-6 col-xs-offset-3" >
                   <div class="panel panel-default">
                       <div class="panel-heading" id="signInUpFormTitle"></div>
                       <div class="panel-body">
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
                    <div class="panel-footer"><a id="signInOrUp" href="#"></a></div>
                   </div>
                </div>
            </div>
            <%@include file="page_components/footer.html" %>
        </div>
    </body>
</html>
<%} else {
    response.sendRedirect("/index.jsp");
}%>
