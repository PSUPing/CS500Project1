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
public class AwardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AwardMethods awardMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int awid = -1;
    private int rating = -1;
    private String uid = "";
    private java.sql.Date nomination_date;
    private java.sql.Date award_date;
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        awardMethods = new AwardMethods();
        message = awardMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Award Servlet
        String queryString = request.getQueryString();

        if (queryString != null && !queryString.trim().equals("")) {
            String[] qString = queryString.split("&");

            for (int i = 0; i < qString.length; i++) {
                String[] tempString = qString[i].split("=");

                try {
                    if (tempString[0].equals("aid"))
                        aid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("awid"))
                        awid = Integer.parseInt(tempString[1]);
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
                    else if (tempString[0].equals("nomination_date"))
                        nomination_date = java.sql.Date.valueOf(tempString[1]);
                    else if (tempString[0].equals("award_date"))
                        award_date = java.sql.Date.valueOf(tempString[1]);
                    else if (tempString[0].equals("rating"))
                        rating = Integer.parseInt(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Award", uid, "AwardServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                Award award = new Award(awid, aid, nomination_date, award_date);

                if (editMode)
                    award = awardMethods.updateAward(award);
                else if (addMode)
                    award = awardMethods.addAward(award);

                renderAward(out, award);
            }
            else if (addMode) {
                renderAwardTextBoxes(out, null);
            }
            else if (editMode) {
                if (awid > -1) {
                    Award editAward = awardMethods.getAward(awid);
                    renderAwardTextBoxes(out, editAward);
                }
                else {
                    out.println("<p><b>Bad Award ID</b></p>");
                }
            }
            else {
                if (awid > -1)
                    renderAward(out, awardMethods.getAward(awid));
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderAwardTextBoxes(PrintWriter out, Award awardToUpdate) {
        if (awardToUpdate != null) {
            out.println("\t\t<form action=\"AwardServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"awid\" value=\"" + awardToUpdate.getAWID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Nomination Date: <input type=\"text\" name=\"nomination_date\" value=\"" + awardToUpdate.getNominationDate() + "\" /></div>");
            out.println("\t\t\t<div>Award Date: <input type=\"text\" name=\"award_date\" value=\"" + awardToUpdate.getAwardDate() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"AwardServlet?awid=" + awardToUpdate.getAWID() +
                    "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"AwardServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Nomination Date: <input type=\"text\" name=\"nomination_date\" /></div>");
            out.println("\t\t\t<div>Award Date: <input type=\"text\" name=\"award_date\" /></div>");

            if (awid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"AwardServlet?awid=" + awardToUpdate.getAWID() +
                        "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"AwardServlet?aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderAward(PrintWriter out, Award displayAward) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"AwardServlet?uid=" + uid + "&awid=" + awid + "&tid=" + tid + "&add=true\">Add</a> " +
                    "<a href=\"AwardServlet?uid=" + uid + "&awid=" + awid + "&aid=" + aid + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>Nomination Date: </b> " + displayAward.getNominationDate() + "</div>");
        out.println("<div><b>Award Date: </b> " + displayAward.getAwardDate() + "</div>");

        if (uid.equals(""))
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "\">Back to Actor Page</a></div>");
        else
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "&uid=" + uid + "\">Back to Actor Page</a></div>");
    }

    private void resetValues() {
    	aid = -1;
        tid = -1;
        awid = -1;
        rating = -1;
        uid = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        awardMethods.closeDBConnection();
    }
}