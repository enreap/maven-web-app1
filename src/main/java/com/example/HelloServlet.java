package com.example;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        response.getWriter().println("<h1>Welcome to Enreap DevOps Team! 🚀</h1>");
        response.getWriter().println("<img src='images/logo.png' width='200'/>");
    }
}
