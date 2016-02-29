<%@ page import="database.Connection" %>
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
        <script src="custom-resources/js/index-script.js"></script>
        <script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <div class = "container" style = "background-color: white">
            <div class = "row" style="margin: 0 -30px;">
                <div class = "col-xs-12" >
                    <table style="border:0; width: 100%;">
                        <tr>
                            <td style="padding-top: 2%; padding-left: 5%;"><a href="index.jsp"><img src="custom-resources/img/logo.png" width="200" height="200"/></a></td>
                            <td style="text-align: center"><h1 id="title">Comunidade do Livro</h1></td>
                        </tr>
                    </table>
                </div>
            </div>
                
            <!--LOGIN-FORM-->    
            <div class = "row" style="padding-top: 3%; margin: 0 -30px;">
                <div class = "col-xs-6 col-md-offset-3" >
                   <div class="panel panel-default">
                       <div class="panel-heading" id="signInUpFormTitle"></div>
                      <div class="panel-body">
                        <form id="signInForm">
                            <fieldset class="form-group">
                              <label for="login">Login</label>
                              <input type="text" class="form-control" id="login" placeholder="Digite o login" required>
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="password">Senha</label>
                              <input type="password" class="form-control" id="password" placeholder="Senha" required>
                            </fieldset>
                            <p style="text-align: center;"><button type="submit" class="btn btn-primary">Entrar</button></p>
                        </form>
                        <form id="signUpForm">
                            <legend>Cadastro Básico</legend>
                             <fieldset class="form-group">
                                <label for="login">Eu sou uma: </label>
                                <div class="radio-inline">
                                    <label>
                                      <input type="radio" name="pessoa-inst" id="pessoaRadio" value="pessoa" checked>
                                      Pessoa física
                                    </label>
                                </div>
                                <div class="radio-inline">
                                  <label>
                                    <input type="radio" name="pessoa-inst" id="optionsRadios2" value="inst">
                                    Instituição
                                  </label>
                                </div>
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="name">Nome <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="name" placeholder="Digite o nome" required checked="checked">
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="userEmail">Email <span style="color: red">*</span></label>
                                <input type="email" class="form-control" id="userEmail" placeholder="Digite o email" required>
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="userLogin">Login de usuário <span style="color: red">*</span></label>
                              <input type="text" class="form-control" id="userLogin" placeholder="Digite o login" required>
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="signInPassword">Senha <span style="color: red">*</span></label>
                              <input type="password" class="form-control" id="signInPassword" placeholder="Senha" required>
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="signInPassword2">Confirmação da senha <span style="color: red">*</span></label>
                              <input type="password" class="form-control" id="signInPassword2" placeholder="Senha" required>
                            </fieldset>
                            <legend>Cadastro Avançado</legend>
                            <fieldset class="form-group">
                                <label id="lblCpfOrCnpj" for="cpfOrCnpj"></label>
                              <input type="text" class="form-control" id="cpfOrCnpj" placeholder="" />
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="tel">Telefone</label>
                              <input type="text" class="form-control" id="tel" placeholder="Digite o telefone">
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="cep">CEP</label>
                              <input type="text" class="form-control" id="cep" placeholder="Digite o cep">
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="cep">Rua</label>
                                <input type="text" class="form-control" id="rua" size="60">
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="bairro">Bairro</label>
                                <input type="text" class="form-control" id="bairro" size="40">
                            </fieldset>
                            <fieldset class="form-group col-xs-10" style="padding: 0;">
                                <label for="cidade">Cidade</label>
                                <input type="text" class="form-control" id="cidade" size="40">
                            </fieldset>
                            <fieldset class="form-group col-xs-2">
                                <label for="uf">Estado</label>
                                <input type="text" class="form-control" id="uf" size="2">
                            </fieldset>
                            <p class="col-xs-12" style="text-align: center;"><button type="submit" class="btn btn-primary">Cadastrar</button></p>
                        </form>
                      </div>
                    <div class="panel-footer"><a id="signInOrUp" href="#"></a></div>
                   </div>
                </div>
            </div>
            
            <!--FOOTER-->   
            <div class = "row" style="text-align: center">
                <footer class="footer">
                    <div class="container">
                      <p class="text-muted"><b>Comunidade do livro</b></p>
                      <p class="text-muted">Desenvolvido por: <i>Carlos Henrique & Augusto Soares</i></p>
                    </div>
                </footer>
            </div>
        </div>
    </body>
</html>
