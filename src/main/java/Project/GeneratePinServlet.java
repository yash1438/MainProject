package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GeneratePinServlet
 */
@WebServlet("/GeneratePin")
public class GeneratePinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       Connection con;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeneratePinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:Xe","yaswanth","1438");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	private Connection DriverManager(String string, String string2, String string3) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			PrintWriter pw=response.getWriter();
			if(true) {
				RequestDispatcher rd=request.getRequestDispatcher("ATMScreen.Html");
				rd.forward(request, response);
			}
			else {
				RequestDispatcher rd=request.getRequestDispatcher("ATMScreen.html");
				rd.forward(request, response);
			}
			 
	 
	}

}
