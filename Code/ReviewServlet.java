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
public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewMethods reviewMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int revid = -1;
    private int rating = -1;
    private String uid = "";
    private String rs = "";
    private String rt = "";
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        reviewMethods = new ReviewMethods();
        message = reviewMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Review Servlet
        String queryString = request.getQueryString();

        if (queryString != null && !queryString.trim().equals("")) {
            String[] qString = queryString.split("&");

            for (int i = 0; i < qString.length; i++) {
                String[] tempString = qString[i].split("=");

                try {
                    if (tempString[0].equals("aid"))
                        aid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("revid"))
                        revid = Integer.parseInt(tempString[1]);
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
                    else if (tempString[0].equals("rs"))
                        rs = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("rt"))
                        rt = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("rating"))
                        rating = Integer.parseInt(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Review", uid, "ReviewServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                Review review = new Review(revid, tid, rating, rs, rt);

                if (editMode)
                    review = reviewMethods.updateReview(review);
                else if (addMode)
                    review = reviewMethods.addReview(review);

                renderReview(out, review);
            }
            else if (addMode) {
                renderReviewTextBoxes(out, null);
            }
            else if (editMode) {
                if (revid > -1) {
                    Review editReview = reviewMethods.getReview(revid);
                    renderReviewTextBoxes(out, editReview);
                }
                else {
                    out.println("<p><b>Bad review ID</b></p>");
                }
            }
            else {
                if (revid > -1) {
                    Review review = reviewMethods.getReview(revid);
                    renderReview(out, review);
                }
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderReviewTextBoxes(PrintWriter out, Review reviewToUpdate) {
        if (reviewToUpdate != null) {
            out.println("\t\t<form action=\"ReviewServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"revid\" value=\"" + reviewToUpdate.getRevID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Review: <input type=\"text\" name=\"rt\" value=\"" + reviewToUpdate.getReviewText() + "\" /></div>");
            out.println("\t\t\t<div>Score: <input type=\"text\" name=\"rating\" value=\"" + reviewToUpdate.getScore() + "\" /></div>");
            out.println("\t\t\t<div>Review Source: <input type=\"text\" name=\"rs\" value=\"" + reviewToUpdate.getReviewSource() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ReviewServlet?revid=" + reviewToUpdate.getRevID() +
                    "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"ReviewServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + tid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Review: <input type=\"text\" name=\"rt\" /></div>");
            out.println("\t\t\t<div>Score: <input type=\"text\" name=\"rating\" /></div>");
            out.println("\t\t\t<div>Review Source: <input type=\"text\" name=\"rs\" /></div>");

            if (revid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ReviewServlet?revid=" + reviewToUpdate.getRevID() +
                        "&aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ReviewServlet?aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderReview(PrintWriter out, Review displayReview) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"ReviewServlet?uid=" + uid + "&revid=" + revid + "&tid=" + tid + "&add=true\">Add</a> " +
                    "<a href=\"ReviewServlet?uid=" + uid + "&revid=" + revid + "&tid=" + tid + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>Review: </b> " + displayReview.getReviewText() + "</div>");
        out.println("<div><b>Score: </b> " + displayReview.getScore() + "</div>");
        out.println("<div><b>Review Source: </b> " + displayReview.getReviewSource() + "</div><br />");

        if (uid.equals(""))
            out.println("<div><a href=\"TitleServlet?aid=" + aid + "&tid=" + tid + "\">Back to Title Page</a></div>");
        else
            out.println("<div><a href=\"TitleServlet?aid=" + aid + "&tid=" + tid + "&uid=" + uid + "\">Back to Title Page</a></div>");
    }

    private void resetValues() {
        aid = -1;
        tid = -1;
        revid = -1;
        rating = -1;
        rs = "";
        rt = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        reviewMethods.closeDBConnection();
    }
}