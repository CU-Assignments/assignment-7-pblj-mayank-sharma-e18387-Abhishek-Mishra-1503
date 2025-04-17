import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Static credential check
        if ("admin".equals(username) && "1234".equals(password)) {
            // Forward to welcome.jsp
            request.setAttribute("username", username);
            RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp");
            rd.forward(request, response);
        } else {
            // Invalid login
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Invalid credentials. Please <a href='login.html'>try again</a>.</h3>");
        }
    }
}
