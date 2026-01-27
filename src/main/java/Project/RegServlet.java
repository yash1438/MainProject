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
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String uname = request.getParameter("uname");
        String aahno = request.getParameter("aahno"); 
        String pword = request.getParameter("pword");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        
        // Handle potential NullPointerException if 'bal' is missing
        int balance = 0;
        if(request.getParameter("bal") != null) {
            balance = Integer.parseInt(request.getParameter("bal"));
        }

        UserBean ub = new UserBean(fname, lname, uname, aahno, pword, gender, balance, email);
        int update = new HDFCDAOServlet().registerUser(ub);

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.println("<html><body style='text-align:center; background:#2c3e50; color:white; font-family:Arial; padding-top:50px;'>");

        if (update > 0) {
            pw.println("<div style='border:1px solid #27ae60; display:inline-block; padding:20px; border-radius:10px;'>");
            pw.println("<h1>✔ Registration Successful!</h1>");
            pw.println("<p>Account Number: <b>" + ub.getAccNo() + "</b></p>");
            
            try {
                sendEmail(ub);
                pw.println("<p style='color:#2ecc71;'>Welcome email sent successfully to: " + email + "</p>");
            } catch (Exception e) {
                pw.println("<p style='color:#e67e22;'>Registration saved, but Email failed.</p>");
                pw.println("<small>Error: " + e.getMessage() + "</small>");
                e.printStackTrace(); // This prints to the server console/logs
            }
            pw.println("</div>");
        } else {
            pw.println("<h1 style='color:#e74c3c;'>✘ Registration Failed</h1>");
            pw.println("<p>Please check your details and try again.</p>");
        }
        
        pw.println("<br><br><a href='login.html' style='color:cyan; text-decoration:none;'>Go to Login</a>");
        pw.println("</body></html>");
    }

    private void sendEmail(UserBean ub) throws MessagingException {
        final String sender = "balayaswanthkumarv@gmail.com"; 
        final String appPwd = "rpvwfwwwsmkzpebl"; 

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true"); // Forces TLS
        
        // This helps fix the 'socket to TLS' conversion error
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, appPwd);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ub.getEmail()));
        msg.setSubject("YASH Bank - Registration Success");
        msg.setText("Hi " + ub.getFname() + ",\nAcc No: " + ub.getAccNo() + "\nRegistration is successful!");

        Transport.send(msg);
    }
    }
