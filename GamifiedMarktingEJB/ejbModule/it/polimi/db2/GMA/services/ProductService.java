package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;


@Stateless
public class ProductService {
    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public ProductService() {
    }

    public List<Product> findProductsByDate(Date date) {

        Query query = em.createQuery("SELECT p FROM Product p",Product.class);
        return (List<Product>) query.getResultList();
    }
}

