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
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserMethods userMethods;
    private ResourceBundle bundle;
    private String message;

    private String uid = "";
    private String pwd = "";
    private String page = "";
    private java.sql.Date dob;
    private java.sql.Date joined;
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        userMethods = new UserMethods();
        message = userMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
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
                    if (tempString[0].equals("uid"))
                        uid = tempString[1];
                    else if (tempString[0].equals("pwd"))
                        pwd = tempString[1];
                    else if (tempString[0].equals("page"))
                        page = tempString[1];
                    else if (tempString[0].equals("save"))
                        saveMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("edit"))
                        editMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("add"))
                        addMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("dob"))
                        dob = java.sql.Date.valueOf(tempString[1]);
                    else if (tempString[0].equals("joined"))
                        joined = java.sql.Date.valueOf(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }

            out.println(HTMLUtils.renderHeader("Login", uid, "LoginServlet"));

            if (!message.equalsIgnoreCase("servus")) {
                out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
            }
            else {
                if (!uid.trim().equals("") && !pwd.trim().equals("")) {
                    User loggedIn = userMethods.getUser(uid, pwd);

                    if (loggedIn != null) {
                        out.println("<div>You have successfully logged in.</div><br /><br />");
                        out.println("<div><a href=\"ActorServlet?uid=" + uid + "\">Go to Main Page</a></div><br />");
                    }
                    else {
                        out.println("<div>Login failed.  Please try again.</div>");
                        renderLogin(out, page);
                    }
                }
                else {
                    if (saveMode) {
                        User user = new User(uid, pwd, dob, joined);

                        if (addMode) {
                            user = userMethods.addUser(user);
                            out.println("<p>" + user.getPassword() + "</p>");
                            out.println("<div>" + uid + " successfully added</div>");
                        }
                        else if (editMode) {
                            user = userMethods.updateUser(user);
                            out.println("<div>" + uid + " successfully updated</div>");
                        }

                        out.println("<div><a href=\"ActorServlet?uid=" + uid + "\">Back to main page</a></div>");
                    }
                    else if (addMode) {
                        renderRegistration(out, null);
                    }
                    else if (editMode) {
                        User user = userMethods.getUser(uid);
                        renderRegistration(out, user);
                    }
                    else {
                        renderLogin(out, page);
                    }
                }
            }
        }
        else {
            out.println(HTMLUtils.renderHeader("Login", "", "LoginServlet"));
            renderLogin(out, "");
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderRegistration(PrintWriter out, User userToUpdate) {
        if (userToUpdate != null) {
            out.println("\t\t<form action=\"LoginServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + userToUpdate.getUID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"joined\" value=\"" + userToUpdate.getDateJoined() + "\" />");
            out.println("\t\t\t<div>User Name: <input type=\"text\" name=\"uid\" disabled=\"true\" value=\"" + userToUpdate.getUID() + "\" /></div>");
            out.println("\t\t\t<div>Password: <input type=\"pwd\" name=\"password\" value=\"" + userToUpdate.getPassword() + "\" /></div>");
            out.println("\t\t\t<div>DOB: <input type=\"text\" name=\"dob\" value=\"" + userToUpdate.getDOB() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"LoginServlet?uid=" + userToUpdate.getUID() + "\">Cancel</a>");
            out.println("\t\t</form>");
            out.println("\t\t<a href=\"ActorServlet?uid=" + userToUpdate.getUID() + "\">Back to Actor Page</a>");
        }
        else {
            out.println("\t\t<form action=\"LoginServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"add\" value=\"true\" />");
            out.println("\t\t\t<div>User Name: <input type=\"text\" name=\"uid\" /></div>");
            out.println("\t\t\t<div>Password: <input type=\"pwd\" name=\"password\" /></div>");
            out.println("\t\t\t<div>DOB: <input type=\"text\" name=\"dob\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"LoginServlet\">Cancel</a>");
            out.println("\t\t</form>");
            out.println("\t\t<a href=\"ActorServlet\">Back to Actor Page</a>");
        }
    }

    private void renderLogin(PrintWriter out, String returnPage) {
        out.println("<form action=\"LoginServlet\" method=\"get\">");
        out.println("Username: <input type=\"text\" name=\"uid\" /><br />");
        out.println("Password: <input type=\"password\" name=\"pwd\" /><br />");

        if (!returnPage.equals(""))
            out.println("<input type=\"hidden\" name=\"page\" value=\"" + returnPage + "\"/><br />");

        out.println("<input type=\"submit\" />");
        out.println("</form>");

        out.println("<a href=\"LoginServlet?add=true\">Register</a>");
    }

    private void resetValues() {
        uid = "";
        pwd = "";
        page = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        userMethods.closeDBConnection();
    }
}