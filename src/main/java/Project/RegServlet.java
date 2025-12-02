//package Project;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/registration")
//public class RegServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String fname = request.getParameter("fname");
//        String lname = request.getParameter("lname");
//        String uname = request.getParameter("uname");
//        String pword = request.getParameter("pword");
//        String gender = request.getParameter("gender");
//        int balance = Integer.parseInt(request.getParameter("bal"));
//          
//        // Create user bean (no need to pass accNo, it will be generated)
//        UserBean ub = new UserBean(fname,lname,uname,pword,balance);
//
//        PrintWriter pw = response.getWriter();
//        response.setContentType("text/html");
//
//        int update = new HDFCDAOServlet().registerUser(ub);
//System.out.println("UPDATE VALUE: "+update);
//        pw.println("<html><body bgcolor='skyblue' text='white'><center><form>");
//        if (update > 0) {
//            pw.println("<h1>Registration Successful</h1>");
//        } else {
//            pw.println("<h1>Registration Failed</h1>");
//        }
//        pw.println("<a href='login.html'>Login</a></form></center></body></html>");
//    }
//}
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registration")
public class RegServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String uname = request.getParameter("uname");
        String aahno=request.getParameter("aahno");
        String pword = request.getParameter("pword");
        String gender = request.getParameter("gender");
        int balance = Integer.parseInt(request.getParameter("bal"));

        // ✅ Create user bean with required details
        UserBean ub = new UserBean(fname, lname, uname,aahno, pword, gender, balance);

        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        int update = new HDFCDAOServlet().registerUser(ub);
        System.out.println("UPDATE VALUE: " + update);

        pw.println("<html>");
        pw.println("<head>");
        pw.println("<style>");
        pw.println("body {background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg');background-size: cover;background-repeat: no-repeat; background-position: center;font-family: Arial, sans-serif; color: white;text-align: center;padding-top: 100px;"
                + "}");
        pw.println(".card {"
                + "background: rgba(0,0,0,0.6);"
                + "width: 450px;"
                + "margin: auto;"
                + "padding: 30px;"
                + "border-radius: 15px;"
                + "box-shadow: 0 0 15px rgba(255,255,255,0.4);"
                + "}");
       pw.println(".tick {font-size: 70px;color: #4CAF50;animation: pop 0.8s ease-out;}");
        pw.println("@keyframes pop {0% { transform: scale(0); opacity: 0; }70% { transform: scale(1.2); opacity: 1; }100% { transform: scale(1); } }");
        pw.println("a { display: inline-block; margin-top: 20px; padding: 10px 20px; background: #2196F3; color: white; text-decoration: none; font-size: 20px; border-radius: 8px;  transition: 0.3s; }");
        pw.println("a:hover {background: #0b7dda; transform: scale(1.05);}");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<div class='card'>");
        if (update > 0) {
            pw.println("<div class='tick'>✔</div>");
            pw.println("<h1>Registration Successful!</h1>");
            pw.println("<p>Your account has been created successfully.</p>");
        } else {
            pw.println("<div class='tick' style='color:red;'>✘</div>");
            pw.println("<h1>Registration Failed</h1>");
            pw.println("<p>There was an issue while creating your account.</p>");
        }

        pw.println("<a href='login.html'>Go to Login</a>");

        pw.println("</div>");
        pw.println("</body>");
        pw.println("</html>");

    }
}
