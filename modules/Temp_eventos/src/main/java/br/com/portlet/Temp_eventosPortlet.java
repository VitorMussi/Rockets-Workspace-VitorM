package br.com.portlet;


import com.liferay.headless.commerce.admin.account.dto.v1_0.User;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
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

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import br.com.constants.Temp_eventosPortletKeys;

/**
 * @author user
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Temp_eventos",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + Temp_eventosPortletKeys.TEMP_EVENTOS,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class Temp_eventosPortlet extends MVCPortlet {
	

	
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
            User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
            
            long groupId = PortalUtil.getScopeGroupId(renderRequest);
            long folderId = getFolderIdByName(groupId, "EventosDesafio"); // Nome da pasta "Eventos"
            
            List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles(groupId, folderId);
            List<EventoDTO> eventos = new ArrayList<>();
            
            for (JournalArticle article : articles) {
                EventoDTO eventoDTO = new EventoDTO();
                
                eventoDTO.setTituloevento(getStructureContentByName(article.getArticleId(), groupId, "tituloevento"));
                eventoDTO.setImagemevento(getStructureContentByName(article.getArticleId(), groupId, "imagemevento"));
                eventoDTO.setDatahorainicio(getStructureContentByName(article.getArticleId(), groupId, "datahorainicio"));
                eventoDTO.setDatahorafinal(getStructureContentByName(article.getArticleId(), groupId, "datahorafinal"));
                eventoDTO.setDescricao(getStructureContentByName(article.getArticleId(), groupId, "descricao"));
                eventoDTO.setConteudoevento(getStructureContentByName(article.getArticleId(), groupId, "conteudoevento"));
                eventoDTO.setLocalidade(getStructureContentByName(article.getArticleId(), groupId, "localidade"));
                eventoDTO.setUrlamigavel(getStructureContentByName(article.getArticleId(), groupId, "urlamigavel"));
                
       
                eventos.add(eventoDTO);
                
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
 
                
                
                
    
    
    
  