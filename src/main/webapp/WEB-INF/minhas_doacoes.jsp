<%
    if(request.getParameter("pick") != null && request.getParameter("pick").equals("sair")){
        session.removeAttribute("user");
    }
    
    if(session.getAttribute("user") != null){
%>
<div id="conteudo">
    MINHAS DOA��ES
</div>
<%} else {
        response.sendRedirect("/index.jsp");
}%>