package it.polimi.db2.controllers;

import it.polimi.db2.GMA.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Register")
@MultipartConfig
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.GMA.services/UserService")
	private UserService usrService;

	public Register() {
		super();
	}

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String usrn = null;
		String pwd = null;
		String confirmpwd = null;
		String email = null;
		
		usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
		pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
		confirmpwd = StringEscapeUtils.escapeJava(request.getParameter("confirmpwd"));
		email = StringEscapeUtils.escapeJava(request.getParameter("email"));
		
		if (usrn == null || pwd == null || confirmpwd == null || email == null
				|| usrn.isEmpty() || pwd.isEmpty() || confirmpwd.isEmpty() || email.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Credentials must be not null");
			return;
		}
		
		if (!pwd.equals(confirmpwd)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			response.getWriter().println("Password and confirm password must be the same.");
			return;
		}
		
		//TODO register
	}

	public void destroy() {

	}
}
