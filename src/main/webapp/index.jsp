<%@ page import="model.User" %>
<%@ page import="model.Endereco" %>

<%
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }    
   
    User userInfo = ((User)session.getAttribute("user"));
    
    //if((userInfo == null && request.getParameter("id") != null) || userInfo != null){
        if(userInfo == null && request.getParameter("id") != null){
            Endereco e = new Endereco();
            User u = new User();

            //Seta o endereco
            e.setBairro(request.getParameter("endereco[bairro]"));
            e.setCep(request.getParameter("endereco[cep]"));
            e.setCidade(request.getParameter("endereco[cidade]"));
            e.setEstado(request.getParameter("endereco[estado]"));
            e.setNumero(Integer.parseInt(request.getParameter("endereco[numero]")));
            e.setRua(request.getParameter("rua"));

            //Carrega as configuracoes de usuario
            u.setId(Integer.parseInt(request.getParameter("id")));
            u.setCelular(request.getParameter("celular"));
            u.setCnpj(request.getParameter("cnpj"));
            u.setCpf(request.getParameter("cpf"));
            u.setEmail(request.getParameter("email"));
            u.setEndereco(e);
            u.setIsAdmin(Boolean.parseBoolean(request.getParameter("isAdmin")));
            u.setIsAtivo(Boolean.parseBoolean(request.getParameter("isAtivo")));
            u.setLogin(request.getParameter("login"));
            u.setName(request.getParameter("name"));
            u.setSenha(request.getParameter("senha"));
            u.setTelefone(request.getParameter("telefone"));

            session.setAttribute("user", u);
        }%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@page contentType="text/html; charset=ISO-8859-1" %>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/css/sidebar.css">
        <title>Comunidade do Livro</title>
    </head>
    
    <body>
        <%@include file="page_components/sidebar.jsp"%>
        <!-- Page Content -->
        <div class="container" id="page-content-wrapper-container" style = "background-color: white; padding: 0; margin: 0;">
            <div class = "row" id="page-content-wrapper" style="padding: 0; margin: 0;">
                
            </div>
        </div>
        <!-- /#page-content-wrapper -->
        <!-- /#wrapper -->
        <!-- jQuery -->
        <script src="jquery/jquery-1.12.1.min.js"></script>
        <script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <script src="custom-resources/js/jquery.redirect.js"></script>
        <script src="custom-resources/js/sidebar_menu.js"></script>
    </body>
</html>
<%//} else {
        //response.sendRedirect("/index.jsp");
//}%>