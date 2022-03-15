package istia.st.articles.web.spring;

import istia.st.articles.dao.Article;
import istia.st.articles.domain.Achat;
import istia.st.articles.domain.Panier;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class AchatController implements Controller {
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
        // on récupère la quantité achetée
        int qté = 0;
        try {
            qté = Integer.parseInt(request.getParameter("qte"));
            if (qté <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            // qté erronée
            request.setAttribute("msg", "Quantité incorrecte");
            request.setAttribute("qte", request.getParameter("qte"));
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("infos");
        }
        // on récupère la session du client
        HttpSession session = request.getSession();
        // on récupère l'article mis en session
        Article article = (Article) session.getAttribute("article");
        // session expirée ?
        if (article == null) {
            // on affiche la page des erreurs
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { config
                    .getHActionListe() });
            return new ModelAndView("erreurs");
        }
        // on crée le nouvel achat
        Achat achat = new Achat(article, qté);
        // on ajoute l'achat au panier du client
        Panier panier = (Panier) session.getAttribute("panier");
        if (panier == null) {
            panier = new Panier();
            session.setAttribute("panier", panier);
        }
        panier.ajouter(achat);
        // on revient à la liste des articles
        return new ModelAndView("index");
    }
}