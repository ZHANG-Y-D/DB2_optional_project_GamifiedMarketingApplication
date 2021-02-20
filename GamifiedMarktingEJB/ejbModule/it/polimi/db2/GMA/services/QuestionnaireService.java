package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Stateless
public class QuestionnaireService {

    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public QuestionnaireService() {
    }

    public void cancelAQuestionnaire(Product product, User user) {

        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setDatetime(sqlTimestamp);
        questionnaire.setProduct(product);
        questionnaire.setUser(user);

        em.persist(questionnaire);
        em.flush();
    }


}
