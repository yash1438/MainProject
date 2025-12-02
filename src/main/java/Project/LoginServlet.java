//package Project;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
///**
// * Servlet implementation class RegServlet
// */
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//	 
//		String uname = request.getParameter("uname");
//		String pword = request.getParameter("pword");
//		 
//	 
//		PrintWriter pw = response.getWriter();
//		response.setContentType("text/html");
//		UserBean ub = new HDFCDAOServlet().loginUser(uname , pword);
////		System.out.println(ub);
//		pw.println("<html><body bgcolor = cyan text=red><center><h1>");
//		 if( ub==null) {
//			 pw.println("Invalid Username/password");
//		 }else {
//			 RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
//			
//			ServletContext sc = getServletContext();
//			sc.setAttribute("user", ub);
//		rd.include(request, response);
//		pw.println("Welcome to hdfc:"+uname+"<br>");
//		String str = String.valueOf(ub.getAccNo());
//		String sub = str.substring(str.length()-4, str.length());
//	
//		pw.println("AccNo:XXXXXX"+sub);
//			
//		 }
//		 pw.println(" </h1></body></center></html>");
//
//
//	}
//
//}
package Project;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");

        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        // Login check
        UserBean ub = new HDFCDAOServlet().loginUser(uname, pword);

        pw.println("<html><body bgcolor='cyan'><center><h1>");

        if (ub == null) {
            pw.println("Invalid Username / Password");
        } else {

            // 🔹 Store user in session
            HttpSession session = request.getSession();
            session.setAttribute("user", ub);

            // 🔹 Mask account number
            long accNo = ub.getAccNo();
            String accStr = String.valueOf(accNo);
            String maskedAcc = "XXXXXX" + accStr.substring(accStr.length() - 4);

            // Redirect to ATM screen
            RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
            rd.include(request, response);

            pw.println("<div style='position: absolute; top: 20px; right: 20px; "
                    + "background: rgba(0,0,0,0.5); padding: 10px 15px; "
                    + "border-radius: 8px; color: red; font-size: 18px;'>");

            pw.println("Welcome to HDFC: " + uname + "<br>");
            pw.println("AccNo: " + maskedAcc + "<br>");

            pw.println("</div>");

        }

        pw.println("</h1></center></body></html>");
    }
}
