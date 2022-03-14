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
public class RetirerAchatAction extends Action {
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
        // la liste des erreurs sur cette action
        ArrayList erreurs = new ArrayList();
        // on récupère le panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null) {
            // session expirée
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherPanierVide");
        }
        // on récupère l'id de l'article à retirer
        String strId = request.getParameter("id");
        // qq chose ?
        if (strId == null) {
            // pas normal
            erreurs.add("action incorrecte([retirerachat,id=null]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
        }
        // on transforme strId en entier
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception ex) {
            // pas normal
            erreurs.add("action incorrecte([retirerachat,id=" + strId + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
        }
        // on enlève l'achat
        panier.enlever(id);
        // on affiche de nouveau le panier
        return mapping.findForward("afficherPanier");
    }
}
