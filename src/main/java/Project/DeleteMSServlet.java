package Project;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DeleteMSServlet
 */
@WebServlet("/DeleteMS")
public class DeleteMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteMSServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String dateTime = request.getParameter("date_time");

	        PrintWriter out = response.getWriter();

	        int result = new MiniStatementDAO().deleteByDate(dateTime);

	        if (result > 0) {
	            response.sendRedirect("MS");  // Refresh table
	        } else {
	            out.println("<h3>Failed to delete record!</h3>");
	        }
	    }
	}


