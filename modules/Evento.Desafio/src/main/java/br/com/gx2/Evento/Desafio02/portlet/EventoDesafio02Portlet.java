package br.com.gx2.Evento.Desafio02.portlet;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.journal.util.comparator.ArticleModifiedDateComparator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import br.com.gx2.Evento.Desafio02.constants.EventoDesafio02PortletKeys;
import br.com.gx2.Evento.Desafio02.dto.EventoDTO;

/**
 * @author user
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=EventoDesafio02",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + EventoDesafio02PortletKeys.EVENTODESAFIO02,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class EventoDesafio02Portlet extends MVCPortlet {	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		
		
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
			
			long groupId = PortalUtil.getScopeGroupId(renderRequest);
			long folderId = getFolderIdByName(groupId, "Eventos");
			
			OrderByComparator<JournalArticle> orderByComparator = new ArticleModifiedDateComparator(false); 
			List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles(groupId, folderId, 0, 4, orderByComparator);
            List<EventoDTO> eventos = new ArrayList<>();
            
            for (JournalArticle article : articles) {
                EventoDTO eventoDTO = new EventoDTO();
            	         	            
                // Configuração do EventoDTO            
                eventoDTO.setTitulo(getStructureContentByName(article.getArticleId(), groupId, "titulo"));
                eventoDTO.setThumb(getStructureContentByName(article.getArticleId(), groupId, "thumb"));
                eventoDTO.setDescricao(getStructureContentByName(article.getArticleId(), groupId, "descricao"));
                eventoDTO.setData(getStructureContentByName(article.getArticleId(), groupId, "data"));
                eventoDTO.setLocal(getStructureContentByName(article.getArticleId(), groupId, "local"));
                eventoDTO.setUrlamigavel(getStructureContentByName(article.getArticleId(), groupId, "urlamigavel"));
                            
                
                
                // Truncamento da descrição para 50 caracteres
                String descricaoCompleta = getStructureContentByName(article.getArticleId(), groupId, "descricao");
                if (descricaoCompleta != null && descricaoCompleta.length() > 50) {
                    descricaoCompleta = descricaoCompleta.substring(0, 50) + "...";
                }
                eventoDTO.setDescricao(descricaoCompleta);
                
                
                                                                                                                            
                eventos.add(eventoDTO);
                
                if (eventos.size() == 4) {
                    break; // Parar se já adicionou 4 eventos únicos
                }
                                            
            }
                    
            
            renderRequest.setAttribute("eventos", eventos);
            renderRequest.setAttribute("user", user);

        } catch (PortalException | DocumentException e) {
            e.printStackTrace();
        }
		
		super.doView(renderRequest, renderResponse);
	}
	
    private long getFolderIdByName(long groupId, String folderName) throws PortalException {
        List<JournalFolder> folders = JournalFolderLocalServiceUtil.getFolders(groupId, 0);

        for (JournalFolder folder : folders) {
            if (folder.getName().equalsIgnoreCase(folderName)) {
                return folder.getFolderId();
            }
        }

        return 0; // Retorna 0 ou lança uma exceção se a pasta não for encontrada
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