package istia.st.articles.web.struts;

import istia.st.articles.domain.Panier;
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
public class VoirPanierAction extends Action {
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
        // on affiche le panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null || panier.getAchats().size() == 0) {
            // panier vide
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherPanierVide");
        } else {
            // il y a qq chose dans le panier
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe(), mainServlet.getHActionValidationPanier() });
            return mapping.findForward("afficherPanier");
        }
    }
}

