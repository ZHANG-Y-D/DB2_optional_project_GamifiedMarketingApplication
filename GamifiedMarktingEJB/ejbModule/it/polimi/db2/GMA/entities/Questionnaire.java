package it.polimi.db2.GMA.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;


@Entity
@Table(name = "Questionnaire", schema = "db_gamified_marketing_application")
@NamedQuery(name = "Questionnaire.findAllQuestionnaireOrderByDatetime", query = "SELECT q FROM Questionnaire q order by q.datetime ASC ")


public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Timestamp datetime;

    private Integer age;

    private String sex;

    private String expertiseLevel;


    @ManyToOne
    @JoinColumn(name = "Product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "User")
    private User user;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ContainMarketing",joinColumns = @JoinColumn(name = "IDQues"))
    @MapKeyJoinColumn(name = "IDQuestion")
    @MapKeyJoinColumn(name = "IDProduct",referencedColumnName = "Product")
    @Column(name = "Answer")
    private Map<MarketingQuestion, String> questionAnswerMap;


    public Questionnaire(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getExpertiseLevel() {
        return expertiseLevel;
    }

    public void setExpertiseLevel(String expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<MarketingQuestion, String> getQuestionAnswerMap() {
        return questionAnswerMap;
    }

    public void setQuestionAnswerMap(Map<MarketingQuestion, String> questionAnswerMap) {
        this.questionAnswerMap = questionAnswerMap;
    }
}
