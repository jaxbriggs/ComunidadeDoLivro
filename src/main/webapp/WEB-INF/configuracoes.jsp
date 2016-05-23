<%@ page import="model.User" %>
<%@ page import="model.Endereco" %>
<%
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }
    
    User user = null;
    if(session.getAttribute("user") != null){
        if(request.getParameter("userId") != null){
            session.removeAttribute("user");
            Endereco e = new Endereco();
            user = new User();
            
            //Seta o endereco
            e.setBairro(request.getParameter("bairro"));
            e.setCep(request.getParameter("cep"));
            e.setCidade(request.getParameter("cidade"));
            e.setEstado(request.getParameter("uf"));
            e.setNumero(Integer.parseInt(request.getParameter("numero")));
            e.setRua(request.getParameter("rua"));

            //Carrega as configuracoes de usuario
            user.setId(Integer.parseInt(request.getParameter("userId")));
            user.setCelular(request.getParameter("cel"));
            
            if(request.getParameter("cpfOrCnpjString").equals("cnpj")){
                user.setCnpj(request.getParameter("cpfOrCnpj"));
            } else {
                user.setCnpj(null);
            }
            
            if(request.getParameter("cpfOrCnpjString").equals("cpf")){
                user.setCpf(request.getParameter("cpfOrCnpj"));
            } else {
                user.setCpf(null);
            }
            
            user.setEmail(request.getParameter("userEmail"));
            user.setEndereco(e);
            user.setIsAdmin(false);
            user.setIsAtivo(true);
            user.setLogin(request.getParameter("userLogin"));
            user.setName(request.getParameter("name"));
            user.setSenha(request.getParameter("signInPassword"));
            user.setTelefone(request.getParameter("tel"));

            session.setAttribute("user", user);
        } else {
            user = ((User)session.getAttribute("user"));
        }
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
                      <h3 class="panel-title">Configura��o de Perfil</h3>
                    </div>
                    <div class="panel-body">
                        <form id="signUpForm"/>
                            <input type="hidden" id="userId" name="userId" value="<%= user.getId() %>"/>
                            <input type="hidden" id="cpfOrCnpjString" name="cpfOrCnpjString" value="<%= user.getCpf() != null ? "cpf" : "cnpj" %>"/>
                            <legend>Cadastro B�sico</legend>
                             <fieldset class="form-group">
                                <label for="login">Eu sou uma: </label>
                                <div class="radio-inline">
                                    <label>
                                      <input type="radio" name="pessoa-inst" id="pessoaRadio" name="pessoaRadio" value="pessoa" <%= user.getCpf() != null ? "checked" : "" %>/>
                                      Pessoa F�sica
                                    </label>
                                </div>
                                <div class="radio-inline">
                                  <label>
                                    <input type="radio" name="pessoa-inst" id="optionsRadios2" name="optionsRadios2" value="inst" <%= user.getCnpj() != null ? "checked" : "" %>/>
                                    Institui��o
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
                                <label for="userLogin">Login de usu�rio <span style="color: red">*</span></label>
                              <input type="text" class="form-control" id="userLogin" name="userLogin" placeholder="Digite o login" required value="<%= user.getLogin() %>"/>
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="signInPassword">Senha <span style="color: red">*</span></label>
                              <input type="password" class="form-control" id="signInPassword" name="signInPassword" placeholder="Senha" required value="" pattern=".{6,}" />
                            </fieldset>
                            <fieldset class="form-group">
                              <label for="signInPassword2">Confirma��o da senha <span style="color: red">*</span></label>
                              <input type="password" class="form-control" id="signInPassword2" name="signInPassword2" placeholder="Senha" required pattern=".{6,}" />
                            </fieldset>
                            <legend>Cadastro Avan�ado</legend>
                            <fieldset class="form-group">
                                <label id="lblCpfOrCnpj" for="cpfOrCnpj"></label>
                                <input type="text" class="form-control" id="cpfOrCnpj" name="cpfOrCnpj" placeholder="" value="<%= user.getCpf() != null ? user.getCpf() : user.getCnpj() %>" />
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
                                <label for="numero">N�mero</label>
                                <input type="number" class="form-control" id="numero" name="numero" size="60" value="<%= user.getEndereco().getNumero() != null ? user.getEndereco().getNumero() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-12" style="padding: 0;">
                                <label for="bairro">Bairro</label>
                                <input type="text" class="form-control" id="bairro" name="bairro" size="40" value="<%= user.getEndereco().getBairro() != null ? user.getEndereco().getBairro() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-10" style="padding: 0;">
                                <label for="cidade">Cidade</label>
                                <input type="text" class="form-control" id="cidade" name="cidade" size="40" value="<%= user.getEndereco().getCidade() != null ? user.getEndereco().getCidade() : "" %>"/>
                            </fieldset>
                            <fieldset class="form-group col-xs-2">
                                <label for="uf">Estado</label>
                                <input type="text" class="form-control" id="uf" name="uf" size="2" value="<%= user.getEndereco().getEstado() != null ? user.getEndereco().getEstado() : "" %>"/>
                            </fieldset>
                            <p class="col-xs-12" style="text-align: center;"><button type="submit" class="btn btn-primary" id="btnSalvarAlteracoes">Salvar Altera��es</button></p>
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