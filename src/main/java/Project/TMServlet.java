package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/TM")
public class TMServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con = null;

    // --------------------- DB CONNECTION ---------------------
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:ORCL", "system", "Yash1438");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------- CLOSE CONNECTION ---------------------
    public void destroy() {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --------------------- TRANSFER LOGIC ---------------------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String receiverAccNo = request.getParameter("receiver");
        String amountStr = request.getParameter("amount");
        String pword = request.getParameter("pword"); // ✅ fixed

        double amount = Double.parseDouble(amountStr);

        try {

            con.setAutoCommit(false);

            // --------------------- AUTHENTICATION ---------------------
            PreparedStatement psPin = con.prepareStatement(
                    "SELECT accno, balance FROM hdfc WHERE pword = ?");

            psPin.setString(1, pword); // ✅ fixed
            ResultSet rsPin = psPin.executeQuery();

            if (!rsPin.next()) {
                out.println("<h3 style='color:red;'>Invalid Password!</h3>");
                con.rollback();
                return;
            }

            String senderAccNo = rsPin.getString("accno");
            double senderBalance = rsPin.getDouble("balance");

            // --------------------- RECEIVER CHECK ---------------------
            PreparedStatement psRec = con.prepareStatement(
                    "SELECT balance FROM hdfc WHERE accno = ?");

            psRec.setString(1, receiverAccNo);
            ResultSet rsRec = psRec.executeQuery();

            if (!rsRec.next()) {
                out.println("<h3 style='color:red;'>Receiver Account Not Found!</h3>");
                con.rollback();
                return;
            }

            // --------------------- BALANCE CHECK ---------------------
            if (senderBalance < amount) {
                out.println("<h3 style='color:red;'>Insufficient Balance!</h3>");
                con.rollback();
                return;
            }

            // --------------------- DEDUCT AMOUNT ---------------------
            PreparedStatement psDeduct = con.prepareStatement(
                    "UPDATE hdfc SET balance = balance - ? WHERE accno = ?");

            psDeduct.setDouble(1, amount);
            psDeduct.setString(2, senderAccNo);
            psDeduct.executeUpdate();

            // --------------------- ADD AMOUNT ---------------------
            PreparedStatement psAdd = con.prepareStatement(
                    "UPDATE hdfc SET balance = balance + ? WHERE accno = ?");

            psAdd.setDouble(1, amount);
            psAdd.setString(2, receiverAccNo);
            psAdd.executeUpdate();

            // --------------------- MINI STATEMENT ---------------------
            PreparedStatement ptmt = con.prepareStatement(
                    "INSERT INTO MINIStatement VALUES(?,?,?,?,?)");

            ptmt.setString(1, senderAccNo);
            ptmt.setString(2, receiverAccNo);
            ptmt.setString(3, "TRANSFER");
            ptmt.setString(4, String.valueOf(amount));

            // ✅ DATE FIX (NO MORE ORA-01861)
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ptmt.setTimestamp(5, timestamp);

			ptmt.executeUpdate();

            // --------------------- COMMIT ---------------------
            con.commit();

            // --------------------- GET NEW BALANCE ---------------------
            PreparedStatement psNewBal = con.prepareStatement(
                    "SELECT balance FROM hdfc WHERE accno = ?");

            psNewBal.setString(1, senderAccNo);
            ResultSet rsNewBal = psNewBal.executeQuery();

            double newBalance = 0;
            if (rsNewBal.next()) {
                newBalance = rsNewBal.getDouble("balance");
            }

            // --------------------- SUCCESS OUTPUT ---------------------
            out.println("<h2 style='color:green;'>₹" + amount + " transferred successfully!</h2>");
            out.println("<h3>Sender Account: " + senderAccNo + "</h3>");
            out.println("<h3>Previous Balance: ₹" + senderBalance + "</h3>");
            out.println("<h3>Current Balance: ₹" + newBalance + "</h3>");
            out.println("<a href='ATMScreen.html'><input type='button' value='Back'></a>");

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            out.println("<h3 style='color:red;'>Transaction Failed!</h3>");
        }
    }
}