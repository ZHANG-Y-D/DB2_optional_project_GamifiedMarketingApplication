package it.polimi.db2.GMA.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name = "Product", schema = "db_gamified_marketing_application")
@NamedQuery(name = "Product.findProductsByDate", query = "SELECT p FROM Product p where p.Date = :date")
@NamedQuery(name = "Product.findProductByName", query = "SELECT p FROM Product p where p.Name = :name")


public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Name;

//    @Temporal(TemporalType.DATE)
    private Date Date;

    private String Image;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "Product")
    List<Questionnaire> questionnaires;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "Product")
    List<MarketingQuestion> marketingQuestionsList;

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
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Date getDate() {
        return this.Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public List<MarketingQuestion> getMarketingQuestionsList() {
        return marketingQuestionsList;
    }

    public void setMarketingQuestionsList(List<MarketingQuestion> marketingQuestionsList) {
        this.marketingQuestionsList = marketingQuestionsList;
    }
}