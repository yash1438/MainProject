package Project;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet("/registration")
public class RegServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // -------- GET SESSION --------
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("otp") == null) {
            pw.println("<h2 style='color:red;text-align:center;'>Email not verified. Please get OTP first.</h2>");
            return;
        }

        // -------- GET FORM DATA --------
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String uname = request.getParameter("uname");
        String aahno = request.getParameter("aahno");
        String pword = request.getParameter("pword");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String enteredOtpStr = request.getParameter("otp");

        int balance = 0;
        if (request.getParameter("bal") != null) {
            balance = Integer.parseInt(request.getParameter("bal"));
        }

        // -------- OTP VALIDATION --------
        int sessionOtp = (int) session.getAttribute("otp");
        String sessionEmail = (String) session.getAttribute("otpEmail");

        int enteredOtp = Integer.parseInt(enteredOtpStr);

        if (enteredOtp != sessionOtp || !email.equals(sessionEmail)) {
            pw.println("<h2 style='color:red;text-align:center;'>Invalid OTP or Email not verified</h2>");
            return;
        }

        // OTP verified → remove from session
        session.removeAttribute("otp");
        session.removeAttribute("otpEmail");

        // -------- SAVE USER --------
        UserBean ub = new UserBean(fname, lname, uname, aahno, pword, gender, balance, email);
        int update = new HDFCDAOServlet().registerUser(ub);

        pw.println("<html><body style='text-align:center; background:#2c3e50; color:white; font-family:Arial; padding-top:50px;'>");

        if (update > 0) {
            pw.println("<div style='border:1px solid #27ae60; display:inline-block; padding:20px; border-radius:10px;'>");
            pw.println("<h1>✔ Registration Successful!</h1>");
            pw.println("<p>Account Number: <b>" + ub.getAccNo() + "</b></p>");

            try {
                sendEmail(ub);
                pw.println("<p style='color:#2ecc71;'>Welcome email sent to: " + email + "</p>");
            } catch (Exception e) {
                pw.println("<p style='color:#e67e22;'>Registration saved, but Email failed.</p>");
                e.printStackTrace();
            }

            pw.println("</div>");
        } else {
            pw.println("<h1 style='color:#e74c3c;'>✘ Registration Failed</h1>");
        }

        pw.println("<br><br><a href='login.html' style='color:cyan;'>Go to Login</a>");
        pw.println("</body></html>");
    }

    // -------- WELCOME EMAIL --------
    private void sendEmail(UserBean ub) throws MessagingException {

        final String sender = "balayaswanthkumarv@gmail.com";
        final String appPwd = "rpvwfwwwsmkzpebl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, appPwd);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ub.getEmail()));

        msg.setSubject("YASH Bank - Registration Success");

        msg.setText(
                "Hi " + ub.getFname() + ",\n\n" +
                "Your account has been created successfully.\n\n" +
                "Account Number: " + ub.getAccNo() + "\n" +
                "Password: " + ub.getPword() + "\n" +
                "PIN: " + ub.getPinNo() + "\n\n" +
                "Welcome to YASH Bank."
        );

        Transport.send(msg);
    }
}