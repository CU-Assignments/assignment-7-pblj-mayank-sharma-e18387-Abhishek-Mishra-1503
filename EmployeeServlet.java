import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;

    public void init() {
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/db-config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            jdbcURL = prop.getProperty("jdbc.url");
            jdbcUsername = prop.getProperty("jdbc.username");
            jdbcPassword = prop.getProperty("jdbc.password");
            Class.forName("com.mysql.cj.jdbc.Driver"); // Adjust driver if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        boolean showAll = request.getParameter("all") != null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword)) {

            if (showAll) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
                printTable(rs, out);

            } else if (idParam != null && !idParam.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
                ps.setInt(1, Integer.parseInt(idParam));
                ResultSet rs = ps.executeQuery();
                printTable(rs, out);

            } else {
                out.println("<p>No employee ID provided. <a href='employeeList.html'>Back</a></p>");
            }

        } catch (SQLException e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    private void printTable(ResultSet rs, PrintWriter out) throws SQLException {
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Designation</th><th>Salary</th></tr>");
        boolean found = false;
        while (rs.next()) {
            found = true;
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%.2f</td></tr>",
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("designation"),
                rs.getDouble("salary"));
        }
        out.println("</table>");
        if (!found) {
            out.println("<p>No employee found.</p>");
        }
        out.println("<p><a href='employeeList.html'>Back</a></p>");
    }
}
