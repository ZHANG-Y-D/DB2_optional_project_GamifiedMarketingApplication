package it.polimi.db2.controllers;

import it.polimi.db2.GMA.entities.Product;
import it.polimi.db2.GMA.services.ProductService;
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
import java.util.List;

@WebServlet("/AdminHomePage")
public class AdminHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.GMA.services/ProductService")
    private ProductService pService;

    public AdminHomePage() {
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
        String loginpath = getServletContext().getContextPath() + "/AdminLogin.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("admin") == null) {
            response.sendRedirect(loginpath);
            return;
        }



        // Redirect to the Home page to insert the product parameters
        String path = "/WEB-INF/AdminHomePage.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        if (session.getAttribute("errorMessage") != null){
            ctx.setVariable("errorMsg", session.getAttribute("errorMessage"));
        }
        templateEngine.process(path, ctx, response.getWriter());


        request.getSession().removeAttribute("errorMessage");
    }

    public void destroy() {
    }

}
