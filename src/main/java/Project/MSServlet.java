package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class MSServlet
 */
@WebServlet("/MS")
public class MSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MSServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	 
//	ArrayList<TranstrationBean> miniStatement = new MiniStatementDAO().getMiniStatement();
//	PrintWriter pw=response.getWriter();
//	for (TranstrationBean transtrationBean : miniStatement) {
//		pw.println(transtrationBean);
	//}
		
		HttpSession hs = request.getSession(false);
		UserBean ub  =(UserBean) hs.getAttribute("user");
		ArrayList<TranstrationBean> miniStatement = new MiniStatementDAO().getMiniStatement(ub.getAccNo());
		PrintWriter pw = response.getWriter();
	 response.setContentType("text/html");
	
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<style>");
		pw.println("table {"
		        + "width: 70%;"
		        + "margin: 20px auto;"
		        + "border-collapse: collapse;"
		        + "font-family: Arial, sans-serif;"
		        + "box-shadow: 0px 0px 10px #ccc;"
		        + "} ");

		pw.println("th {"
		        + "background-color: #007bff;"
		        + "color: white;"
		        + "padding: 10px;"
		        + "text-align: center;"
		        + "font-size: 18px;"
		        + "} ");

		pw.println("td {"
		        + "padding: 10px;"
		        + "text-align: center;"
		        + "border-bottom: 1px solid #ddd;"
		        + "font-size: 16px;"
		        + "} ");

		pw.println("tr:hover {"
		        + "background-color: #f1f1f1;"
		        + "} ");

		pw.println("h2 {"
		        + "text-align: center;"
		        + "font-family: Arial;"
		        + "margin-top: 20px;"
		        + "} ");

		pw.println("</style>");
		pw.println("</head>");
		pw.println("<body>");

		pw.println("<h2>Mini Statement</h2>");

		pw.println("<table border='1'>");
		pw.println("<tr>"
		        + "<th>Sender</th>"
		        + "<th>Receiver</th>"
		        + "<th>Type</th>"
		        + "<th>Amount</th>"
		        + "<th>Date / Time</th>"
		        + "<th>Delete</th>"
		        + "</tr>");
		pw.println("<div style='text-align:center; margin-bottom:20px;'>");
		pw.println("<form action='DownloadMiniPDF' method='post'>");
		pw.println("<button type='submit' style='"
		        + "background:green;"
		        + "color:white;"
		        + "padding:10px 20px;"
		        + "font-size:16px;"
		        + "border:none;"
		        + "border-radius:6px;"
		        + "cursor:pointer;'>Download PDF</button>");
		pw.println("</form>");
		pw.println("</div>");


		for (TranstrationBean t : miniStatement) {
		    pw.println("<tr>");
		    pw.println("<td>" + t.getSenderaccountNo() + "</td>");
		    pw.println("<td>" + t.getReceiveraccountNo() + "</td>");
		    pw.println("<td>" + t.getTranstrationtype() + "</td>");
		    pw.println("<td>" + t.getTranstrationamount() + "</td>");
		    pw.println("<td>" + t.getDate_time() + "</td>");
		 
		
		  pw.println("<td>");
		    pw.println("<form action='DeleteMS' method='post'>");
		    pw.println("<input type='hidden' name='date_time' value='" + t.getDate_time() + "'>");
		    pw.println("<button type='submit' style='background:red;color:white;padding:5px 10px;border:none;border-radius:4px;cursor:pointer;'>Delete</button>");
		    pw.println("</form>");
		    pw.println("</td>");
		    
		  
		    pw.println("</form>");
		    pw.println("</div>");
		    pw.println("</tr>");
		}
		pw.println("</table>");
		pw.println("<form action='ATMScreen.html' method='get' style='text-align:center; margin-top:20px;'>");
		pw.println("<button type='submit' style='"
		        + "background:#444;"
		        + "color:white;"
		        + "padding:10px 20px;"
		        + "font-size:18px;"
		        + "border:none;"
		        + "border-radius:6px;"
		        + "cursor:pointer;'>Exit </button>");
		pw.println("</form>");

		pw.println("</body></html>");

	}

}
