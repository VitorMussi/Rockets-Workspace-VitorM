package br.com.vm.teste.portlet;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
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

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import br.com.vm.teste.constants.TestePortletKeys;
import br.com.vm.teste.portlet.TesteDTO;



/**
 * @author user
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Teste",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + TestePortletKeys.TESTE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class TestePortlet extends MVCPortlet {
	
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		
		
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
			
			long groupId = PortalUtil.getScopeGroupId(renderRequest);
            String articleId1 = "44972";
                                
            	         	            
            TesteDTO testeDTO = new TesteDTO();
            
            testeDTO.setTitulo(getStructureContentByName(articleId1, groupId, "titulo"));
            testeDTO.setThumb(getStructureContentByName(articleId1, groupId, "thumb"));
            testeDTO.setDescricao(getStructureContentByName(articleId1, groupId, "descricao"));
            testeDTO.setData(getStructureContentByName(articleId1, groupId, "data"));
            testeDTO.setLocal(getStructureContentByName(articleId1, groupId, "local"));
                        
                   
            renderRequest.setAttribute("testeDTO", testeDTO);
            
            renderRequest.setAttribute("user", user);
            
            	
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
