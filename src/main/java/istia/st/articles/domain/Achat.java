package istia.st.articles.domain;

import istia.st.articles.dao.Article;

public class Achat {
    /**
     * l'article acheté
     */
    private Article article;
    /**
     * la qté achetée
     */
    private int qte;
    /**
     * @return Returns le montant à payer pour l'achat
     */
    public double getTotal() {
        return article.getPrix() * qte;
    }
    /**
     *
     * @param article
     * l'article acheté
     * @param qte
     * la qté achetée
     */
    public Achat(Article article, int qte) {
        this.article = article;
        this.qte = qte;
    }
/**
 * @return Returns l'article acheté
 */
public Article getArticle() {
    return article;
}
    /**
     * @param article
     * l'article acheté
     */
    public void setArticle(Article article) {
        this.article = article;
    }
    /**
     * @return Returns la qté achetée
     */
    public int getQte() {
        return qte;
    }
    /**
     * @param qte
     * la qté achetée
     */
    public void setQte(int qte) {
        this.qte = qte;
    }
    /**
     * @return chaîne représentant l'achat
     */
    public String toString() {
        return "[" + this.article.toString() + "," + this.qte + "]";
    }
}

