package istia.st.articles.domain;
// Imports
import istia.st.articles.dao.Article;

import java.util.ArrayList; import java.util.List;
public abstract interface IArticlesDomain {
    // Méthodes
    public void acheter(Panier panier);
    public List getAllArticles();
    public Article getArticleById(int id);
    public ArrayList getErreurs();

}

