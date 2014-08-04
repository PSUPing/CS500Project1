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
public class TriviaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TriviaMethods triviaMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int trvid = -1;
    private int rating = -1;
    private String uid = "";
    private String trivia_text = "";
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        triviaMethods = new TriviaMethods();
        message = triviaMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Trivia Servlet
        String queryString = request.getQueryString();

        if (queryString != null && !queryString.trim().equals("")) {
            String[] qString = queryString.split("&");

            for (int i = 0; i < qString.length; i++) {
                String[] tempString = qString[i].split("=");

                try {
                    if (tempString[0].equals("aid"))
                        aid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("trvid"))
                        trvid = Integer.parseInt(tempString[1]);
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
                    else if (tempString[0].equals("trivia_text"))
                        trivia_text = tempString[1];
                    else if (tempString[0].equals("rating"))
                        rating = Integer.parseInt(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Trivia", uid, "TriviaServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                Trivia trivia = new Trivia(trvid, aid, trivia_text);

                if (editMode)
                    trivia = triviaMethods.updateTrivia(trivia);
                else if (addMode)
                    trivia = triviaMethods.addTrivia(trivia);

                renderTrivia(out, trivia);
            }
            else if (addMode) {
                renderTriviaTextBoxes(out, null);
            }
            else if (editMode) {
                if (trvid > -1) {
                    Trivia editTrivia = triviaMethods.getTrivia(trvid);
                    renderTriviaTextBoxes(out, editTrivia);
                }
                else {
                    out.println("<p><b>Bad Trivia ID</b></p>");
                }
            }
            else {
                if (trvid > -1)
                    renderTrivia(out, triviaMethods.getTrivia(trvid));
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderTriviaTextBoxes(PrintWriter out, Trivia triviaToUpdate) {
        if (triviaToUpdate != null) {
            out.println("\t\t<form action=\"TriviaServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"nid\" value=\"" + triviaToUpdate.getTriviaID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Trivia: <input type=\"text\" name=\"trivia_text\" value=\"" + triviaToUpdate.getTrivia() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TriviaServlet?trvid=" + triviaToUpdate.getTriviaID() +
                    "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"TriviaServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Trivia Source: <input type=\"text\" name=\"trivia_source\" /></div>");
            out.println("\t\t\t<div>Trivia Url: <input type=\"text\" name=\"trivia_url\" /></div>");

            if (trvid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TriviaServlet?trvid=" + triviaToUpdate.getTriviaID() +
                        "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TriviaServlet?aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderTrivia(PrintWriter out, Trivia displayTrivia) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"TriviaServlet?uid=" + uid + "&trvid=" + trvid + "&tid=" + tid + "&add=true\">Add</a> " +
                    "<a href=\"TriviaServlet?uid=" + uid + "&trvid=" + trvid + "&aid=" + aid + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>Trivia: </b> " + displayTrivia.getTrivia() + "</div>");

        if (uid.equals(""))
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "\">Back to Actor Page</a></div>");
        else
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "&uid=" + uid + "\">Back to Actor Page</a></div>");
    }

    private void resetValues() {
    	aid = -1;
        tid = -1;
        trvid = -1;
        rating = -1;
        uid = "";
        trivia_text = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        triviaMethods.closeDBConnection();
    }
}