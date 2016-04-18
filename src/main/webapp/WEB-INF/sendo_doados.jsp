<%@ page import="model.User" %>
<%
  User user = null;
  if(session.getAttribute("user") != null){
    user = ((User)session.getAttribute("user"));
  }
%>

<link rel="stylesheet" type="text/css" href="custom-resources/css/general.css">
<link rel="stylesheet" type="text/css" href="../slick/slick.css">
<link rel="stylesheet" type="text/css" href="../slick/slick-theme.css">
    <div class="container-fluid">
        <div class="row">
            <%if(user == null){%>
                <div>
                    Seja bem-vindo, Visitante.
                </div>
            <%} else {%>
                <div>
                    Seja bem-vindo, <%= user.getName() %>.
                </div>
            <%}%>
        </div>
        <div class="row">
            <h2 style="text-align: center;">Livros Disponíveis Para Doação</h2>
            <div id="sendoDoadosCarrousel" style="padding: 0; margin: 0;">
                <!-- IMPLEMENTAR VISUALIZACAO DE LIVROS SENDO DOADOS -->
                <!-- SLICK CARROUSSEL -->
            </div>
        </div>
    </div>
    <script src="jquery/jquery-1.12.1.min.js"></script>
    <script src="../custom-resources/js/sendo_doados.js"></script>
    <script type="text/javascript" src="../slick/slick.min.js"></script>
</div>

