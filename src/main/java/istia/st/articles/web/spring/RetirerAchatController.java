package istia.st.articles.web.spring;

import istia.st.articles.domain.Panier;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class RetirerAchatController implements Controller {
    // la configuration de l'appli web
    Config config;
    public void setConfig(Config config) {
        this.config = config;
    }
    // traitement de la reqête
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // la liste des erreurs sur cette action
        ArrayList erreurs = new ArrayList();
        // on récupère le panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null) {
            // session expirée
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on récupère l'id de l'article à retirer
        String strId = request.getParameter("id");
        // qq chose ?
        if (strId == null) {
            // pas normal
            erreurs.add("action incorrecte([retirerachat,id=null]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on transforme strId en entier
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception ex) {
            // pas normal
            erreurs.add("action incorrecte([retirerachat,id=" + strId + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on enlève l'achat
        panier.enlever(id);
        // on affiche de nouveau le panier
        request.setAttribute("actions",
                new Hashtable[] { config.getHActionListe() });
        return new ModelAndView("redirpanier");
    }
}
