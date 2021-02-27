package it.polimi.db2.GMA.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.GMA.entities.Administrator;
import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;
import it.polimi.db2.GMA.exceptions.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials",
					User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}

	public List<User> findUsersAnsweredQuestionsOnADay(Date date) throws PersistenceException {
		List<User> uList = null;
		List<User> todayAnsweredUsers = new ArrayList<>();

		try {
			uList = em.createNamedQuery("User.findAllUsersDescByPoints",User.class).getResultList();
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		for (User u: uList){
			for (Questionnaire q: u.getQuestionnaires()){
				Date quesDate = Date.valueOf(q.getDatetime().toLocalDateTime().toLocalDate());
				Date dateToday = Date.valueOf(LocalDate.now());
				if (quesDate.equals(dateToday)) {
					todayAnsweredUsers.add(u);
					break;
				}
			}
		}
		return todayAnsweredUsers;
	}

	public Administrator checkAdminCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<Administrator> adminList = null;
		try {
			adminList = em.createNamedQuery("Admin.checkCredentials",
					Administrator.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		if (adminList.isEmpty())
			return null;
		else if (adminList.size() == 1)
			return adminList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}
}
