package br.com.gx2.eventos.portlet.portlet;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import br.com.gx2.eventos.portlet.constants.evenPortletKeys;


/**
 * @author user
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=even",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + evenPortletKeys.EVEN,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class evenPortlet extends MVCPortlet {
	
		@Override
		public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
				throws IOException, PortletException {

			try {
				ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
				User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());

				long groupId = PortalUtil.getScopeGroupId(renderRequest);
				String folderName = "Eventos";

				ExpandoBridge expandoBridge = user.getExpandoBridge();
				String valorLocal = (String) expandoBridge.getAttribute("Local");

				List<JournalFolder> folders = JournalFolderServiceUtil.getFolders(groupId, 0);

				long folderId = 0;

				for (JournalFolder folder : folders) {
					if (folder.getName().equals(folderName)) {
						folderId = folder.getFolderId();
						break;
					}
				}

				List<EventoDTO> listaNoticias = new ArrayList<>();

				List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles(groupId, folderId);

				for (JournalArticle article : articles) {
					JournalArticle curArticle = JournalArticleLocalServiceUtil.getArticle(themeDisplay.getScopeGroupId(),
							article.getArticleId());
					Locale locale = themeDisplay.getLocale();
					String friendlyURL = curArticle.getUrlTitle(locale);
					
					double currentVersion = article.getVersion();
					double latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(groupId, article.getArticleId());
					
					if (Math.abs(currentVersion - latestVersion) < 0.000001) {
						String articleId = article.getArticleId();
						String titulo = getStructureContentByName(articleId, groupId, "titulo");
						String descricao = getStructureContentByName(articleId, groupId, "descricao");
						String local = getStructureContentByName(articleId, groupId, "local");
						String thumb = getStructureContentByName(articleId, groupId, "thumb");
						String urlamigavel = getStructureContentByName(articleId, groupId, "urlamigavel");

						EventoDTO eventoDTO = new EventoDTO();
						eventoDTO.setTitulo(titulo);
						eventoDTO.setDescricao(descricao);
						eventoDTO.setLocal(local);
						eventoDTO.setThumb(thumb);
						eventoDTO.setUrlamigavel(urlamigavel);
						

						listaNoticias.add(eventoDTO);
					}
					renderRequest.setAttribute("friendlyURL", friendlyURL);
				}

				renderRequest.setAttribute("user", user);
				renderRequest.setAttribute("listaNoticias", listaNoticias);
				renderRequest.setAttribute("valorLocal", valorLocal);

			} catch (PortalException | DocumentException e) {
				e.printStackTrace();
			}

			super.doView(renderRequest, renderResponse);
		}

		public static String getStructureContentByName(String articleId, long groupId, String name)
				throws PortalException, DocumentException {
			final JournalArticle article = JournalArticleLocalServiceUtil.getArticle(groupId, articleId);
			final Document document = SAXReaderUtil.read(article.getContent());
			final Node node = document
					.selectSingleNode("/root/dynamic-element[@field-reference='" + name + "']/dynamic-content");

			return node != null ? node.getText() : null;
		}
	
}
	