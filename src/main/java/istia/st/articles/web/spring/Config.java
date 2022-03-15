package istia.st.articles.web.spring;
import java.util.Hashtable;
import istia.st.articles.domain.IArticlesDomain;
/**
 * @author ST - ISTIA
 */
public class Config {
    // champs privés
    private IArticlesDomain articlesDomain = null;
    private final String ACTION_LISTE = "liste.do";
    private final String ACTION_PANIER = "panier.do";
    private final String ACTION_VALIDATION_PANIER = "validerpanier.do";
    private final String lienActionListe = "Liste des articles";
    private final String lienActionPanier = "Voir le panier";
    private final String lienActionValidationPanier = "Valider le panier";
    private Hashtable hActionListe = new Hashtable(2);
    private Hashtable hActionPanier = new Hashtable(2);
    private Hashtable hActionValidationPanier = new Hashtable(2);
    // getters-setters
    public IArticlesDomain getArticlesDomain() {
        return articlesDomain;
    }
    public void setArticlesDomain(IArticlesDomain articlesDomain) {
        this.articlesDomain = articlesDomain;
    }
    public Hashtable getHActionListe() {
        return hActionListe;
    }
    public Hashtable getHActionPanier() {
        return hActionPanier;
    }
    public Hashtable getHActionValidationPanier() {
        return hActionValidationPanier;
    }
    // init application web
    public void init() {
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