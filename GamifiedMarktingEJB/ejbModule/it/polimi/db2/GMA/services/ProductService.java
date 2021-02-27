package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.exceptions.ProductCreationException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
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

    public void createAProduct(String name, Date date, File image, List<String> questions) throws ProductCreationException {
//
//        Product product = new Product();
//        product.setName(name);
//        product.setDate(date);
//
//        byte[] bytes;
//
////        bytes = org.apache.commons.io.FileUtils.readFileToByteArray(image);
//
//        bytes=null;
//        product.setImage(bytes);
//        List<MarketingQuestion> marketingQuestionList = new ArrayList<>();
//        for (String q:questions){
//            MarketingQuestion marketingQuestion = new MarketingQuestion();
//            marketingQuestion.setProduct(product);
//            marketingQuestion.setQuestion(q);
//            marketingQuestionList.add(marketingQuestion);
//        }
//        product.setMarketingQuestionsList(marketingQuestionList);
//
//        try {
//            em.persist(product);
//            em.flush();
//        } catch (PersistenceException e){
//            throw new ProductCreationException(e.getMessage());
//        }

    }

}

