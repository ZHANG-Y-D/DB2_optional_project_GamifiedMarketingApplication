package it.polimi.db2.controllers;

import it.polimi.db2.GMA.entities.MarketingQuestion;
import it.polimi.db2.GMA.entities.Product;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/GoToQuestionnaireStatisticalSection")
public class GoToQuestionnaireStatisticalSection extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    public GoToQuestionnaireStatisticalSection() {
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
        Product product = (Product) session.getAttribute("product");


        //All inputs of the marketing section are mandatory
        Map<MarketingQuestion,String> questionAnswerMap = new HashMap<>();
        for (MarketingQuestion mq : product.getMarketingQuestionsList())
        {
            int questionID = mq.getId();
            String answer = null;

            try {
                answer = StringEscapeUtils.escapeJava(request.getParameter(Integer.toString(questionID)));
                if (answer == null || answer.isEmpty()) {
                    throw new Exception("You have to answer all the marketing question");
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have to answer all the marketing question");
                return;
            }
            questionAnswerMap.put(mq,answer);
        }

        if (questionAnswerMap.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "No marketing question is registered");
            response.sendRedirect(pathContext +"/HomePage");
            return;
        }

        request.getSession().setAttribute("questionAnswerMap", questionAnswerMap);

        String path = "/WEB-INF/QuestionnaireStatisticalSection.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("product", product);
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    public void destroy() {
    }
}
