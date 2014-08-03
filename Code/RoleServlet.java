import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

/*
 *
 * @author Matt Ping & Matt Bucci
 *
 */
public class RoleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TitleMethods titleMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private String roleName = "";
    private String tName = "";
    private String uid = "";
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        titleMethods = new TitleMethods();
        message = titleMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Actor Servlet
        String queryString = request.getQueryString();

        if (queryString != null && !queryString.trim().equals("")) {
            String[] qString = queryString.split("&");

            for (int i = 0; i < qString.length; i++) {
                String[] tempString = qString[i].split("=");

                try {
                    if (tempString[0].equals("aid"))
                        aid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("tid"))
                        tid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("uid"))
                        uid = tempString[1];
                    else if (tempString[0].equals("save"))
                        saveMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("edit"))
                        editMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("add"))
                        addMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("role"))
                        roleName = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("name"))
                        tName = HTMLUtils.cleanQString(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Role", uid, "RoleServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else if (uid.equals("")) {
            out.println("\t\t<h1>This page only works with a valid logged in user!</h1>");
        }
        else {
            if (saveMode) {
                ActorRole actorRole = new ActorRole(aid, tid, roleName);

                if (editMode)
                    actorRole = titleMethods.updateActorRole(actorRole);
                else if (addMode)
                    actorRole = titleMethods.addActorRole(actorRole);

                out.println("\t\t<div>Role saved successfully</div>");

                TitleActorRole titleRole = titleMethods.getSingleTitleAndRole(aid, tid);
                renderEnterRole(out, titleRole);
            }
            else if (!tName.equals("") || tid > -1) {
                renderTitleResult(out);
            }
            else {
                renderTitleSearch(out);
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderTitleSearch(PrintWriter out) {
        out.println("\t\t<form action=\"RoleServlet\" method=\"get\">");
        out.println("\t\t\t<h2>Title search: </h2>");
        out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
        out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
        out.println("\t\t\t<input type=\"text\" name=\"name\" /><input type=\"submit\" value=\"Search\" />");
        out.println("\t\t</form>");
    }

    public void renderTitleResult(PrintWriter out) {
        out.println("\t\t<form action=\"RoleServlet\" method=\"get\">");
        out.println("\t\t\t<h2>Title search: </h2>");
        out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
        out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
        out.println("\t\t\t<input type=\"text\" name=\"name\" /><input type=\"submit\" value=\"Search\" />");
        out.println("\t\t</form>");

        if (!tName.equals("")) {
            ArrayList titles = new ArrayList();

            titles = titleMethods.getTitlesAndRole(tName);

            if (titles.size() > 1) {
                out.println("\t\t<h2>Results</h2>");
                out.println("\t\t<table>");
                out.println("\t\t\t<tr>");
                out.println("\t\t\t\t<td><b>Title Name</b></td>");
                out.println("\t\t\t\t<td><b>Release Year</b></td>");
                out.println("\t\t\t\t<td><b>Synopsis</b></td>");
                out.println("\t\t\t\t<td><b>Genre</b></td>");
                out.println("\t\t\t</tr>");

                for (int i = 0; i < titles.size(); i++) {
                    TitleActorRole title = (TitleActorRole)  titles.get(i);

                    out.println("\t\t\t<tr>");
                    out.println("\t\t\t\t<td><a href=\"RoleServlet?aid=" + aid + "&tid=" + title.getTID() + "&uid=" + uid + "\">" + title.getName() + "</a></td>");
                    out.println("\t\t\t\t<td>" + title.getYear() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getSynopsis() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getGenre() + "</td>");
                    out.println("\t\t\t</tr>");
                }

                out.println("\t\t</table>");
            }
            else {
                TitleActorRole titleRole = (TitleActorRole) titles.get(0);
                renderEnterRole(out, titleRole);
            }
        }
        else if (tid > -1) {
            TitleActorRole titleRole = titleMethods.getSingleTitleAndRole(aid, tid);
            renderEnterRole(out, titleRole);
        }
    }

    public void renderEnterRole(PrintWriter out, TitleActorRole titleRole) {
        out.println("\t\t<form action=\"RoleServlet\" method=\"get\">");
        out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
        out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
        out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + titleRole.getTID() + "\" />");
        out.println("\t\t\t<table>");
        out.println("\t\t\t\t<tr>");
        out.println("\t\t\t\t\t<td><b>Title Name</b></td>");
        out.println("\t\t\t\t\t<td><b>Release Year</b></td>");
        out.println("\t\t\t\t\t<td><b>Synopsis</b></td>");
        out.println("\t\t\t\t\t<td><b>Genre</b></td>");
        out.println("\t\t\t\t\t<td><b>Role</b></td>");
        out.println("\t\t\t\t</tr>");
        out.println("\t\t\t\t<tr>");
        out.println("\t\t\t\t\t<td>" + titleRole.getName() + "</td>");
        out.println("\t\t\t\t\t<td>" + titleRole.getYear() + "</td>");
        out.println("\t\t\t\t\t<td>" + titleRole.getSynopsis() + "</td>");
        out.println("\t\t\t\t\t<td>" + titleRole.getGenre() + "</td>");
        out.println("\t\t\t\t\t<td><input type=\"text\" name=\"role\" value=\"" + titleRole.getRole() + "</td>");
        out.println("\t\t\t\t</tr>");
        out.println("\t\t\t</table>");
        out.println("\t\t</form>");
    }

    private void resetValues() {
        aid = -1;
        tid = -1;
        roleName = "";
        tName = "";
        uid = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        titleMethods.closeDBConnection();
    }
}