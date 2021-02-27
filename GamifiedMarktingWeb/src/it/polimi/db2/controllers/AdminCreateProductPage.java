package it.polimi.db2.controllers;

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


@WebServlet("/AdminCreateProductPage")
public class AdminCreateProductPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.GMA.services/ProductService")
    private ProductService pService;

    public AdminCreateProductPage() {
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



        Integer count = null;
        try {
            count = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("count")));
        }catch (NumberFormatException e){
            count = null;
        }
        if (count == null ) {
            count = 1;
        } else {
            count = count + 1;
        }


        // Redirect to the Home page to insert the product parameters
        String path = "/WEB-INF/AdminCreateProductPage.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("count", count);
        if (session.getAttribute("errorMessage") != null){
            ctx.setVariable("errorMsg", session.getAttribute("errorMessage"));
        }
        templateEngine.process(path, ctx, response.getWriter());

        request.getSession().removeAttribute("errorMessage");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


//        // If admin user is not logged in (not present in session) redirect to the login
//        String pathContext = getServletContext().getContextPath();
//        HttpSession session = request.getSession();
//        if (session.isNew() || session.getAttribute("admin") == null) {
//            response.sendRedirect(pathContext+ "/AdminLogin.html");
//            return;
//        }
//
//        String name;
//        Date date;
//        File image;
//        List<String> questionList = new ArrayList<>();
//
//        try {
//            name = StringEscapeUtils.escapeJava(request.getParameter("ProductName"));
//            date = Date.valueOf(StringEscapeUtils.escapeJava(request.getParameter("Date")));
//            image = new File(request.getParameter("image"));
//            if (name == null || name.isEmpty() ||
//                    date == null || image == null) {
//                throw new Exception("No empty filed!");
//            }
//
//            if (!image.getName().contains("jpg") && !image.getName().contains("png")){
//                throw new Exception("You can only upload jpg or png image");
//            }
//
//            for (int i = 1; ; i++) {
//                String question = StringEscapeUtils.escapeJava(request.getParameter("question" + i));
//                if (question == null || question.isEmpty()) {
//                    break;
//                }
//                questionList.add(question);
//            }
//            if (questionList.isEmpty()){
//                throw new Exception("You have to input at least one question!");
//            }
//        }catch (Exception e){
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//            return;
//        }
//
//
//        try {
//            pService.createAProduct(name,date,image,questionList);
//        }catch (ProductCreationException e){
//            request.getSession().setAttribute("errorMessage", e.getMessage());
//            response.sendRedirect(pathContext + "/AdminCreateProductPage");
//            return;
//        }
//
//
//        request.getSession().setAttribute("errorMessage", "You successfully created this product");
//        response.sendRedirect(pathContext + "/AdminCreateProductPage");

    }

    public void destroy() {
    }

}
