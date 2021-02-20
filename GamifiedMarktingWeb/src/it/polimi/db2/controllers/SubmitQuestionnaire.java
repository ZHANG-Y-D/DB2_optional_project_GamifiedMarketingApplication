package it.polimi.db2.controllers;

import it.polimi.db2.GMA.services.ProductService;
import it.polimi.db2.GMA.services.QuestionnaireService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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

        String product = request.getParameter("product");

        if (product == null || product.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid query parameters");
            return;
        }

        //Check if this product belongs to current day
        //TODO

        String path = "/WEB-INF/QuestionnaireMarketingSection.html";
//        List<Edition> results;
//        ServletContext servletContext = getServletContext();
//        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
//        results = edService.search(title, author, performer);
//        ctx.setVariable("editions", results);
//        templateEngine.process(path, ctx, response.getWriter());
    }



}