package istia.st.articles.web.struts;

import istia.st.articles.domain.IArticlesDomain;
import istia.st.articles.exception.UncheckedAccessArticlesException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author ST - ISTIA
 *
 */
public class ListeArticlesAction extends Action {
    /**
     * affichage de la liste des articles - s'appuie sur la couche [domain]
     *
     * @param mapping :
     * configuration de l'action dans struts-config.xml
     * @param form :
     * le formulaire passé à l'action - ici aucun
     * @param request :
     * la requête HTTP du client
     * @param response :
     * la réponse HTTP au client
     */
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
            request.setAttribute("actions", new Hashtable[] {});
            return mapping.findForward("afficherErreurs");
        }
        // l'objet d'accès au domaine
        IArticlesDomain articlesDomain = mainServlet.getArticlesDomain();
        // la liste des erreurs
        ArrayList erreurs = new ArrayList();
        // on demande la liste des articles
        List articles = null;
        try {
            articles = articlesDomain.getAllArticles();
        } catch (UncheckedAccessArticlesException ex) {
            // on mémorise l'erreur
            erreurs.add("Erreur lors de l'obtention de tous les articles : "
                    + ex.toString());
        }
        // des erreurs ?
        if (erreurs.size() != 0) {
            // on affiche la page des erreurs
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
        }
        // on affiche la liste des articles
        request.setAttribute("listarticles", articles);
        request.setAttribute("message", "");
        request.setAttribute("actions", new Hashtable[] { mainServlet
                .getHActionPanier() });
        return mapping.findForward("afficherListeArticles");
    }
}
