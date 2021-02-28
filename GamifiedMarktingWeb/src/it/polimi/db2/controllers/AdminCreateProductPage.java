package it.polimi.db2.controllers;

import it.polimi.db2.GMA.exceptions.ProductCreationException;
import it.polimi.db2.GMA.services.ProductService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/AdminCreateProductPage")
@MultipartConfig
public class AdminCreateProductPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.GMA.services/ProductService")
    private ProductService pService;

    public AdminCreateProductPage() {
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


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        // If admin user is not logged in (not present in session) redirect to the login
        String pathContext = getServletContext().getContextPath();
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("admin") == null) {
            response.sendRedirect(pathContext+ "/AdminLogin.html");
            return;
        }

        String name;
        Date date;
        Part filePart;
        List<String> questionList = new ArrayList<>();
        InputStream imageStream = null;


        try {
            name = StringEscapeUtils.escapeJava(request.getParameter("ProductName"));
            date = Date.valueOf(StringEscapeUtils.escapeJava(request.getParameter("Date")));
            filePart = request.getPart("image");

            String contentType = filePart.getContentType();
            imageStream = filePart.getInputStream();


            if (name == null || name.isEmpty() || date == null ||
                    contentType == null || imageStream.available()<=0) {
                throw new Exception("No empty filed!");
            }

            if (!contentType.contains("jpg")
                    && !contentType.contains("jpeg")
                    && !contentType.contains("png")){
                throw new Exception("You can only upload jpg/jpeg or png image");
            }

            for (int i = 1; ; i++) {
                String question = StringEscapeUtils.escapeJava(request.getParameter("question" + i));
                if (question == null || question.isEmpty()) {
                    break;
                }
                questionList.add(question);
            }
            if (questionList.isEmpty()){
                throw new Exception("You have to input at least one question!");
            }
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        int imageLength = (int) filePart.getSize();
        byte[] bytesImage = new byte[imageLength];
        imageStream.read(bytesImage);
        imageStream.close();


        try {
            pService.createAProduct(name,date,bytesImage,questionList);
        }catch (ProductCreationException e){
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(pathContext + "/AdminCreateProductPage");
            return;
        }


        request.getSession().setAttribute("errorMessage", "You successfully created this product");
        response.sendRedirect(pathContext + "/AdminCreateProductPage");

    }

    public void destroy() {
    }

}
