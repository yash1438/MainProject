//package Project;
//
//import java.io.IOException;
//import java.util.List;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//
//@WebServlet("/DownloadMiniPDF")
//public class DownloadMiniPDF extends HttpServlet {
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        try {
//            // Set response as PDF file
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "attachment; filename=ministatement.pdf");
//
//            Document document = new Document();
//            PdfWriter.getInstance(document, response.getOutputStream());
//            document.open();
//
//            // Title
//            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
//            Paragraph title = new Paragraph("Mini Statement", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            title.setSpacingAfter(20);
//            document.add(title);
//        	HttpSession hs = request.getSession(false);
//    		UserBean ub  =(UserBean) hs.getAttribute("user");
//            // Fetch mini statement from DAO
//            List<TranstrationBean> miniStatement = new MiniStatementDAO().getMiniStatement(ub.getAccNo());
//
//            // Table
//            PdfPTable table = new PdfPTable(5);
//            table.setWidthPercentage(100);
//
//            table.addCell("Sender");
//            table.addCell("Receiver");
//            table.addCell("Type");
//            table.addCell("Amount");
//            table.addCell("Date/Time");
//
//            for (TranstrationBean t : miniStatement) {
//                table.addCell(t.getSenderaccountNo());
//                table.addCell(t.getReceiveraccountNo());
//                table.addCell(t.getTranstrationtype());
//                table.addCell(String.valueOf(t.getTranstrationamount()));
//                table.addCell(String.valueOf(t.getDate_time()));
//            }
//
//            document.add(table);
//            document.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
package Project;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/DownloadMiniPDF")
public class DownloadMiniPDF extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Connection con;

    // -------- DB CONNECTION --------
    public void init() {
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

    // -------- MAIN METHOD --------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // -------- SESSION CHECK --------
            HttpSession hs = request.getSession(false);

            if (hs == null || hs.getAttribute("user") == null) {
                response.sendRedirect("Sessionexpiry.jsp");
                return;
            }

            UserBean ub = (UserBean) hs.getAttribute("user");
            long accNo = ub.getAccNo();

            // -------- FETCH MINI STATEMENT --------
            PreparedStatement ps = con.prepareStatement(
                    "SELECT SENDERACCOUNTNO, RECEIVERACCOUNTNO, TRANSTRATIONTYPE, " +
                    "TRANSTRATIONAMOUNT, DATE_TIME " +
                    "FROM MINISTATEMENT " +
                    "WHERE SENDERACCOUNTNO=? OR RECEIVERACCOUNTNO=? " +
                    "ORDER BY DATE_TIME DESC");

            ps.setString(1, String.valueOf(accNo));
            ps.setString(2, String.valueOf(accNo));

            ResultSet rs = ps.executeQuery();

            // For email content
            StringBuilder mailData = new StringBuilder();
            mailData.append("Mini Statement\n");
            mailData.append("---------------------------------\n");

            // -------- PDF RESPONSE --------
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=MiniStatement.pdf");

            OutputStream out = response.getOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("YASH Bank"));
            document.add(new Paragraph("Mini Statement"));
            document.add(new Paragraph("Account No: " + accNo));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.addCell("Sender");
            table.addCell("Receiver");
            table.addCell("Type");
            table.addCell("Amount");
            table.addCell("Date/Time");

            while (rs.next()) {

                String sender = rs.getString("SENDERACCOUNTNO");
                String receiver = rs.getString("RECEIVERACCOUNTNO");
                String type = rs.getString("TRANSTRATIONTYPE");
                String amount = rs.getString("TRANSTRATIONAMOUNT");
                String date = rs.getString("DATE_TIME");

                // Add to PDF
                table.addCell(sender);
                table.addCell(receiver);
                table.addCell(type);
                table.addCell(amount);
                table.addCell(date);

                // Add to email text
                mailData.append(type)
                        .append("  ₹")
                        .append(amount)
                        .append("  ")
                        .append(date)
                        .append("\n");
            }

            document.add(table);
            document.close();
            out.close();

            // -------- SEND EMAIL --------
            sendEmail(ub, mailData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------- EMAIL METHOD --------
    private void sendEmail(UserBean ub, String data) throws MessagingException {

        final String senderEmail = "balayaswanthkumarv@gmail.com";
        final String appPwd = "rpvwfwwwsmkzpebl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // SSL Fix
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPwd);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderEmail));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ub.getEmail()));

        msg.setSubject("YASH Bank Mini Statement");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String messageText =
                "Dear Customer,\n\n" +
                "Account No: " + ub.getAccNo() + "\n" +
                "Generated On: " + now.format(dtf) + "\n\n" +
                data +
                "\nThank you for banking with YASH Bank.";

        msg.setText(messageText);

        Transport.send(msg);
    }

    public void destroy() {
        try {
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
