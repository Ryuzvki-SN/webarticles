package istia.st.articles.web.spring;

import istia.st.articles.exception.UncheckedAccessArticlesException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class ListController implements Controller {
    // la configuration de l'appli web
    Config config;
    public void setConfig(Config config) {
        this.config = config;
    }
    // traitement de la reqête
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
// on demande la liste des articles
        List articles = null;
        try {
            articles = config.getArticlesDomain().getAllArticles();
        } catch (UncheckedAccessArticlesException ex) {
// on mémorise l'erreur
            ArrayList erreurs = new ArrayList();
            erreurs.add("Erreur lors de l'obtention de tous les articles : "
                    + ex.toString());
// on affiche la page des erreurs
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
// envoyer la vue erreurs
            return new ModelAndView("erreurs");
        }
// on affiche la liste des articles
        request.setAttribute("listarticles", articles);
        request.setAttribute("message", "");
        request.setAttribute("actions", new Hashtable[] { config
                .getHActionPanier() });
// envoyer la vue
        return new ModelAndView("liste");
    }
}