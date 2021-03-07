package it.polimi.db2.controllers;


import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.entities.User;
import it.polimi.db2.GMA.exceptions.AccountBlockedException;
import it.polimi.db2.GMA.exceptions.OtherException;
import it.polimi.db2.GMA.exceptions.QuestionnaireDoubleAnswerException;
import it.polimi.db2.GMA.services.QuestionnaireService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CancelQuestionnaire")
public class CancelQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "it.polimi.db2.GMA.services/QuestionnaireService")
    private QuestionnaireService qService;


    public CancelQuestionnaire() {
        super();
    }

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // If the user is not logged in (not present in session) redirect to the login
        String pathContext = getServletContext().getContextPath() ;
        HttpSession session = request.getSession();

        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(pathContext+ "/index.html");
            return;
        }

        if (session.getAttribute("product") == null) {
            response.sendRedirect(pathContext + "/HomePage");
            return;
        }
        Product product = (Product) session.getAttribute("product");
        User user = (User) session.getAttribute("user");


        try{
            qService.cancelAQuestionnaire(product,user);
        }catch (QuestionnaireDoubleAnswerException |
                AccountBlockedException |
                OtherException e){
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(pathContext + "/HomePage");
            return;
        }

        response.sendRedirect(pathContext + "/HomePage");

    }

    public void destroy() {
    }

}
