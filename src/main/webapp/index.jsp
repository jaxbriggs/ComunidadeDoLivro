<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="com.google.gson.JsonParser" %>
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
            e.setRua(request.getParameter("endereco[rua]"));

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
        } else if(userInfo == null && (String)request.getAttribute("userJson") != null) {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse((String)request.getAttribute("userJson")).getAsJsonObject();
            JsonObject end = obj.getAsJsonObject("endereco");
            
            Endereco e = new Endereco();
            User u = new User();
            
            //Seta o endereco
            e.setBairro(end.get("bairro").toString());
            e.setCep(end.get("cep").toString());
            e.setCidade(end.get("cidade").toString());
            e.setEstado(end.get("estado").toString());
            e.setNumero(Integer.parseInt(end.get("numero").toString()));
            e.setRua(end.get("rua").toString());

            //Carrega as configuracoes de usuario
            u.setId(Integer.parseInt(obj.get("id").toString()));
            u.setCelular(obj.get("celular").toString());
            try{
                u.setCnpj(obj.get("cnpj").toString());
            } catch(Exception ex) {
                u.setCnpj("");
            }
            try{
                u.setCpf(obj.get("cpf").toString());
            } catch(Exception ex) {
                u.setCpf("");
            }
            u.setEmail(obj.get("email").toString());
            u.setEndereco(e);
            u.setIsAdmin(Boolean.parseBoolean(obj.get("isAdmin").toString()));
            u.setIsAtivo(Boolean.parseBoolean(obj.get("isAtivo").toString()));
            u.setLogin(obj.get("login").toString());
            u.setName(obj.get("name").toString());
            u.setSenha(obj.get("senha").toString());
            u.setTelefone(obj.get("telefone").toString());

            session.setAttribute("user", u);
            
            response.sendRedirect("/index.jsp");
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
    
    <body id="index_body">
        <%@include file="page_components/sidebar.jsp"%>
        <!-- Page Content -->
        <div class="container" id="page-content-wrapper-container" style = "background-color: white; padding: 0;">
            <div id="page-content-wrapper" style="padding: 0;">
                
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
