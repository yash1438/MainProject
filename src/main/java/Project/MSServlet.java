package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/MS")
public class MSServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // -------- SESSION CHECK --------
        HttpSession hs = request.getSession(false);

        if (hs == null || hs.getAttribute("user") == null) {
            request.setAttribute("msg", "Session Expired! Please login again.");
            request.getRequestDispatcher("Sessionexpiry.jsp").forward(request, response);
            return;
        }

        UserBean ub = (UserBean) hs.getAttribute("user");

        // -------- GET MINI STATEMENT --------
        ArrayList<TranstrationBean> miniStatement =
                new MiniStatementDAO().getMiniStatement(ub.getAccNo());

        // -------- HTML --------
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<title>Mini Statement</title>");
        pw.println("<style>");
        pw.println("body{font-family:Arial;background:#f5f5f5;}");
        pw.println("table{"
                + "width:70%;"
                + "margin:20px auto;"
                + "border-collapse:collapse;"
                + "background:white;"
                + "box-shadow:0 0 10px #ccc;}");
        pw.println("th{background:#007bff;color:white;padding:10px;font-size:18px;}");
        pw.println("td{padding:10px;text-align:center;border-bottom:1px solid #ddd;}");
        pw.println("tr:hover{background:#f1f1f1;}");
        pw.println("h2{text-align:center;margin-top:20px;}");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");

        pw.println("<h2>YASH Bank - Mini Statement</h2>");

        // -------- DOWNLOAD PDF BUTTON --------
        pw.println("<div style='text-align:center;margin-bottom:20px;'>");
        pw.println("<form action='DownloadMiniPDF' method='post'>");
        pw.println("<input type='hidden' name='accno' value='" + ub.getAccNo() + "'>");
        pw.println("<button type='submit' style='background:green;color:white;"
                + "padding:10px 20px;font-size:16px;border:none;border-radius:6px;"
                + "cursor:pointer;'>Download PDF</button>");
        pw.println("</form>");
        pw.println("</div>");

        // -------- TABLE --------
        pw.println("<table border='1'>");
        pw.println("<tr>");
        pw.println("<th>Account No</th>");
        pw.println("<th>Type</th>");
        pw.println("<th>Amount</th>");
        pw.println("<th>Date / Time</th>");
        pw.println("<th>Delete</th>");
        pw.println("</tr>");

        if (miniStatement == null || miniStatement.isEmpty()) {
            pw.println("<tr><td colspan='5' style='color:gray;'>No transactions found.</td></tr>");
        } else {
            for (TranstrationBean t : miniStatement) {
                pw.println("<tr>");
                pw.println("<td>" + t.getAccNo() + "</td>");
                pw.println("<td>" + t.getTranstrationtype() + "</td>");
                pw.println("<td>₹" + t.getTranstrationamount() + "</td>");
                pw.println("<td>" + t.getDate_time() + "</td>");

                // Delete Button
                pw.println("<td>");
                pw.println("<form action='DeleteMS' method='post'>");
                pw.println("<input type='hidden' name='date_time' value='"
                        + t.getDate_time() + "'>");
                pw.println("<button type='submit' style='background:red;color:white;"
                        + "padding:5px 10px;border:none;border-radius:4px;"
                        + "cursor:pointer;'>Delete</button>");
                pw.println("</form>");
                pw.println("</td>");

                pw.println("</tr>");
            }
        }

        pw.println("</table>");

        // -------- EXIT BUTTON --------
        pw.println("<form action='ATMScreen.html' method='get' "
                + "style='text-align:center;margin-top:20px;'>");
        pw.println("<button type='submit' style='background:#444;color:white;"
                + "padding:10px 20px;font-size:18px;border:none;border-radius:6px;"
                + "cursor:pointer;'>Exit</button>");
        pw.println("</form>");

        pw.println("</body>");
        pw.println("</html>");
    }
}