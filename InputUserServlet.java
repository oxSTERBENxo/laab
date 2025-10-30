package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "InputUserServlet", urlPatterns = "")
public class InputUserServlet extends HttpServlet {

    private final SpringTemplateEngine springTemplateEngine;

    // Spring Boot will inject the template engine
    public InputUserServlet(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    // Show the page with the input form
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        // If already logged in, skip directly to /listBooks
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect("/listBooks");
            return;
        }

        springTemplateEngine.process("inputName.html", context, resp.getWriter());
    }

    // Handle form submission
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");

        if (name == null || name.trim().isEmpty()) {
            IWebExchange webExchange = JakartaServletWebApplication
                    .buildApplication(getServletContext())
                    .buildExchange(req, resp);
            WebContext context = new WebContext(webExchange);
            context.setVariable("error", "Внеси име!");
            springTemplateEngine.process("inputName.html", context, resp.getWriter());
            return;
        }

        // Save the user in session
        HttpSession session = req.getSession();
        session.setAttribute("user", name);

        // Redirect to books page (the reservation start page)
        resp.sendRedirect("/listBooks");
    }
}
