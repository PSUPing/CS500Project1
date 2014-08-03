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
public class TitleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TitleMethods titleMethods;
    private ReviewMethods reviewMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int year = -1;
    private String tName = "";
    private String uid = "";
    private String genre = "";
    private String synopsis = "";
    private String ttype = "";
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        titleMethods = new TitleMethods();
        reviewMethods = new ReviewMethods();
        message = titleMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the Title Servlet
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
                    else if (tempString[0].equals("name"))
                        tName = tempString[1].replace('+', ' ').replaceAll("%3A", ":");
                    else if (tempString[0].equals("genre"))
                        genre = tempString[1].replace('+', ' ');
                    else if (tempString[0].equals("year"))
                        year = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("synopsis"))
                        synopsis = tempString[1].replace('+', ' ');
                    else if (tempString[0].equals("ttype"))
                        ttype = tempString[1].replace('+', ' ');
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Title", uid, "TitleServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                Title title = new Title(tid, tName, genre, year, synopsis, ttype);

                if (editMode)
                    title = titleMethods.updateTitle(title);
                else if (addMode)
                    title = titleMethods.addTitle(title);

                renderTitle(out, title);
            }
            else if (addMode) {
                renderTitleTextBoxes(out, null);
            }
            else if (editMode) {
                if (tid > -1) {
                    Title editTitle = titleMethods.getTitle(tid);
                    renderTitleTextBoxes(out, editTitle);
                }
                else {
                    out.println("<p><b>Bad title ID</b></p>");
                }
            }
            else {
                if (tid > -1)
                    renderTitle(out, titleMethods.getTitle(tid));
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderTitleTextBoxes(PrintWriter out, Title titleToUpdate) {
        if (titleToUpdate != null) {
            out.println("\t\t<form action=\"TitleServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"tid\" value=\"" + titleToUpdate.getTID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Name: <input type=\"text\" name=\"name\" value=\"" + titleToUpdate.getName() + "\" /></div>");
            out.println("\t\t\t<div>Release Year: <input type=\"text\" name=\"year\" value=\"" + titleToUpdate.getYear() + "\" /></div>");
            out.println("\t\t\t<div>Genre: <input type=\"text\" name=\"genre\" value=\"" + titleToUpdate.getGenre() + "\" /></div>");
            out.println("\t\t\t<div>Synopsis: <input type=\"text\" name=\"synopsis\" value=\"" + titleToUpdate.getSynopsis() + "\" /></div>");
            out.println("\t\t\t<div>Title Type: <input type=\"text\" name=\"ttype\" value=\"" + titleToUpdate.getTitleType() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TitleServlet?tid=" + titleToUpdate.getTID() + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"TitleServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"add\" value=\"true\" />");
            out.println("\t\t\t<div>Name: <input type=\"text\" name=\"name\" /></div>");
            out.println("\t\t\t<div>Release Year: <input type=\"text\" name=\"year\" /></div>");
            out.println("\t\t\t<div>Genre: <input type=\"text\" name=\"genre\" /></div>");
            out.println("\t\t\t<div>Synopsis: <input type=\"text\" name=\"synopsis\" /></div>");
            out.println("\t\t\t<div>Title Type: <input type=\"text\" name=\"ttype\" /></div>");

            if (tid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TitleServlet?uid=" + uid + "&tid=" + tid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"TitleServlet?uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderTitleReviews(PrintWriter out) {
        reviewMethods.setConn(titleMethods.getConn());
        ArrayList reviews = reviewMethods.getReivewByTitle(tid);

        if (reviews.size() > 0) {
            out.println("\t\t<h2>Title Reviews</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b></b></td>");
            out.println("\t\t\t\t<td><b>Review Source</b></td>");
            out.println("\t\t\t\t<td><b>Review</b></td>");
            out.println("\t\t\t\t<td><b>Rating</b></td>");
            out.println("\t\t\t</tr>");

            for (int i = 0; i < reviews.size(); i++) {
                Review review = (Review)  reviews.get(i);

                out.println("\t\t\t<tr>");

                if (uid.equals(""))
                    out.println("\t\t\t\t<td><a href=\"ReviewServlet?tid=" + tid + "&revid=" + "\">View</a></td>");
                else
                    out.println("\t\t\t\t<td><a href=\"ReviewServlet?tid=" + tid + "&uid=" + uid + "&revid=" + "\">View</a></td>");

                out.println("\t\t\t\t<td>" + review.getReviewSource() + "</td>");
                out.println("\t\t\t\t<td>" + review.getReviewText() + "</td>");
                out.println("\t\t\t\t<td>" + review.getScore() + "</td>");
                out.println("\t\t\t</tr>");
                out.println("\t\t</table>");
            }
        }
    }

    private void renderTitle(PrintWriter out, Title displayTitle) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"TitleServlet?uid=" + uid + "&tid=" + tid + "&add=true\">Add</a> " +
                    "<a href=\"TitleServlet?uid=" + uid + "&tid=" + tid + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>" + displayTitle.getName() + "</b></div><br />");
        out.println("<div><b>Release Year: </b> " + displayTitle.getYear() + "</div>");
        out.println("<div><b>Genre: </b> " + displayTitle.getGenre() + "</div>");
        out.println("<div><b>Synopsis: </b> " + displayTitle.getSynopsis() + "</div>");
        out.println("<div><b>Title Type: </b> " + displayTitle.getTitleType() + "</div>");

        renderTitleReviews(out);
        out.println("\t\t<br /><br />");

        if (uid.equals(""))
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "\">Back to Actor Page</a></div>");
        else
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "&uid=" + uid + "\">Back to Actor Page</a></div>");
    }

    private void resetValues() {
        aid = -1;
        tid = -1;
        year = -1;
        tName = "";
        uid = "";
        genre = "";
        synopsis = "";
        ttype = "";
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