package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/SendOTP")
public class SendOTP extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter pw = response.getWriter();

        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            pw.println("Enter valid email");
            return;
        }

        // Generate OTP
        int otp = 100000 + new Random().nextInt(900000);

        // Store in session
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("otpEmail", email);

        try {
            sendEmail(email, otp);
            pw.println("OTP sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("Failed to send OTP");
        }
    }

    private void sendEmail(String toEmail, int otp) throws MessagingException {

        final String sender = "balayaswanthkumarv@gmail.com";
        final String appPwd = "rpvwfwwwsmkzpebl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, appPwd);
                    }
                });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));

        msg.setSubject("YASH Bank Email OTP");

        msg.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");

        Transport.send(msg);
    }
}