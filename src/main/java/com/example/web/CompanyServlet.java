package com.example.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CompanyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set company info
        request.setAttribute("companyName", "Enreap");
        request.setAttribute("companyAddress", "7th Floor, Marisoft Phase 3, Kalyani Nagar, Pune, India 411014");
        request.setAttribute("logoPath", "images/logo.png");

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }
}
