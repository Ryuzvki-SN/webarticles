package istia.st.articles.web.struts;
import istia.st.articles.domain.IArticlesDomain;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionServlet;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
/**
 * @author ST - ISTIA
 *
 */
public class MainServlet extends ActionServlet {
    // champs privés
    private ArrayList erreurs = new ArrayList();
    private IArticlesDomain articlesDomain = null;
    private final String SPRING_CONFIG_FILENAME = "springConfigFileName";
    private final String[] parameters = {SPRING_CONFIG_FILENAME};
    private ServletConfig config;
    private final String ACTION_LISTE = "liste.do";
    private final String ACTION_PANIER = "panier.do";
    private final String ACTION_ACHAT = "achat.do";
    private final String ACTION_INFOS = "infos.do";
    private final String ACTION_RETIRER_ACHAT = "retirerachat.do";
    private final String ACTION_VALIDATION_PANIER = "validerpanier.do";
    private String urlActionListe;
    private final String lienActionListe = "Liste des articles";
    private String urlActionPanier;
    private final String lienActionPanier = "Voir le panier";
    private String urlActionValidationPanier;
    private final String lienActionValidationPanier = "Valider le panier";
    private Hashtable hActionListe = new Hashtable(2);
    private Hashtable hActionPanier = new Hashtable(2);
    private Hashtable hActionValidationPanier = new Hashtable(2);

    // getters - setters
    public IArticlesDomain getArticlesDomain() {
        return articlesDomain;
    }

    public void setArticlesDomain(IArticlesDomain articlesDomain) {
        this.articlesDomain = articlesDomain;
    }

    public ArrayList getErreurs() {
        return erreurs;
    }

    public void setErreurs(ArrayList erreurs) {
        this.erreurs = erreurs;
    }

    public Hashtable getHActionListe() {
        return hActionListe;
    }

    public void setHActionListe(Hashtable actionListe) {
        hActionListe = actionListe;
    }

    public Hashtable getHActionPanier() {
        return hActionPanier;
    }

    public void setHActionPanier(Hashtable actionPanier) {
        hActionPanier = actionPanier;
    }

    public Hashtable getHActionValidationPanier() {
        return hActionValidationPanier;
    }

    public void setHActionValidationPanier(Hashtable actionValidationPanier) {
        hActionValidationPanier = actionValidationPanier;
    }
    public void init() throws ServletException{
        // init classe parent
        super.init();
        // on récupère les paramètres d'initialisation de la servlet
        config = getServletConfig();
        String param = null;
        for (int i = 0; i < parameters.length; i++) {
            param = config.getInitParameter(parameters[i]);
            if (param == null) {
                // on mémorise l'erreur
                erreurs.add("Paramètre [" + parameters[i]
                        + "] absent dans le fichier [web.xml]");
            }
        }
        // des erreurs ?
        if (erreurs.size() != 0) {
            return;
        }
        // on crée un objet IArticlesDomain d'accès à la couche métier
        try {
            articlesDomain = (IArticlesDomain) (new XmlBeanFactory(
                    new ClassPathResource((String) config
                            .getInitParameter(SPRING_CONFIG_FILENAME))))
                    .getBean("articlesDomain");
        } catch (Exception ex) {
            // on mémorise l'erreur
            erreurs.add("Erreur de configuration de l'accès aux données : "
                    + ex.toString());
            return;
        }
        // on mémorise certaines url de l'application
        hActionListe.put("href", ACTION_LISTE);
        hActionListe.put("lien", lienActionListe);
        hActionPanier.put("href", ACTION_PANIER);
        hActionPanier.put("lien", lienActionPanier);
        hActionValidationPanier.put("href", ACTION_VALIDATION_PANIER);
        hActionValidationPanier.put("lien", lienActionValidationPanier);
        // c'est fini
        return;
    }
}