package it.polimi.db2.GMA.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;


@Entity
@Table(name = "MarketingQuestion", schema = "db_gamified_marketing_application")

public class MarketingQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;

    @ManyToOne
    @JoinColumn(name = "Product")
    private Product product;

    @ElementCollection
    @CollectionTable(name = "ContainMarketing",joinColumns = @JoinColumn(name = "IDQuestion"),foreignKey = @ForeignKey(name = "IDProduct"))
    @MapKeyJoinColumn(name = "IDQues")
    @MapKeyJoinColumn(name = "IDProduct",referencedColumnName = "Product")
    @Column(name = "Answer")
    private Map<Questionnaire, String> questionnaireAnswerMap;


    public MarketingQuestion() {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Questionnaire, String> getQuestionnaireAnswerMap() {
        return questionnaireAnswerMap;
    }

    public void setQuestionnaireAnswerMap(Map<Questionnaire, String> questionnaireAnswerMap) {
        this.questionnaireAnswerMap = questionnaireAnswerMap;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}