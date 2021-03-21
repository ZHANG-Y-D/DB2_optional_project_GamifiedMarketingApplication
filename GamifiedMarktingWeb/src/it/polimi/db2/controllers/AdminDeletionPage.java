package it.polimi.db2.controllers;

import it.polimi.db2.GMA.entities.Questionnaire;
import it.polimi.db2.GMA.entities.User;
import it.polimi.db2.GMA.exceptions.DeletionQuestionnaireException;
import it.polimi.db2.GMA.exceptions.OtherException;
import it.polimi.db2.GMA.services.ProductService;
import it.polimi.db2.GMA.services.QuestionnaireService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
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
import java.sql.Date;
import java.util.List;

@WebServlet("/AdminDeletionPage")
public class AdminDeletionPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.GMA.services/QuestionnaireService")
    private QuestionnaireService qService;

    public AdminDeletionPage() {
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


        // If admin user is not logged in (not present in session) redirect to the login
        String pathContext = getServletContext().getContextPath();
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("admin") == null) {
            response.sendRedirect(pathContext+ "/AdminLogin.html");
            return;
        }

        String path = "/WEB-INF/AdminDeletionPage.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        Integer questionnaireID;
        String errorMessage = null;

        try {
            questionnaireID = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("questionnaireID")));
        }catch (NumberFormatException e){
            questionnaireID = null;
        }

        if (questionnaireID != null && questionnaireID >= 0) {
            try {
                qService.deleteQuestionnaire(questionnaireID);
            } catch (DeletionQuestionnaireException | OtherException e) {
                ctx.setVariable("errorMsg", e.getMessage());
                templateEngine.process(path, ctx, response.getWriter());
                return;
            }
            errorMessage = "Deleted questionnaire successfully!";
        }

        List<Questionnaire> questionnaireList;

        try {
            questionnaireList = qService.getAllQuestionnaire();
        }catch (PersistenceException e){
            ctx.setVariable("errorMsg", e.getMessage());
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        if (questionnaireList.isEmpty()){
            ctx.setVariable("errorMsg", "No questionnaire yet");
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        ctx.setVariable("errorMsg",errorMessage);
        ctx.setVariable("questionnaireList", questionnaireList);
        templateEngine.process(path, ctx, response.getWriter());
    }



    public void destroy() {
    }

}
