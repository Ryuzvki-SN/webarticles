package istia.st.articles.dao;

import java.io.*;
import java.sql.*;
import java.util.*;
import istia.st.articles.dao.Article;
import istia.st.articles.exception.*;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * @author ST-ISTIA
 *
 */
public class ArticlesDaoSqlMap implements IArticlesDao {

    private SqlMapClient sqlMap = null;

    public SqlMapClient getSqlMap()
    {
        return sqlMap;
    }

    public void setSqlMap(SqlMapClient sqlMap)
    {
        this.sqlMap = sqlMap;
    }

    public ArticlesDaoSqlMap(String sqlMapConfigFileName)
    {
        Reader reader = null;
        UncheckedAccessArticlesException ex = null;
        try
        {
            reader = Resources.getResourceAsReader(sqlMapConfigFileName);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        }
        catch (Exception ex1)
        {

            ex = new UncheckedAccessArticlesException(ex1.getMessage());
            /*
                    "Lou ma nekh alla Kandji! Erreur lors de la construction de l'objet [sqlMap] à partir du fichier de configuration ["
                            + sqlMapConfigFileName + "] : ", ex1*/
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (Exception ex2)
            {
                if (ex != null)
                {
                    ex = new UncheckedAccessArticlesException(
                            "Erreur lors de la fermeture de l'objet [reader] à partir du fichier de configuration ["
                                    + sqlMapConfigFileName + "] : ", ex2);
                }
            }
        }
        if (ex != null)
        {
            throw ex;
        }
    }

    public synchronized List getAllArticles()
    {
        try
        {
            return sqlMap.queryForList("getAllArticles", null);
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Echec de l'obtention de tous les articles : [" + ex + "]", ex);
        }
    }

    public synchronized int ajouteArticle(Article unArticle)
    {
        try
        {
            return sqlMap.update("insertArticle", initHashMapArticle(unArticle));
        }
        catch (SQLException ex) {
            throw new UncheckedAccessArticlesException(
                    "Echec de l'ajout de l'article [" + unArticle + "] : [" + ex + "]",
                    ex);
        }
    }

    private Map<String,Object> initHashMapArticle(Article unArticle)
    {
        Map<String,Object> parametres = new HashMap<>(5);

        parametres.put("id", unArticle.getId());
        parametres.put("nom", unArticle.getNom());
        parametres.put("prix", unArticle.getPrix());
        parametres.put("stockactuel", unArticle.getStockActuel());
        parametres.put("stockminimum", unArticle.getStockMinimum());

        return parametres;
    }

    public synchronized int supprimeArticle(int idArticle)
    {
        try
        {
            Map<String,Object> parametres = new HashMap<>(1);
            parametres.put("id", idArticle);
            return sqlMap.update("deleteArticle", parametres);
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la suppression de l'article d'id [" + idArticle
                            + "] : [" + ex + "]", ex);
        }
    }

    public synchronized int modifieArticle(Article unArticle)
    {
        try
        {
            return sqlMap.update("modifyArticle", initHashMapArticle(unArticle));
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la mise à jour de l'article [" + unArticle +
                            "] : ["
                            + ex + "]", ex);
        }
    }

    public synchronized Article getArticleById(int idArticle)
    {
        try
        {
            Map<String,Object> parametres = new HashMap<>(1);
            parametres.put("id", idArticle);
            return (Article) sqlMap.queryForObject("getArticleById", parametres);
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de la recherche de l'article d'id [" + idArticle
                            + "] : [" + ex + "]", ex);
        }
    }

    public synchronized void clearAllArticles()
    {
        try
        {
            sqlMap.update("clearAllArticles", null);
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors de l'effacement de la table des articles : [" + ex +
                            "]",
                    ex);
        }
    }

    public synchronized void augmenterTousLesPrix(int pourcentage)
    {
        try
        {
            Map<String, Object> parametres = new HashMap<>(1);
            parametres.put("pourcentage", 0.01 * pourcentage);
            sqlMap.update("augmentationPrix", parametres);
        }
        catch (SQLException ex)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur l'augmentation des prix : [" + ex + "]", ex);
        }
    }

    public void badOperationThrowsException() { throw new UncheckedAccessArticlesException(); }

    public synchronized void doInsertionsInTransaction(Article[] articles)
    {
        UncheckedAccessArticlesException ex = null;
        int i = 0;
        Connection connexion = null;
        try
        {
            sqlMap.startTransaction();
            for (; i < articles.length; i++) { ajouteArticle(articles[i]);}
            sqlMap.commitTransaction();
        }
        catch (Exception ex1)
        {
            ex = new UncheckedAccessArticlesException(
                    "doInsertionsInTransaction, erreur d'accès à la base : [" + ex1 +
                            "]",
                    ex1);
        }

        finally
        {
            try
            {
                sqlMap.endTransaction();
            }
            catch (SQLException ex2)
            {
                if(ex!=null)
                {
                    ex = new UncheckedAccessArticlesException(
                            "doInsertionsInTransaction, échec du close : [" + ex2 + "]", ex2);
                }
            }
        }
        if (ex != null)
        {
            throw ex;
        }
    }

    public synchronized int changerStockArticle(int idArticle, int mouvement)
    {
        try
        {
            Map<String,Object> parametres = new HashMap<String,Object>(2);
            parametres.put("id", idArticle);
            parametres.put("mouvement", mouvement);
            return sqlMap.update("changerStockArticle", parametres);
        }
        catch (SQLException e1)
        {
            throw new UncheckedAccessArticlesException(
                    "Erreur lors du changement de stock [idArticle=" + idArticle +
                            ", mouvement=" + mouvement + "] : [" + e1 + "]", e1);
        }
    }
}


