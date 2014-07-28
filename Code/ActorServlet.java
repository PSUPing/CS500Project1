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
//    private ActorMethods actMethods;
    private ResourceBundle bundle;
    private String message;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
//        message = reg.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
//                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

/*    public void displayActors(PrintWriter out, boolean viewOnly) {
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
    }*/

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String aid = "";
        String uid = "";
        boolean editMode = false;

        // Get possible variables for the Actor Servlet
        String queryString = request.getQueryString();
        String[] qString = queryString.split("&");

        for (int i = 0; i < qString.length; i++) {
            String[] tempString = qString[i].split("=");

            if (tempString[0].equals("aid"))
                aid = tempString[1];
            else if (tempString[0].equals("uid"))
                uid = tempString[1];
            else if (tempString[0].equals("editMode"))
                editMode = Boolean.valueOf(tempString[1]).booleanValue();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println(HTMLUtils.renderHeader("Actor", "", editMode));
        out.println("<h2>Actor search: </h2><input type=\"text\" />");

        if (!editMode) {

        }
/*        if (!message.equalsIgnoreCase("servus")) {
            out.println("<h1>Oracle connection failed " + message + "</h1>");
        }*/

        // TODO: This will be the format for moving between pages and for changing from View to Edit
/*        out.println("<a href=\"ActorServlet?name=${name}&dob=${dob}&editMode=true\">Edit Mode</a>");
        out.println("<a href=\"ActorServlet?name=${name}&dob=${dob}\">View Mode</a>");
        out.println("<a href=\"MovieServlet?name=${name}&year=${year}\">Movie Page</a>");*/

        out.println(HTMLUtils.renderClosingTags());
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
//        reg.closeDBConnection();
    }
}