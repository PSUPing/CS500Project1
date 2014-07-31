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

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        userMethods = new UserMethods();
        message = userMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uid = "";
        String pwd = "";
        String page = "";

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

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
                        out.println("<div><a href=\"" + page + "?uid=" + uid + "\">Return to previous page</a></div><br />");
                        out.println("<div><a href=\"ActorServlet?uid=" + uid + "\">Go to Main Page</a></div><br />");
                    }
                    else {
                        out.println("<div>Login failed.  Please try again.</div>");
                        renderLogin(out, page);
                    }
                }
                else {
                    renderLogin(out, page);
                }
            }
        }
        else {
            out.println(HTMLUtils.renderHeader("Login", "", "LoginServlet"));
            renderLogin(out, "");
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderLogin(PrintWriter out, String returnPage) {
        out.println("<form action=\"LoginServlet\" method=\"get\">");
        out.println("Username: <input type=\"text\" name=\"uid\" /><br />");
        out.println("Password: <input type=\"password\" name=\"pwd\" /><br />");

        if (!returnPage.equals(""))
            out.println("<input type=\"hidden\" name=\"page\" value=\"" + returnPage + "\"/><br />");

        out.println("<input type=\"submit\" />");
        out.println("</form>");
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        userMethods.closeDBConnection();
    }
}