package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

@Stateless
public class QuestionnaireService {

    @PersistenceContext(unitName = "GamifiedMarketingEJB")
    private EntityManager em;

    public QuestionnaireService() {
    }


    

}
