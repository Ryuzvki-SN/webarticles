package istia.st.articles.web.struts;
import istia.st.articles.domain.IArticlesDomain;
import istia.st.articles.domain.Panier;
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
public class ValiderPanierAction extends Action {
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
        // la liste des erreurs sur cette action
        ArrayList erreurs = new ArrayList();
// l'acheteur a confirmé son panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null) {
            // session expirée
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
        }
// on valide le panier
        try {
            articlesDomain.acheter(panier);
        } catch (UncheckedAccessArticlesException ex) {
// pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
        }
// on récupère les éventuelles erreurs
        erreurs = articlesDomain.getErreurs();
        if (erreurs.size() != 0) {
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe(),mainServlet.getHActionPanier() });
            return mapping.findForward("afficherErreurs");
        }
// tout semble OK - on affiche la liste des articles
        return mapping.findForward("afficherListeArticles");
    }
}