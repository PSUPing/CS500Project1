import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
public class QuoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ActorMethods actMethods;
    private TitleMethods titleMethods;
    private QuoteMethods quoteMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int qid = -1;
    private int tid = -1;
    private int uRating = -1;
    private String aName = "";
    private String uid = "";
    private String bio = "";
    private String qt = "";
    private java.sql.Date dob;
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        actMethods = new ActorMethods();
        quoteMethods = new QuoteMethods();
        titleMethods = new TitleMethods();
        message = quoteMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Quote Servlet
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
                    else if (tempString[0].equals("dob"))
                        dob = java.sql.Date.valueOf(tempString[1]);
                    else if (tempString[0].equals("bio"))
                        bio = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("name"))
                        aName = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("urating"))
                        uRating = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("qid"))
                        qid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("qt"))
                        qt = HTMLUtils.cleanQString(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Quote", uid, "QuoteServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                Quote quote = new Quote(qid, aid, qt);

                if (editMode)
                    quote = quoteMethods.updateQuote(quote);
                else if (addMode)
                    quote = quoteMethods.addQuote(quote);

                renderQuote(out, quote);
                
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
                renderQuoteTextBoxes(out, null);
            }
            else if (editMode) {
                if (qid > -1) {
                    Quote editReview = quoteMethods.getQuote(qid);
                    renderQuoteTextBoxes(out, editReview);
                }
                else {
                    out.println("<p><b>Bad Quote ID</b></p>");
                }
            }
            else {
                if (qid > -1)
                    renderQuote(out, quoteMethods.getQuote(qid));
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderQuoteTextBoxes(PrintWriter out, Quote quoteToUpdate) {
        if (quoteToUpdate != null) {
            out.println("\t\t<form action=\"QuoteServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"revid\" value=\"" + quoteToUpdate.getQID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Quote: <input type=\"text\" name=\"qt\" value=\"" + quoteToUpdate.getQuote() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"QuoteServlet?qid=" + quoteToUpdate.getQID() +
                    "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"QuoteServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Quote: <input type=\"text\" name=\"qt\" /></div>");

            if (qid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"QuoteServlet?qid=" + quoteToUpdate.getQID() +
                        "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"QuoteServlet?aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderQuote(PrintWriter out, Quote displayQuote) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&qid=" + displayQuote.getQID() + "&add=true\">Add</a> " +
                    "<a href=\"QuoteServlet?uid=" + uid + "&qid=" + displayQuote.getQID() + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>Quote:</b> " + displayQuote.getQuote() + "</div>");

        if (uid.equals(""))
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "\">Back to Actor Page</a></div>");
        else
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "&uid=" + uid + "\">Back to Actor Page</a></div>");
    }

    private void resetValues() {
        aid = -1;
        qid = -1;
        tid = -1;
        uRating = -1;
        aName = "";
        uid = "";
        bio = "";
        qt = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        quoteMethods.closeDBConnection();
    }
}