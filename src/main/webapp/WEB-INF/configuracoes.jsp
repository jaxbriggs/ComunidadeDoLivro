<%@ page import="model.User" %>
<%
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }
    
    User user = null;
    if(session.getAttribute("user") != null){
        user = ((User)session.getAttribute("user"));
%>
<link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
<link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">
<div id="conteudo" class="container-fluid">
    <div class="row">
        <div class="col-xs-offset-1 col-xs-10" id="" style="padding: 0;">
            <div class="col-xs-12" style=" text-align: center; margin-bottom: 3%; margin-top: 3%;">
                 <h2>Meu Perfil</h2>
            </div>
            <div class="col-xs-offset-2 col-xs-8" style="margin-bottom: 8%;">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                      <h3 class="panel-title">Configuração de Perfil</h3>
                    </div>
                    <div class="panel-body">
                        <form id="signUpForm" method="post" action=""/>
                            <legend>Cadastro Básico</legend>
                             <fieldset class="form-group">
                                <label for="login">Eu sou uma: </label>
                                <div class="radio-inline">
                                    <label>
                                      <input type="radio" name="pessoa-inst" id="pessoaRadio" name="pessoaRadio" value="pessoa" <%= user.getCpf() != null ? "checked" : "" %>/>
                                      Pessoa Física
                                    </label>
                                </div>
                                <div class="radio-inline">
                                  <label>
                                    <input type="radio" name="pessoa-inst" id="optionsRadios2" name="optionsRadios2" value="inst" <%= user.getCnpj() != null ? "checked" : "" %>/>
                                    Instituição
                                  </label>
                                </div>
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="name">Nome <span style="color: red">*</span></label>
                                <input type="text" class="form-control" id="name" name="name" placeholder="Digite o nome" required checked="checked" value="<%= user.getName() %>"/>
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="userEmail">Email <span style="color: red">*</span></label>
                                <input type="email" class="form-control" id="userEmail" name="userEmail" placeholder="Digite o email" required value="<%= user.getEmail() %>"/>
                            </fieldset>
                            <fieldset class="form-group">
                                <label for="userLogin">Login de usuário <span style="color: red">*</span></label>
                              <input type="text" class="form-control" id="userLogin" name="userLogin" placeholder="Digite o login" required value="<%= user.getLogin() %>"/>
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="signInPassword">Senha <span style="color: red">*</span></label>
                              <input type="password" class="form-control" id="signInPassword" name="signInPassword" placeholder="Senha" required value=""/>
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
                                <input type="text" class="form-control" id="tel" name="tel" placeholder="Digite o telefone" value="<%= user.getTelefone() != null ? user.getTelefone() : "" %>" />
                            </fieldset>
                            <fieldset class="form-group col-xs-6" style="padding: 0 0 0 2%;">
                                <label for="cel">Celular</label>
                              <input type="text" class="form-control" id="cel" name="cel" placeholder="Digite o celular" value="<%= user.getCelular() != null ? user.getCelular() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-12" style="padding: 0;">
                                <label for="cep">CEP</label>
                              <input type="text" class="form-control" id="cep" name="cep" placeholder="Digite o cep" value="<%= user.getEndereco().getCep() != null ? user.getEndereco().getCep() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-9" style="padding: 0;">
                                <label for="rua">Rua</label>
                                <input type="text" class="form-control" id="rua" name="rua" size="60" value="<%= user.getEndereco().getRua() != null ? user.getEndereco().getRua() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-3" style="padding: 0 2%;">
                                <label for="numero">Número</label>
                                <input type="number" class="form-control" id="numero" name="numero" size="60" value="<%= user.getEndereco().getNumero() != null ? user.getEndereco().getNumero() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-12" style="padding: 0;">
                                <label for="bairro">Bairro</label>
                                <input type="text" class="form-control" id="bairro" name="bairro" size="40" value="<%= user.getEndereco().getBairro() != null ? user.getEndereco().getBairro() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-10" style="padding: 0;">
                                <label for="cidade">Cidade</label>
                                <input type="text" class="form-control" id="cidade" name="cidade" size="40" value="<%= user.getEndereco().getEstado() != null ? user.getEndereco().getEstado() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-2">
                                <label for="uf">Estado</label>
                                <input type="text" class="form-control" id="uf" name="uf" size="2" value="<%= user.getEndereco().getCidade() != null ? user.getEndereco().getCidade() : "" %>"/>
                            </fieldset>
                            <p class="col-xs-12" style="text-align: center;"><button type="submit" class="btn btn-primary" id="btnSalvarAlteracoes">Salvar Alterações</button></p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="jquery/jquery-1.12.1.min.js"></script>
<script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
<script src="custom-resources/js/index-script.js"></script>
<script src="../custom-resources/js/configuracoes.js"></script>
<%} else {
        response.sendRedirect("/index.jsp");
}%>