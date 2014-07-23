import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

/**
 *
 * @author Matt Ping & Matt Bucci
 *
 */
public class ActorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ActorMethods actMethods;
    private Registrar reg;
    private ResourceBundle bundle;
    private String message;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        reg = new Registrar();
        message = reg.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void displayActors(PrintWriter out, boolean viewOnly) {
        out.println("<h1>Student roster</h1>");
        out.println("<table>");

        ArrayList allActors = actMethods.getAllActors();

        for (int i = 0; i < roster.size(); i++) {
            Actor actor = (Actor) allActors.get(i);

            if (viewOnly)
                out.println(actor.toViewHTML());
            else
                out.println(actor.toEditHTML());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body>");

        if (!message.equalsIgnoreCase("servus")) {
            out.println("<h1>Oracle connection failed " + message + "</h1>");
        } else {
            String mode = request.getParameter("editMode");

            if (mode == null)
                displayActors(out, true);
            else
                displayActors(out, false);
        }

        // TODO: This will be the format for moving between pages and for changing from View to Edit
        out.println("<a href=\"ActorServlet?name=${name}&dob=${dob}&editMode=true\">Edit Mode</a>");
        out.println("<a href=\"ActorServlet?name=${name}&dob=${dob}\">View Mode</a>");
        out.println("<a href=\"MovieServlet?name=${name}&year=${year}\">Movie Page</a>");

        out.println("</table>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {

        doGet(inRequest, outResponse);
    }

    public void destroy() {
        reg.closeDBConnection();
    }
}