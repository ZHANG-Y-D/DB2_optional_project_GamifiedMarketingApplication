package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;


import java.util.List;


@Stateless
public class ProductService {
    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public ProductService() {
    }


    public List<Product> findProductsByDate(Date date) {

        List<Product> productList;
        productList = em.createNamedQuery("Product.findProductsByDate", Product.class)
                .setParameter("date", date)
                .getResultList();
        return productList;
    }

    public Product findProductByName(String name) {

        Product product;
        product = em.createNamedQuery("Product.findProductByName", Product.class)
                .setParameter("name", name)
                .getResultList().get(0);
        return product;
    }

}

