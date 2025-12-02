package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class GPServlet
 */
@WebServlet("/GP")
public class GPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       Connection con=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GPServlet() {
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
		try {
			response.setContentType("text/html");
		String s1=request.getParameter("PinNo");
		String s2=request.getParameter("Cid");
//		 System.out.println("pin="+s1);
//		 System.out.println("cid="+s2);
		PreparedStatement pstmt =con.prepareStatement("update hdfc_user_details set PINNO=? where cid=?");
		pstmt.setString(1, s1);
		pstmt.setString(2, s2);
	int k=	pstmt.executeUpdate();
//	System.out.println("k= "+k);
	PrintWriter pw=response.getWriter();
	//pw.println("<html><body text=red><center>");
	//pw.println("<img src='Logo/hdfc-logo.png' alt='HDFC Logo'>");
	if(k>0) {
		//print msg
	//	System.out.println("sucess");
	//	pw.println("Pin Generate Sucessfully");
		RequestDispatcher rd=request.getRequestDispatcher("ATMScreen.html");
		rd.include(request, response);
	}
	else {
		//System.out.println("not sucess");
		pw.println("Pin Generate Faild");
		RequestDispatcher rd=request.getRequestDispatcher("ATMScreen.html");
		rd.include(request, response);
	}
		//pw.println("</center></body></html>");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
