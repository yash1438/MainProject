 
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
                    "jdbc:oracle:thin:@localhost:1521:ORCL",
                    "system",
                    "Yash1438");
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
                    "SELECT ACCNO, TRANTYPE, AMOUNT, TRANDATE " +
                    "FROM MINISTATEMENT " +
                    "WHERE ACCNO = ? " +
                    "ORDER BY TRANDATE DESC");

            ps.setString(1, String.valueOf(accNo)); // only ONE param now

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

            // -------- PDF HEADER --------
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font subFont  = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("YASH Bank", titleFont));
            document.add(new Paragraph("Mini Statement", subFont));
            document.add(new Paragraph("Account No: " + accNo, subFont));
            document.add(new Paragraph(" "));

            // -------- PDF TABLE — 4 columns now --------
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Table Headers
            table.addCell(new PdfPCell(new Phrase("Account No")));
            table.addCell(new PdfPCell(new Phrase("Type")));
            table.addCell(new PdfPCell(new Phrase("Amount")));
            table.addCell(new PdfPCell(new Phrase("Date / Time")));

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                String accno  = rs.getString("ACCNO");
                String type   = rs.getString("TRANTYPE");
                String amount = rs.getString("AMOUNT");
                String date   = rs.getString("TRANDATE");

                // Add to PDF
                table.addCell(accno  != null ? accno  : "-");
                table.addCell(type   != null ? type   : "-");
                table.addCell(amount != null ? "Rs." + amount : "-"); // ₹ may not render in PDF font
                table.addCell(date   != null ? date   : "-");

                // Add to email text
                mailData.append(type)
                        .append("  Rs.")
                        .append(amount)
                        .append("  ")
                        .append(date)
                        .append("\n");
            }

            if (!hasData) {
                mailData.append("No transactions found.\n");
                PdfPCell noData = new PdfPCell(new Phrase("No transactions found."));
                noData.setColspan(4);
                table.addCell(noData);
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
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPwd);
            }
        });

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderEmail));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(ub.getEmail()));
        msg.setSubject("YASH Bank Mini Statement");

        String messageText =
                "Dear Customer,\n\n" +
                "Account No  : " + ub.getAccNo() + "\n" +
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