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

    private String Question;

    @ManyToOne
    @JoinColumn(name = "Product")
    private Product Product;

    @ElementCollection
    @CollectionTable(name = "ContainMarketing",joinColumns = @JoinColumn(name = "IDQuestion"))
    @MapKeyJoinColumn(name = "IDQues")
    @Column(name = "Answer")
    private Map<Questionnaire, String> questionnaireAnswerMap;




    public MarketingQuestion() {

    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
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
        return Product;
    }

    public void setProduct(Product product) {
        Product = product;
    }
}