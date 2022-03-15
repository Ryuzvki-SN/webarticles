package istia.st.articles.web.spring;

import istia.st.articles.dao.Article;
import istia.st.articles.exception.UncheckedAccessArticlesException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class InfosController implements Controller {
    // la configuration de l'appli web
    Config config;
    public void setConfig(Config config) {
        this.config = config;
    }
    // traitement de la reqête
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // la liste des erreurs
        ArrayList erreurs = new ArrayList();
        // on récupère l'id demandé
        String strId = request.getParameter("id");
        // qq chose ?
        if (strId == null) {
            // pas normal
            erreurs.add("action incorrecte([infos,id=null]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config.getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on transforme strId en entier
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception ex) {
            // pas normal
            erreurs.add("action incorrecte([infos,id=" + strId + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config.getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on demande l'article de clé id
        Article article = null;
        try {
            article = config.getArticlesDomain().getArticleById(id);
        } catch (UncheckedAccessArticlesException ex) {
            // pas normal
            erreurs.add("Erreur d'accès aux données [" + ex.toString() + "]");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config.getHActionListe() });
            return new ModelAndView("erreurs");
        }
        if (article == null) {
            // pas normal
            erreurs.add("Article de clé [" + id + "] inexistant");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config.getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on met l'article dans la session
        request.getSession().setAttribute("article", article);
        // on affiche la page d'infos
        request.setAttribute("actions", new Hashtable[] { config.getHActionListe() });
        return new ModelAndView("infos");
    }
}