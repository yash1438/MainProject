package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/WD")
public class WDServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con;

    // ---------- DB CONNECTION ----------
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:ORCL",
                    "system",
                    "Yash1438"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (con != null)
                con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- WITHDRAW LOGIC ----------
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        try {
            int withdrawAmount = Integer.parseInt(request.getParameter("withdraw"));

            HttpSession hs = request.getSession(false);
            UserBean ub = (UserBean) hs.getAttribute("user");

            con.setAutoCommit(false);

            // Check Balance
            PreparedStatement ps = con.prepareStatement(
                    "SELECT BALANCE FROM HDFC WHERE ACCNO=?");
            ps.setLong(1, ub.getAccNo());
            ResultSet rs = ps.executeQuery();

            pw.println("<html><body style='text-align:center;font-family:Arial;'>");
//            pw.println("<h1>YASH Bank</h1>");

            if (rs.next()) {
                int balance = rs.getInt("BALANCE");

                if (balance >= withdrawAmount) {

                    int newBalance = balance - withdrawAmount;

                    // Update Balance
                    PreparedStatement ps2 = con.prepareStatement(
                            "UPDATE HDFC SET BALANCE=? WHERE ACCNO=?");
                    ps2.setInt(1, newBalance);
                    ps2.setLong(2, ub.getAccNo());

                    int updated = ps2.executeUpdate();

                    if (updated > 0) {

                        // Date & Time
                    	LocalDateTime now = LocalDateTime.now();
                    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    	timestamp.setNanos(now.getNano());
                    	
                    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    	String dateTime = now.format(dtf);
                    	

                        // Insert Mini Statement
//                        PreparedStatement ps3 = con.prepareStatement(
//                                "INSERT INTO MINISTATEMENT VALUES(?,ministatement_seq.NEXTVAL,?,?,?)");
//                        ps3.setString(1, String.valueOf(ub.getAccNo()));
//                        ps3.setString(2, "-");
//                        ps3.setString(3, "Withdraw");
//                        ps3.setString(4, String.valueOf(withdrawAmount));
//                        ps3.setTimestamp(5, timestamp);
//                        ps3.executeUpdate();
//                        con.commit();
                    	PreparedStatement ps3 = con.prepareStatement(
                    		    "INSERT INTO MINISTATEMENT(TID, ACCNO, TRANTYPE, AMOUNT, TRANDATE) " +
                    		    "VALUES(ministatement_seq.NEXTVAL, ?, ?, ?, ?)");
                    		ps3.setString(1, String.valueOf(ub.getAccNo()));   // ACCNO
                    		ps3.setString(2, "Withdraw");                      // TRANTYPE
                    		ps3.setString(3, String.valueOf(withdrawAmount));  // AMOUNT
                    		ps3.setTimestamp(4, timestamp);                    // TRANDATE
                    		ps3.executeUpdate();

//                        pw.println("<h2 style='color:green;'>Withdrawal Successful</h2>");
//                        pw.println("<h3>Amount: ₹" + withdrawAmount + "</h3>");
//                        pw.println("<h3>Available Balance: ₹" + newBalance + "</h3>");

                        // ---------- SEND EMAIL ----------
                        try {
                            sendEmail(ub, withdrawAmount, newBalance, dateTime);
                        } catch (Exception mailEx) {
                            mailEx.printStackTrace();
                        }

                    } else {
                        con.rollback();
                        pw.println("<h3 style='color:red;'>Transaction Failed</h3>");
                    }

                } else {
                    pw.println("<h3 style='color:red;'>Insufficient Balance</h3>");
                }

            } else {
                pw.println("<h3 style='color:red;'>Invalid Account</h3>");
            }

            pw.println("</body></html>");

            RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
            rd.include(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ex) {
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
    }

    // ---------- EMAIL METHOD ----------
    private void sendEmail(UserBean ub, int amount, int balance, String dateTime)
            throws MessagingException {

        final String sender = "balayaswanthkumarv@gmail.com";
        final String appPwd = "rpvwfwwwsmkzpebl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // IMPORTANT FIX
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, appPwd);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ub.getEmail()));

        msg.setSubject("YASH Bank Withdrawal Alert");

        String text =
                "Account No : " + ub.getAccNo() + "\n" +
                "Transaction : Withdraw\n" +
                "Amount : ₹" + amount + "\n" +
                "Date & Time : " + dateTime + "\n" +
                "Balance : ₹" + balance;

        msg.setText(text);

        Transport.send(msg);
    }
}