package it.polimi.db2.controllers;

import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.services.ProductService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/GoToQuestionnaireMarketingSection")
public class GoToQuestionnaireMarketingSection extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "it.polimi.db2.GMA.services/ProductService")
    private ProductService pService;

    public GoToQuestionnaireMarketingSection() {
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
        String pathContent = getServletContext().getContextPath() ;
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(pathContent+ "/index.html");
            return;
        }

        String productName = null;
        try {
            productName = StringEscapeUtils.escapeJava(request.getParameter("productName"));
            if (productName == null || productName.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            // for debugging only e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product is invalid");
            return;
        }

        Product product = null;

        try {
            product = pService.findProductByName(productName);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Products resource not found");
                return;
            }

            //Check if the requested product is today's product
            if (!product.getDate().equals(Date.valueOf(LocalDate.now()))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "The requested product is not today's product");
                return;
            }

            //Check if the requested product actual has marketing Question
            if (product.getMarketingQuestionsList().isEmpty()){
                response.sendRedirect(pathContent+ "/HomePage");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get the product");
            return;
        }


        request.getSession().setAttribute("product", product);

        // Redirect to the Home page to insert the product parameters
        String path = "/WEB-INF/QuestionnaireMarketingSection.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("product", product);
        templateEngine.process(path, ctx, response.getWriter());
    }

    public void destroy() {
    }

}
