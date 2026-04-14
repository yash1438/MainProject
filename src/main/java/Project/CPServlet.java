package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CP")
public class CPServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Connection con = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:ORCL", "system", "Yash1438"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        String newPin = request.getParameter("PinNo");
        HttpSession session1 = request.getSession(false);

        try {
            if (session1 != null) {

                UserBean ub = (UserBean) session1.getAttribute("user");

                // -------- UPDATE PIN --------
                PreparedStatement pstmt = con.prepareStatement(
                    "UPDATE hdfc_user_details SET pinno=? WHERE AccountNo=?"
                );
                pstmt.setString(1, newPin);
                pstmt.setLong(2, ub.getAccNo());

                int k = pstmt.executeUpdate();

                if (k > 0) {

                    // -------- SEND EMAIL --------
                    try {
                        sendEmail(ub);
                    } catch (Exception mailEx) {
                        mailEx.printStackTrace(); // don't crash if email fails
                    }

                    RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
                    rd.include(request, response);

                } else {
                    pw.println("<h3 style='color:red;'>PIN Update Failed.</h3>");
                    RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
                    rd.include(request, response);
                }

            } else {
                request.setAttribute("msg", "Session Expired");
                request.getRequestDispatcher("Sessionexpiry.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            pw.println("Error: " + e.getMessage());
        }
    }

    // -------- EMAIL METHOD --------
    private void sendEmail(UserBean ub) throws MessagingException {

        final String sender = "balayaswanthkumarv@gmail.com";
        final String appPwd = "rpvwfwwwsmkzpebl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, appPwd);
            }
        });

        // Date & Time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = now.format(dtf);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ub.getEmail()));

        msg.setSubject("YASH Bank - PIN Change Alert");

        String text =
                "Dear Customer,\n\n" +
                "Your ATM PIN has been changed successfully.\n\n" +
                "Transaction Details\n" +
                "-----------------------------\n" +
                "Account No  : " + ub.getAccNo() + "\n" +
                "Action      : PIN Changed\n" +
                "PIN No:"+ub.getPinNo()+
                "Date & Time : " + dateTime + "\n" +
                "-----------------------------\n\n" +
                "If you did not request this change, " +
                "please contact YASH Bank immediately.\n\n" +
                "Thank you for banking with YASH Bank.";

        msg.setText(text);
        Transport.send(msg);
    }

    @Override
    public void destroy() {
        try {
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}