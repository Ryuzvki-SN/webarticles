package istia.st.articles.web.spring;

import istia.st.articles.domain.Panier;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class VoirPanierController implements Controller {
    // la configuration de l'appli web
    Config config;
    public void setConfig(Config config) {
        this.config = config;
    }
    // traitement de la reqête
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // on affiche le panier
        Panier panier = (Panier) request.getSession().getAttribute("panier");
        if (panier == null || panier.getAchats().size() == 0) {
            // panier vide
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("paniervide");
        } else {
            // il y a qq chose dans le panier
            request.setAttribute("actions", new Hashtable[] {
                    config.getHActionListe(), config.getHActionValidationPanier() });
            return new ModelAndView("panier");
        }
    }
}