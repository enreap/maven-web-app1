package com.example.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CompanyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set company info
        request.setAttribute("companyName", "Acme Corporation");
        request.setAttribute("companyAddress", "123 Main Street, Springfield, USA");
        request.setAttribute("logoPath", "images/logo.png");

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }
}
