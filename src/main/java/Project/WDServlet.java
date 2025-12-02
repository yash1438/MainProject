package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

@WebServlet("/WD")
public class WDServlet extends HttpServlet {
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

            
            String amtStr = request.getParameter("withdraw"); // amount to withdraw
            int withdrawAmount = Integer.parseInt(amtStr);

            pw.println("<html><head><title>Withdraw</title>");
            pw.println("<style>");
            pw.println("body {background-image: url('https://wpblogassets.paytm.com/paytmblog/uploads/2021/07/ATM_8_WhatIsAnATMCardAndHowToUseIt-1-800x500.jpg');"
                    + "background-size: cover;"
                    + "background-repeat: no-repeat;"
                    + "text-align: center;"
                    + "color: white;"
                    + "font-family: 'Segoe UI', sans-serif;}");
            pw.println("header {display: flex; align-items: center; justify-content: center; padding: 20px;}");
            pw.println("header img { width: 100px; height: auto; margin-right: 10px; }");
            pw.println("header h1 { font-size: 48px; margin: 0; color: white; }");
            pw.println("header h1 span { color: #E61C23; }");
            pw.println(".balance { color: #ff1493; font-size: 30px; margin-top: 50px; font-weight: bold; }");
            pw.println("h3 { color: red; margin-top: 40px; }");
            pw.println("button {padding: 10px 20px; border-radius: 8px; border: none; background-color: #1632b4; color: white; font-size: 16px; cursor: pointer;}");
            pw.println("button:hover { background-color: #1f4bda; }");
            pw.println("</style></head><body>");

            pw.println("<header>");
            pw.println("<img src='Logo/hdfc-logo.png' alt='HDFC Logo'>");
            pw.println("<h1><span>HDFC</span> Bank</h1>");
            pw.println("</header>");

            con.setAutoCommit(false); // start transaction
            HttpSession hs = request.getSession(false);
            UserBean ub= (UserBean) hs.getAttribute("user");
            PreparedStatement psCheck = con.prepareStatement(
                    "SELECT BALANCE FROM HDFC WHERE ACCNO = ?");
            psCheck.setLong(1, ub.getAccNo());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int balance = rs.getInt("BALANCE");

                if (balance >= withdrawAmount) {
                    int newBalance = balance - withdrawAmount;

                    PreparedStatement psUpdate = con.prepareStatement(
                            "UPDATE HDFC SET BALANCE = ? WHERE ACCNO = ?");
                    psUpdate.setInt(1, newBalance);
                    psUpdate.setLong(2, ub.getAccNo());

                    int updated = psUpdate.executeUpdate();

                    if (updated > 0) {
                        con.commit(); // success
                        PreparedStatement ptmt = con.prepareStatement(
       		                 "Insert into  MINIStatement values(?,?,?,?,?)");
                 	   ptmt.setString(1, ""+ub.getAccNo());
       		       ptmt.setString(2, "-");
       		       ptmt.setString(3,"withdraw" );
       		       ptmt.setString(4, ""+ withdrawAmount);
       		       LocalDateTime now = LocalDateTime.now();
       			     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       			     String format = now.format(dtf);
       		      ptmt.setString(5,format);
       		      
       		    
       		  ptmt.executeUpdate();
                        //pw.println("<div class='balance'>Withdrawal Successful! ₹" + withdrawAmount + " withdrawn.</div>");
                       // pw.println("<div class='balance'>New Balance: ₹" + newBalance + "</div>");
                        
                    } else {
                        con.rollback(); // failed update
                        pw.println("<h3>Transaction failed! Please try again.</h3>");
                    }
                } else {
                    pw.println("<h3>Insufficient Balance! Withdrawal Denied.</h3>");
                }
            } else {
                pw.println("<h3>Invalid Account Number!</h3>");
              
            }

            pw.println("</body></html>");
            RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
            rd.include(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback(); // rollback if any exception
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true); // reset auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
