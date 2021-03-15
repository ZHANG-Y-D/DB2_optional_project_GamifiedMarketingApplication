package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.MarketingQuestion;
import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;
import it.polimi.db2.GMA.exceptions.AccountBlockedException;
import it.polimi.db2.GMA.exceptions.DeletionQuestionnaireException;
import it.polimi.db2.GMA.exceptions.OtherException;
import it.polimi.db2.GMA.exceptions.QuestionnaireDoubleAnswerException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;


@Stateless
public class QuestionnaireService {

    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public QuestionnaireService() {
    }


    public void cancelAQuestionnaire(Product product, User user) throws QuestionnaireDoubleAnswerException,
                                                AccountBlockedException, OtherException {

        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDatetime(sqlTimestamp);
        questionnaire.setProduct(product);
        questionnaire.setUser(user);

        try {
            em.persist(questionnaire);
            em.flush();
        }catch (Exception e){
            if(e.getMessage().contains("blocked")){
                throw new AccountBlockedException("Your account is blocked");
            }else if((e.getMessage().contains("Duplicate"))) {
                throw new QuestionnaireDoubleAnswerException("You have already answered/canceled for this questionnaire");
            } else {
                throw new OtherException(e.getMessage());
            }
        }


    }

    public void submitAQuestionnaire(Product product, User user,
                                     Integer age, String sex, String expertiseLevel,
                                     Map<MarketingQuestion,String> questionAnswerMap)
            throws QuestionnaireDoubleAnswerException, AccountBlockedException, PersistenceException, OtherException {


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
        }catch (Exception e){
            if(e.getMessage().contains("Your account is blocked")){
                throw new AccountBlockedException("Your account is already blocked");
            }else if((e.getMessage().contains("Duplicate"))) {
                throw new QuestionnaireDoubleAnswerException("You have already answered/canceled for this questionnaire");
            } else {
                throw new OtherException(e.getMessage());
            }
        }

        user = em.find(User.class,user.getId());
        if (user.getBlocked() == TRUE){
            throw new AccountBlockedException("Your submitted answer contain offensive words, " +
                    "your account is blocked");
        }
    }

    public List<Questionnaire> getAllQuestionnaire(){

        List<Questionnaire> questionnaireList = null;

        try {
            questionnaireList = em.createNamedQuery("Questionnaire.findAllQuestionnaireOrderByDatetime",Questionnaire.class)
                    .getResultList();
            for (Questionnaire e : questionnaireList) {
                em.refresh(e);
            }
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
        return questionnaireList;
    }

    public void deleteQuestionnaire(Integer questionnaireID) throws DeletionQuestionnaireException,OtherException {

        try {
            Questionnaire questionnaire = em.find(Questionnaire.class,questionnaireID);
            em.remove(questionnaire);
            em.flush();
        }catch (Exception e){
            if (e.getMessage().contains("Delete only for the preceding date")){
                throw new DeletionQuestionnaireException("Deletion should be possible " +
                        "only for a date preceding the current date.");
            }else {
                throw new OtherException(e.getMessage());
            }
        }

    }
}
