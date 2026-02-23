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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // DAO Login Check
        UserBean ub = new HDFCDAOServlet().loginUser(uname, pword);

        /* ❌ INVALID LOGIN */
        if (ub == null) {

            RequestDispatcher rd = request.getRequestDispatcher("login.html");
            rd.include(request, response);

            pw.println("<script>");
            pw.println("showErrorPopup();");
            pw.println("</script>");

            return; // ⛔ stop execution
        }

        /* ✅ VALID LOGIN */

        HttpSession session = request.getSession(true);
        session.setAttribute("user", ub);
        session.setMaxInactiveInterval(100);
        request.setAttribute("session", session);
        long accNo = ub.getAccNo();
        String accStr = String.valueOf(accNo);
        String maskedAcc = "XXXXXX" + accStr.substring(accStr.length() - 4);

        RequestDispatcher rd = request.getRequestDispatcher("ATMScreen.html");
        rd.include(request, response);
      
      

        pw.println("<div style='position:absolute; top:20px; right:20px;"
                + "background:rgba(0,0,0,0.6); padding:12px 18px;"
                + "border-radius:10px; color:white; font-size:18px;'>");

        pw.println("Welcome : " + uname + "<br>");
        pw.println("Acc No : " + maskedAcc);

        pw.println("</div>");
    }
}
