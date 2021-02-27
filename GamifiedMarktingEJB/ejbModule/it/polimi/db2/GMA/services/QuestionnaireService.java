package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.MarketingQuestion;
import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

import it.polimi.db2.GMA.exceptions.*;

import static java.lang.Boolean.TRUE;


@Stateless
public class QuestionnaireService {

    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public QuestionnaireService() {
    }

    public void cancelAQuestionnaire(Product product, User user) throws QuestionnaireDoubleAnswerException, AccountBlockedException {

        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDatetime(sqlTimestamp);
        questionnaire.setProduct(product);
        questionnaire.setUser(user);

        try {
            em.persist(questionnaire);
            em.flush();
        }catch (PersistenceException persistenceException){
            if(persistenceException.getMessage().contains("your account is blocked")){
                throw new AccountBlockedException("Your account is blocked");
            }else if((persistenceException.getMessage().contains("Duplicate"))) {
                throw new QuestionnaireDoubleAnswerException("You have already answered/canceled for this questionnaire");
            } else {
                throw new PersistenceException(persistenceException.getMessage());
            }
        }


    }

    public void submitAQuestionnaire(Product product, User user,
                                     Integer age, String sex, String expertiseLevel,
                                     Map<MarketingQuestion,String> questionAnswerMap)
            throws QuestionnaireDoubleAnswerException, AccountBlockedException {


        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDatetime(sqlTimestamp);
        questionnaire.setProduct(product);
        questionnaire.setUser(user);


        if (age != null && age <= 0){
            age = null;
        }
        if (!sex.equals("M")  && !sex.equals("F")){
            sex = null;
        }
        if (!expertiseLevel.equals("LOW") &&
        !expertiseLevel.equals("MEDIUM") &&
        !expertiseLevel.equals("HIGH")){
            expertiseLevel = null;
        }

        questionnaire.setAge(age);
        questionnaire.setSex(sex);
        questionnaire.setExpertiseLevel(expertiseLevel);

        questionnaire.setQuestionAnswerMap(questionAnswerMap);


        try {
            em.persist(questionnaire);
            em.flush();
        }catch (PersistenceException persistenceException){
            if(persistenceException.getMessage().contains("Your account is blocked")){
                throw new AccountBlockedException("Your account is already blocked");
            }else if((persistenceException.getMessage().contains("Duplicate"))) {
                throw new QuestionnaireDoubleAnswerException("You have already answered/canceled for this questionnaire");
            } else {
                throw new PersistenceException(persistenceException.getMessage());
            }
        }

        user = em.find(User.class,user.getId());
        if (user.getBlocked() == TRUE){
            throw new AccountBlockedException("Your submitted answer contain offensive words, " +
                    "your account is blocked");
        }

    }


}
