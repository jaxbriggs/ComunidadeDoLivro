<%
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }
    
    if(session.getAttribute("user") != null){
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
        <%@page contentType="text/html; charset=ISO-8859-1" %>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="stylesheet" type="text/css" href="bootstrap-3.3.6-dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="custom-resources/font-awesome-4.5.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="../jquery/jquery-ui-1.11.4/themes/smoothness/jquery-ui.min.css">
        <link rel="stylesheet" type="text/css" href="../custom-resources/css/general.css">
        <script src="jquery/jquery-1.12.1.min.js"></script>
        <script src="../jquery/jquery-ui-1.11.4/jquery-ui.min.js"></script>
        <script src="bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <script src="../custom-resources/js/ajax/requests.js"></script>
        <script src="../custom-resources/js/novo_pedido.js"></script>
        <script src="../custom-resources/js/sidebar_menu.js"></script>
    </head>
<body>
 <!--Menu de acesso as funções-->        
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                
              <!-- Menu meus pedidos -->
              <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#"><span><i class="fa fa-archive"></i></span> Meus Pedidos</a>
              </div>

              <!-- Menu novo pedido -->
              <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="">
                        <a  id="btnNovoPedido" href="#"><span><i class="fa fa-plus fa-lg"></i></span> Novo Pedido </a>
                    </li>
                </ul>
                <form class="navbar-form navbar-right" role="search">
               
                  <!-- Busca de pedidos -->
                  <div class="form-group" style="display: table;">
                      <span style="width: 1%;" class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
                    <input type="text" class="form-control" placeholder="Pedido">
                  </div> 
                </form>
              </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
 <!--Novo pedido-->
 
        
 <!--Termina Novo pedido-->
 
 
 <!--Filtrar Pedidos-->
 
        
 <!--Filtrar Pedidos-->
 
 
 
 
<!--Término do Menu de acesso as funções-->   
</body>

<%} else {
        response.sendRedirect("/index.jsp");
}%>

</html>