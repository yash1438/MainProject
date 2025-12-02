package Project;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@WebServlet("/DownloadMiniPDF")
public class DownloadMiniPDF extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Set response as PDF file
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=ministatement.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Mini Statement", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
        	HttpSession hs = request.getSession(false);
    		UserBean ub  =(UserBean) hs.getAttribute("user");
            // Fetch mini statement from DAO
            List<TranstrationBean> miniStatement = new MiniStatementDAO().getMiniStatement(ub.getAccNo());

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("Sender");
            table.addCell("Receiver");
            table.addCell("Type");
            table.addCell("Amount");
            table.addCell("Date/Time");

            for (TranstrationBean t : miniStatement) {
                table.addCell(t.getSenderaccountNo());
                table.addCell(t.getReceiveraccountNo());
                table.addCell(t.getTranstrationtype());
                table.addCell(String.valueOf(t.getTranstrationamount()));
                table.addCell(String.valueOf(t.getDate_time()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
