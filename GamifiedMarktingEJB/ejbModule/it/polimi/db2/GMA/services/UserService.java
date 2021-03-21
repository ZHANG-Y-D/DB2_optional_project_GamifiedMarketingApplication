package it.polimi.db2.GMA.services;

import it.polimi.db2.GMA.entities.Administrator;
import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;
import it.polimi.db2.GMA.exceptions.CredentialsException;
import it.polimi.db2.GMA.exceptions.RegisterException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedMarketingEJB")
	private EntityManager em;

	public UserService() {
	}

	public void registerNewClient (String usrn, String pwd, String email) throws RegisterException {

		User user = new User();

		user.setUsername(usrn);
		user.setPassword(pwd);
		user.setEmail(email);
		user.setBlocked(false);
		user.setPoint(0);

		try {
			em.persist(user);
			em.flush();
		} catch (Exception e){
			throw new RegisterException(e.getMessage());
		}

	}



	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class)
					.setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
			for (User e : uList) {
				em.refresh(e);
			}
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}

	public Administrator checkAdminCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<Administrator> adminList = null;
		try {
			adminList = em.createNamedQuery("Admin.checkCredentials", Administrator.class)
					.setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
			for (Administrator e : adminList) {
				em.refresh(e);
			}
		} catch (Exception e) {
			throw new CredentialsException("Could not verify credentials");
		}
		if (adminList.isEmpty())
			return null;
		else if (adminList.size() == 1)
			return adminList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}

	public List<User> findUsersAnsweredQuestionsOnADay(Date dateToday) throws PersistenceException {
		List<User> uList = null;
		List<User> todayAnsweredUsers = new ArrayList<>();

		try {
			uList = em.createNamedQuery("User.findAllUsersDescByPoints",User.class)
					.getResultList();
			for (User e : uList) {
				em.refresh(e);
			}
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		for (User u: uList){
			for (Questionnaire q: u.getQuestionnaires()){
				Date quesDate = Date.valueOf(q.getDatetime().toLocalDateTime().toLocalDate());
				if (quesDate.equals(dateToday) && !q.getQuestionAnswerMap().isEmpty()) {
					todayAnsweredUsers.add(u);
					break;
				}
			}
		}
		return todayAnsweredUsers;
	}

	public List<User> findUsersCancelledQuestionnaireOnADay(Date dateToday) throws PersistenceException {
		List<User> uList = null;
		List<User> thisDayCancelledUsers = new ArrayList<>();

		try {
			uList = em.createNamedQuery("User.findAllUsersDescByPoints",User.class)
					.getResultList();
			for (User e : uList) {
				em.refresh(e);
			}
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
		for (User u: uList){
			for (Questionnaire q: u.getQuestionnaires()){
				Date quesDate = Date.valueOf(q.getDatetime().toLocalDateTime().toLocalDate());
				if (quesDate.equals(dateToday) && q.getQuestionAnswerMap().isEmpty()) {
					thisDayCancelledUsers.add(u);
					break;
				}
			}
		}
		return thisDayCancelledUsers;
	}

	public void toBlockAccount(int userID){

		User user = em.find(User.class,userID);
		user.setBlocked(true);
		em.flush();
	}

}
