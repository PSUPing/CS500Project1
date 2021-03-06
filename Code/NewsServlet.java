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
public class NewsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NewsMethods newsMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int nid = -1;
    private int rating = -1;
    private String uid = "";
    private String news_source = "";
    private String news_url = "";
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        newsMethods = new NewsMethods();
        message = newsMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        resetValues();

        // Get possible variables for the News Servlet
        String queryString = request.getQueryString();

        if (queryString != null && !queryString.trim().equals("")) {
            String[] qString = queryString.split("&");

            for (int i = 0; i < qString.length; i++) {
                String[] tempString = qString[i].split("=");

                try {
                    if (tempString[0].equals("aid"))
                        aid = Integer.parseInt(tempString[1]);
                    else if (tempString[0].equals("nid"))
                        nid = Integer.parseInt(tempString[1]);
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
                    else if (tempString[0].equals("news_source"))
                        news_source = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("news_url"))
                        news_url = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("rating"))
                        rating = Integer.parseInt(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("News", uid, "NewsServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                News news = new News(nid, aid, news_source, news_url);

                if (editMode)
                    news = newsMethods.updateNews(news);
                else if (addMode)
                    news = newsMethods.addNews(news);

                renderNews(out, news);
            }
            else if (addMode) {
                renderNewsTextBoxes(out, null);
            }
            else if (editMode) {
                if (nid > -1) {
                    News editNews = newsMethods.getNews(nid);
                    renderNewsTextBoxes(out, editNews);
                }
                else {
                    out.println("<p><b>Bad News ID</b></p>");
                }
            }
            else {
                if (nid > -1)
                    renderNews(out, newsMethods.getNews(nid));
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderNewsTextBoxes(PrintWriter out, News newsToUpdate) {
        if (newsToUpdate != null) {
            out.println("\t\t<form action=\"NewsServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"nid\" value=\"" + newsToUpdate.getNID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>News Source: <input type=\"text\" name=\"news_source\" value=\"" + newsToUpdate.getNewsSource() + "\" /></div>");
            out.println("\t\t\t<div>News URL: <input type=\"text\" name=\"news_url\" value=\"" + newsToUpdate.getNewsURL() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"NewsServlet?nid=" + newsToUpdate.getNID() +
                    "&aid=" + aid + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"NewsServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"add\" value=\"true\" />");
            out.println("\t\t\t<div>News Source: <input type=\"text\" name=\"news_source\" /></div>");
            out.println("\t\t\t<div>News Url: <input type=\"text\" name=\"news_url\" /></div>");

            if (nid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"NewsServlet?nid=" + newsToUpdate.getNID() +
                        "&aid=" + aid + "&uid=" + uid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"NewsServlet?aid=" + aid + "&uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderNews(PrintWriter out, News displayNews) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"NewsServlet?uid=" + uid + "&nid=" + nid + "&tid=" + tid + "&add=true\">Add</a> " +
                    "<a href=\"NewsServlet?uid=" + uid + "&nid=" + nid + "&aid=" + aid + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>News Source: </b> " + displayNews.getNewsSource() + "</div>");
        out.println("<div><b>News URL: </b> " + displayNews.getNewsURL() + "</div>");

        if (uid.equals(""))
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "\">Back to Actor Page</a></div>");
        else
            out.println("<div><a href=\"ActorServlet?aid=" + aid + "&uid=" + uid + "\">Back to Actor Page</a></div>");
    }

    private void resetValues() {
    	aid = -1;
        tid = -1;
        nid = -1;
        rating = -1;
        uid = "";
        news_source = "";
        news_url = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        newsMethods.closeDBConnection();
    }
}