package istia.st.articles.web.spring;

import istia.st.articles.domain.Panier;
import istia.st.articles.exception.UncheckedAccessArticlesException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class ValiderPanierController implements Controller {
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
        // l'acheteur a confirmé son panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null) {
            // session expirée
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on valide le panier
        try {
            config.getArticlesDomain().acheter(panier);
        } catch (UncheckedAccessArticlesException ex) {
            // pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on récupère les éventuelles erreurs
        erreurs = config.getArticlesDomain().getErreurs();
        if (erreurs.size() != 0) {
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe(), config.getHActionPanier() });
            return new ModelAndView("erreurs");
        }
        // tout semble OK - on affiche la liste des articles
        return new ModelAndView("index");
    }
}
