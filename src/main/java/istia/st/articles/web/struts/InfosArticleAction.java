package istia.st.articles.web.struts;

import istia.st.articles.dao.Article;
import istia.st.articles.domain.IArticlesDomain;
import istia.st.articles.exception.UncheckedAccessArticlesException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author ST-ISTIA
 *
 */
public class InfosArticleAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // la servlet de contrôle
        MainServlet mainServlet = (MainServlet) this.getServlet();
        // erreurs d'initialisation ?
        ArrayList erreursInit = mainServlet.getErreurs();
        if (erreursInit.size() != 0) {
            // on affiche la page des erreurs
            request.setAttribute("erreurs", erreursInit);
            request.setAttribute("actions", new Hashtable[]{});
            return mapping.findForward("afficherErreurs");
        }
        // l'objet d'accès au domaine
        IArticlesDomain articlesDomain = mainServlet.getArticlesDomain();
        // la liste des erreurs
        ArrayList erreurs = new ArrayList();
        // on récupère l'id demandé
        String strId = request.getParameter("id");
        // qq chose ?
        if (strId == null) {
            // pas normal
            erreurs.add("action incorrecte([infos,id=null]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{mainServlet
                    .getHActionListe()});
            return mapping.findForward("afficherErreurs");
        }
        // on transforme strId en entier
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception ex) {
            // pas normal
            erreurs.add("action incorrecte([infos,id=" + strId + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{mainServlet
                    .getHActionListe()});
            return mapping.findForward("afficherErreurs");
        }
        // on demande l'article de clé id
        Article article = null;
        try {
            article = articlesDomain.getArticleById(id);
        } catch (UncheckedAccessArticlesException ex) {
            // pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{mainServlet
                    .getHActionListe()});
            return mapping.findForward("afficherErreurs");
        }
        if (article == null) {
            // pas normal
            erreurs.add("Article de clé [" + id + "] inexistant");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[]{mainServlet
                    .getHActionListe()});
            return mapping.findForward("afficherErreurs");
        }
        // on met l'article dans la session
        request.getSession().setAttribute("article", article);
        // on affiche la page d'infos
        request.setAttribute("actions", new Hashtable[]{mainServlet
                .getHActionListe()});
        return mapping.findForward("afficherInfosArticle");
    }
}
