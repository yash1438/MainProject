//package Project;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/CB")
//public class CBServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    Connection con;
//
//    public void init(ServletConfig config) throws ServletException {
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            con = DriverManager.getConnection(
//                "jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438"
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void destroy() {
//        try {
//            if (con != null) con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("text/html");
//        try (PrintWriter pw = response.getWriter()) {
//
//            String accNo = request.getParameter("ACCNO");
//
//            PreparedStatement pstmt = con.prepareStatement(
//                "SELECT BALANCE FROM HDFC WHERE ACCNO = ?"
//            );
//            pstmt.setString(1, accNo);
//
//            ResultSet rs = pstmt.executeQuery();
//
//            pw.println("<html><body style=\"background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg'); background-size: cover; text-align:center; color:white;\" > ");
//
//            if (rs.next()) {
//                int balance = rs.getInt("BALANCE");
//                pw.println("<img src='Logo/hdfc-logo.png' alt='HDFC Logo' width='150'><h1><span>HDFC</span> Bank</h1><br><br>");
//                
//                pw.println("<h3 style='color: Drak pink; size:1000%'>Your Balance is: ₹" + balance + "</h3>");
//
//            } else {
//                pw.println("<h3>Invalid Account Number</h3>");
//                RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
//                rd.include(request, response);
//            }
//
//            pw.println("<br><br><a href='ATMScreen.html'><button type='submit' style='padding:10px 20px; border-radius:8px;'>Exit</button></a>");
//            pw.println("</body></html>");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CB")
public class CBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con;

    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        try (PrintWriter pw = response.getWriter()) {

            HttpSession hs = request.getSession(false);
            UserBean ub = (UserBean) hs.getAttribute("user");

            PreparedStatement pstmt = con.prepareStatement(
                "SELECT BALANCE FROM HDFC WHERE ACCNO = ?"
            );
         
            pstmt.setLong(1, ub.getAccNo());
            ResultSet rs = pstmt.executeQuery();

            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Check Balance</title>");
            pw.println("<style>");
            pw.println("body {"
                    + "background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg');"
                    + "background-size: cover;"
                    + "background-repeat: no-repeat;"
                    + "text-align: center;"
                    + "color: white;"
                    + "font-family: 'Segoe UI', sans-serif;"
                    + "}");
            pw.println("header {"
                    + "display: flex;"
                    + "align-items: center;"
                    + "justify-content: center;"
                    + "padding: 20px;"
                    + "}");
            pw.println("header img { width: 100px; height: auto; margin-right: 10px; }");
            pw.println("header h1 { font-size: 48px; margin: 0; color: white; }");
            pw.println("header h1 span { color: #E61C23; }");
            pw.println(".balance { color: #ff1493; font-size: 30px; margin-top: 50px; font-weight: bold; }"); // hot pink color
            pw.println("button {"
                    + "padding: 10px 20px;"
                    + "border-radius: 8px;"
                    + "border: none;"
                    + "background-color: #1632b4;"
                    + "color: white;"
                    + "font-size: 16px;"
                    + "cursor: pointer;"
                    + "}");
            pw.println("button:hover { background-color: #1f4bda; }");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("<header>");
            pw.println("<h1><span>YASH</span> Bank</h1>");
            pw.println("</header>");

            if (rs.next()) {
                int balance = rs.getInt("BALANCE");
                pw.println("<div class='balance'>Your Balance is: ₹" + balance + "</div>");
            } else {
            	
                pw.println("<h3 style='color:red; margin-top:40px;'>Invalid Account Number</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
                rd.include(request, response);
            }
            
            pw.println("<br><br><a href='ATMScreen.html'><button type='submit'>Exit</button></a>");
            
            pw.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
