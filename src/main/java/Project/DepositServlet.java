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

@WebServlet("/Deposit")
public class DepositServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Connection con = null;

    // ---------- DB CONNECTION ----------
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "yaswanth",
                    "143812");
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

    // ---------- DEPOSIT LOGIC ----------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        String depositStr = request.getParameter("deposit");

        try {
            // Validate input
            if (depositStr == null || depositStr.isEmpty()) {
                pw.println("<h3 style='color:red;text-align:center;'>Enter amount!</h3>");
                request.getRequestDispatcher("ATMScreen.html").include(request, response);
                return;
            }

            int depositAmount = Integer.parseInt(depositStr);

            if (depositAmount <= 0) {
                pw.println("<h3 style='color:red;text-align:center;'>Invalid amount!</h3>");
                request.getRequestDispatcher("ATMScreen.html").include(request, response);
                return;
            }

            // Session check
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                request.setAttribute("msg", "Session Expired");
                request.getRequestDispatcher("Sessionexpiry.jsp").forward(request, response);
                return;
            }

            UserBean ub = (UserBean) session.getAttribute("user");

            // ---------- UPDATE BALANCE ----------
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE HDFC SET BALANCE = BALANCE + ? WHERE ACCNO=?");
            ps.setInt(1, depositAmount);
            ps.setLong(2, ub.getAccNo());

            int k = ps.executeUpdate();

            pw.println("<html><body style='text-align:center;font-family:Arial;'>");
//            pw.println("<h1>YASH Bank</h1>");

            if (k > 0) {

                // Get Updated Balance
                PreparedStatement psBal = con.prepareStatement(
                        "SELECT BALANCE FROM HDFC WHERE ACCNO=?");
                psBal.setLong(1, ub.getAccNo());
                ResultSet rsBal = psBal.executeQuery();

                int newBalance = 0;
                if (rsBal.next()) {
                    newBalance = rsBal.getInt("BALANCE");
                }

                // Date & Time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTime = now.format(dtf);

                // Insert Mini Statement
                PreparedStatement ptmt = con.prepareStatement(
                        "INSERT INTO MINISTATEMENT VALUES(?,?,?,?,?)");
                ptmt.setString(1, "-");
                ptmt.setString(2, String.valueOf(ub.getAccNo()));
                ptmt.setString(3, "Deposit");
                ptmt.setString(4, String.valueOf(depositAmount));
                ptmt.setString(5, dateTime);
                ptmt.executeUpdate();

//                pw.println("<h2 style='color:green;'>Deposit Successful</h2>");
//                pw.println("<h3>Amount: ₹" + depositAmount + "</h3>");
//                pw.println("<h3>Available Balance: ₹" + newBalance + "</h3>");

                // ---------- SEND EMAIL ----------
                try {
                    sendEmail(ub, depositAmount, newBalance, dateTime);
                } catch (Exception mailEx) {
                    mailEx.printStackTrace();
                }

            } else {
                pw.println("<h3 style='color:red;'>Deposit Failed</h3>");
            }

            pw.println("</body></html>");

            RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
            rd.include(request, response);

        } catch (NumberFormatException e) {
            pw.println("<h3 style='color:red;text-align:center;'>Invalid amount!</h3>");
            request.getRequestDispatcher("ATMScreen.html").include(request, response);
        } catch (Exception e) {
            e.printStackTrace();
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

        // Fix PKIX / SSL error
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

        msg.setSubject("YASH Bank Deposit Alert");

        String text =
                "Dear Customer,\n\n" +
                "Transaction Details\n" +
                "-----------------------------\n" +
                "Account No : " + ub.getAccNo() + "\n" +
                "Transaction : Deposit\n" +
                "Amount      : ₹" + amount + "\n" +
                "Date & Time : " + dateTime + "\n" +
                "Balance     : ₹" + balance + "\n" +
                "-----------------------------\n\n" +
                "Thank you for banking with YASH Bank.";

        msg.setText(text);

        Transport.send(msg);
    }
}