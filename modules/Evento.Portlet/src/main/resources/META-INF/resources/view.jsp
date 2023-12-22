<%@ include file="/init.jsp" %>

<c:if test="${listaNoticias ne null and not empty listaNoticias}">
	<div class="row m-0 d-flex justify-content-around">
		<c:forEach var="noticia" items="${listaNoticias}">
			<c:if test="${noticia.localidade eq valorLocalidade}">
				<a href="back-end/w/${friendlyURL}" class="card-event shadow-sm d-flex flex-column" style="text-decoration: none">
					<img src="https://i.imgur.com/9apdyP4.jpg" alt="Imagem da Notícia" class="card-event-img-top w-100 h-75">
					<div class="card-event-body d-flex align-items-center justify-content-center text-center px-3">
						<p class="card-event-body-description">${noticia.descricao}</p>
					</div>
				</a>
			</c:if>
		</c:forEach>
	</div>
</c:if>