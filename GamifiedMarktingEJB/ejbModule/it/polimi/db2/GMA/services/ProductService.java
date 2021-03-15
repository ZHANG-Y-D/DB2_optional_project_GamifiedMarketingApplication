package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.MarketingQuestion;
import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.exceptions.ProductCreationException;

import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
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
        for (Product e : productList) {
            em.refresh(e);
        }
        return productList;
    }

    public Product findProductByName(String name) {

        Product product;
        product = em.createNamedQuery("Product.findProductByName", Product.class)
                .setParameter("name", name)
                .getResultList().get(0);
        em.refresh(product);
        return product;
    }

    public void createAProduct(String name, Date date, byte[] image, List<String> questions) throws ProductCreationException {

        Product product = new Product();
        product.setName(name);
        product.setDate(date);

        product.setImage(image);
        List<MarketingQuestion> marketingQuestionList = new ArrayList<>();
        for (String q:questions){
            MarketingQuestion marketingQuestion = new MarketingQuestion();
            marketingQuestion.setProduct(product);
            marketingQuestion.setQuestion(q);
            marketingQuestionList.add(marketingQuestion);
        }
        product.setMarketingQuestionsList(marketingQuestionList);

        try {
            em.persist(product);
            em.flush();
        } catch (Exception e){
            throw new ProductCreationException(e.getMessage());
        }

    }

}

