package it.polimi.db2.GMA.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Date;
import java.util.Base64;
import java.util.List;


@Entity
@Table(name = "Product", schema = "db_gamified_marketing_application")
@NamedQuery(name = "Product.findProductsByDate", query = "SELECT p FROM Product p where p.date = :date")
@NamedQuery(name = "Product.findProductByName", query = "SELECT p FROM Product p where p.name = :name")


public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private Date date;

    @Lob
    private byte[] image;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "Product")
    private List<Questionnaire> questionnaires;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "Product")
    private List<MarketingQuestion> marketingQuestionsList;

    public Product(){

    }

    public List<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImageData() {
        return Base64.getMimeEncoder().encodeToString(image);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<MarketingQuestion> getMarketingQuestionsList() {
        return marketingQuestionsList;
    }

    public void setMarketingQuestionsList(List<MarketingQuestion> marketingQuestionsList) {
        this.marketingQuestionsList = marketingQuestionsList;
    }
}