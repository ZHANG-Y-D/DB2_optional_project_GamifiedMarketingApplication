package it.polimi.db2.controllers;

import it.polimi.db2.GMA.entities.MarketingQuestion;
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
import java.util.Map;


@WebServlet("/SubmitQuestionnaire")
public class SubmitQuestionnaire extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.GMA.services/QuestionnaireService")
    private QuestionnaireService qService;

    public SubmitQuestionnaire() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // If the user is not logged in (not present in session) redirect to the login
        String pathContext = getServletContext().getContextPath() ;
        HttpSession session = request.getSession();

        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(pathContext+ "/index.html");
            return;
        }

        if (session.getAttribute("product") == null) {
            request.getSession().setAttribute("errorMessage", "The product is invalid");
            response.sendRedirect(pathContext + "/HomePage");
            return;
        }

        if (session.getAttribute("questionAnswerMap") == null) {
            request.getSession().setAttribute("errorMessage", "The answer is invalid");
            response.sendRedirect(pathContext + "/HomePage");
            return;
        }

        User user = (User) session.getAttribute("user");
        Product product = (Product) session.getAttribute("product");
        Map<MarketingQuestion,String> questionAnswerMap =
                (Map<MarketingQuestion,String>) session.getAttribute("questionAnswerMap");

        Integer age = null;
        String sex = null;
        String expertiseLevel = null;


        try {
            age = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("Age")));
        }catch (NumberFormatException e){
            age = null;
        }
        sex = StringEscapeUtils.escapeJava(request.getParameter("Sex"));
        expertiseLevel = StringEscapeUtils.escapeJava(request.getParameter("ExpertiseLevel"));

        try {
            qService.submitAQuestionnaire(product,user,age,sex,expertiseLevel,questionAnswerMap);
        } catch (QuestionnaireDoubleAnswerException |
                AccountBlockedException |
                OtherException e){
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(pathContext + "/HomePage");
            return;
        }


        request.getSession().setAttribute("errorMessage",
                "Your questionnaire was successfully submitted");
        response.sendRedirect(pathContext + "/HomePage");
    }



}