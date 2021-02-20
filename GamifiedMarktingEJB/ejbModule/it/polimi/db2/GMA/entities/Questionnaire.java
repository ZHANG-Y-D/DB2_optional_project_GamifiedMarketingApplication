package it.polimi.db2.GMA.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;


@Entity
@Table(name = "Questionnaire", schema = "db_gamified_marketing_application")

public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Timestamp Datetime;

    private Integer Age;

    private String Sex;

    private String ExpertiseLevel;


    @ManyToOne
    @JoinColumn(name = "Product")
    private Product Product;

    @ManyToOne
    @JoinColumn(name = "User")
    private User User;


//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "ContainMarketing",joinColumns = @JoinColumn(name = "IDQues"))
//    @MapKeyJoinColumn(name = "MarketingQuestion")
//    @Column(name = "IDQuestion")
//    private Map<MarketingQuestion, String> Question;

    public Questionnaire(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Timestamp getDatetime() {
        return Datetime;
    }

    public void setDatetime(Timestamp datetime) {
        Datetime = datetime;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getExpertiseLevel() {
        return ExpertiseLevel;
    }

    public void setExpertiseLevel(String expertiseLevel) {
        ExpertiseLevel = expertiseLevel;
    }

    public Product getProduct() {
        return Product;
    }

    public void setProduct(Product product) {
        Product = product;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }
}
