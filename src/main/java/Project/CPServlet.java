package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection con = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:XE",
				"yaswanth",
				"1438"
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

		try {
			HttpSession session = request.getSession(false);

			if (session == null || session.getAttribute("user") == null) {
				pw.println("<h3 style='color:red;'>Session Expired. Please login again.</h3>");
				RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.include(request, response);
				return;
			}

			// ❗ Update PIN for ALL records (NO WHERE CONDITION)
			PreparedStatement pstmt = con.prepareStatement(
				"UPDATE hdfc_user_details SET pinno=? where AccountNo=?"
			);
            UserBean ub = (UserBean) session.getAttribute("user");
			pstmt.setString(1, newPin);
			pstmt.setLong(2, ub.getAccNo());

			int k = pstmt.executeUpdate();

			if (k > 0) {
				RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
				rd.include(request, response);
			} else {
				pw.println("<h3 style='color:red;'>PIN Update Failed.</h3>");
				RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
				rd.include(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			pw.println("Error: " + e.getMessage());
		}
	}
}
