package istia.st.articles.web.struts;

import istia.st.articles.dao.Article;
import istia.st.articles.domain.Achat;
import istia.st.articles.domain.Panier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author ST
 *
 */
public class AchatArticleAction extends Action {
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
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherInfosArticle");
        }
        // on récupère la session du client
        HttpSession session = request.getSession();
        // on récupère l'article mis en session
        Article article = (Article) session.getAttribute("article");
        // session expirée ?
        if(article==null){
            // on affiche la page des erreurs
            erreurs.add("Votre session a expiré");
            request.setAttribute("erreurs", erreurs);
            request.setAttribute("actions", new Hashtable[] { mainServlet
                    .getHActionListe() });
            return mapping.findForward("afficherErreurs");
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
        return mapping.findForward("afficherListeArticles");
    }
}


