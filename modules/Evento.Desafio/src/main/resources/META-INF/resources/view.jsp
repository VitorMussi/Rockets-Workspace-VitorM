<%@ include file="/init.jsp" %>

<%

EventoDTO eventoDTO = (EventoDTO)request.getAttribute("eventoDTO");
%>


<%@ page import="java.util.List" %>
<%@ page import="br.com.gx2.Evento.Desafio02.dto.EventoDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Eventos</title>
    <!-- Incluir estilos CSS adicionais, se necessário -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .btn-custom {
            background-color: transparent;
            color: #007bff; /* Esta é a cor padrão dos links do Bootstrap */
            border-color: transparent;
            box-shadow: none; /* Remove sombras */
        }
        .btn-custom:hover {
            background-color: transparent;
            color: #0056b3; /* Cor mais escura para o hover */
            border-color: transparent;
        }
    </style>
    
    
    
</head>
<body>

<div class="container mt-4">
    <div class="row">
        <% List<EventoDTO> eventos = (List<EventoDTO>) request.getAttribute("eventos");
           if (eventos != null && !eventos.isEmpty()) {
               for (EventoDTO evento : eventos) {
        %>
                <div class="col-md-4 mb-4">
                    <div class="card" style="width: 18rem;">
                        <% if (evento.getThumb() != null && !evento.getThumb().isEmpty()) { %>
                            <img class="card-img-top" src="<%= evento.getThumb() %>" alt="Thumbnail">
                        <% } %>
                        <div class="card-body">
                            <h5 class="card-title"><%= evento.getTitulo() %></h5>
                            <p class="card-text"><%= evento.getDescricao() %></p>
                            <p class="card-text"><small class="text-muted">Data: <%= evento.getData() %></small></p>
                            <p class="card-text"><small class="text-muted">Local: <%= evento.getLocal() %></small></p>
                            <% if (evento.getUrlamigavel() != null && !evento.getUrlamigavel().isEmpty()) { %>
                                <a href="<%= evento.getUrlamigavel() %>" class="btn btn-custom">LEIA MAIS</a>
                            <% } %>
                        </div>
                    </div>
                </div>
        <% 
               }
           } else {
        %>
            <div class="col-12">
                <p>Nenhum evento disponível.</p>
            </div>
        <% 
           } 
        %>
    </div>
</div>

</body>
</html>
