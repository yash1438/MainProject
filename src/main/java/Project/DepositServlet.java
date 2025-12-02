// package Project;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/Deposit")
//public class DepositServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//    Connection con = null;
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
//            if (con != null)
//                con.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("text/html");
//        PrintWriter pw = response.getWriter();
//
//        String depositStr = request.getParameter("deposit"); // Deposit amount
//           // Account number[
//
//        try {
//            // 🔹 Validate input
//            if (depositStr == null) {
//                pw.println("<h3 style='color:red; text-align:center;'>⚠ Please enter all fields!</h3>");
//                RequestDispatcher rd = request.getRequestDispatcher("Deposit.html");
//                rd.include(request, response);
//                return;
//            }
//
//            int depositAmount = Integer.parseInt(depositStr);
//            if (depositAmount <= 0) {
//                pw.println("<h3 style='color:red; text-align:center;'>⚠ Deposit amount must be greater than 0!</h3>");
//                RequestDispatcher rd = request.getRequestDispatcher("Deposit.html");
//                rd.include(request, response);
//                return;
//            }
//
//            // 🔹 Update balance
//            PreparedStatement pstmt = con.prepareStatement(
//                "UPDATE HDFC SET BALANCE = BALANCE + ? WHERE ACCNO = ?"
//            );
//            pstmt.setInt(1, depositAmount);
//            ServletContext sc = getServletContext();
//            UserBean ub = (UserBean) sc.getAttribute("user");
//            
//            pstmt.setLong(2, ub.getAccNo());
//
//            int k = pstmt.executeUpdate();
//
//            // 🔹 HTML Response Page
//            pw.println("<html>");
//            pw.println("<head>");
//            pw.println("<title>Deposit Success</title>");
//            pw.println("<style>");
//            pw.println("body {"
//                    + "background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg');"
//                    + "background-size: cover;"
//                    + "background-repeat: no-repeat;"
//                    + "text-align: center;"
//                    + "font-family: 'Segoe UI', sans-serif;"
//                    + "color: white;"
//                    + "}");
//            pw.println("header { display: flex; align-items: center; justify-content: center; padding: 20px; }");
//            pw.println("header img { width: 100px; height: auto; margin-right: 10px; }");
//            pw.println("header h1 { font-size: 48px; margin: 0; color: white; }");
//            pw.println("header h1 span { color: #E61C23; }");
//            pw.println(".message { margin-top: 60px; font-size: 24px; font-weight: bold; color: #ff1493; }");
//            pw.println("button { padding: 10px 20px; border-radius: 8px; border: none; background-color: #1632b4; color: white; font-size: 16px; cursor: pointer; }");
//            pw.println("button:hover { background-color: #1f4bda; }");
//            pw.println("</style>");
//            pw.println("</head>");
//            pw.println("<body>");
//
//            pw.println("<header>");
//            pw.println("<img src='Logo/hdfc-logo.png' alt='HDFC Logo'>");
//            pw.println("<h1><span>HDFC</span> Bank</h1>");
//            pw.println("</header>");
//
//            if (k > 0) {
//                pw.println("<div class='message'>✅ Amount ₹" + depositAmount + " deposited successfully!</div>");
//            } else {
//                pw.println("<div class='message' style='color:red;'>⚠ Invalid Account Number!</div>");
//                RequestDispatcher rd = request.getRequestDispatcher("Deposit.html");
//                rd.include(request, response);
//            }
//
//            pw.println("<br><br><a href='ATMScreen.html'><button type='submit'>Exit</button></a>");
//            pw.println("</body></html>");
//
//        } catch (NumberFormatException e) {
//            pw.println("<h3 style='color:red; text-align:center;'>⚠ Invalid deposit amount!</h3>");
//            RequestDispatcher rd = request.getRequestDispatcher("Deposit.html");
//            rd.include(request, response);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            pw.println("<h3 style='color:red; text-align:center;'>⚠ Database Error: " + e.getMessage() + "</h3>");
//        }
//    }
//}
//
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Deposit")
public class DepositServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con = null;

    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "yaswanth", "1438");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        String depositStr = request.getParameter("deposit");

        try {
            // 🔹 Validate
            if (depositStr == null) {
                pw.println("<h3 style='color:red; text-align:center;'>⚠ Please enter all fields!</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
                rd.include(request, response);
                return;
            }

            int depositAmount = Integer.parseInt(depositStr);

            if (depositAmount <= 0) {
                pw.println("<h3 style='color:red; text-align:center;'>⚠ Deposit amount must be greater than 0!</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
                rd.include(request, response);
                return;
            }

            // 🔹 Get user from session (fix for NPE)
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("user") == null) {
                pw.println("<h3 style='color:red; text-align:center;'>Session expired. Login again.</h3>");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
                rd.include(request, response);
                return;
            }

            UserBean ub = (UserBean) session.getAttribute("user");

            // 🔹 Update balance
            PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE HDFC SET BALANCE = BALANCE + ? WHERE ACCNO = ?");
            pstmt.setInt(1, depositAmount);
            pstmt.setLong(2, ub.getAccNo());

            int k = pstmt.executeUpdate();

            // 🔹 Response HTML
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Deposit Success</title>");
            pw.println("<style>");
            pw.println("body {"
                    + "background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg');"
                    + "background-size: cover;"
                    + "background-repeat: no-repeat;"
                    + "text-align: center;"
                    + "color: white;"
                    + "font-family: Arial;"
                    + "}");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");

            if (k > 0) {
   
          	  PreparedStatement ptmt = con.prepareStatement(
		                 "Insert into  MINIStatement values(?,?,?,?,?)");
          	   ptmt.setString(1, "-");
		       ptmt.setString(2, ""+ub.getAccNo());
		       ptmt.setString(3,"deposit" );
		       ptmt.setString(4, ""+ depositAmount);
		       LocalDateTime now = LocalDateTime.now();
			     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			     String format = now.format(dtf);
		      ptmt.setString(5,format);
		      
		    
		  ptmt.executeUpdate();
           //     pw.println("<h1>₹" + depositAmount + " Deposited Successfully!</h1>");
                RequestDispatcher rs = request.getRequestDispatcher("ATMScreen.html");
                rs.include(request, response);
            } 
            
         } catch (NumberFormatException e) {
            pw.println("<h3 style='color:red; text-align:center;'>⚠ Invalid deposit amount!</h3>");
            RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
            rd.include(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
