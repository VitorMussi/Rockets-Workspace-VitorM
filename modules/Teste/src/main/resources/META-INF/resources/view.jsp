<%@ include file="/init.jsp" %>

<%@ 

TesteDTO testeDTO = (testeDTO)request.getAttribute("TesteDTO");
%>

<h2>Titulo: <%=testeDTO.getTitulo() %></h2>
<p>Descri��o: <%=testeDTO.getDescricao() %></p>
<p>Data: <%=testeDTO.getData() %></p>
<p>Local: <%=testeDTO.getLocal() %></p>
