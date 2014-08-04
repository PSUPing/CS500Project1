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
public class ActorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ActorMethods actMethods;
    private ResourceBundle bundle;
    private String message;

    private int aid = -1;
    private int tid = -1;
    private int uRating = -1;
    private String aName = "";
    private String uid = "";
    private String bio = "";
    private java.sql.Date dob;
    private boolean editMode = false;
    private boolean addMode = false;
    private boolean saveMode = false;

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        actMethods = new ActorMethods();
        message = actMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
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
                    else if (tempString[0].equals("dob"))
                        dob = java.sql.Date.valueOf(tempString[1]);
                    else if (tempString[0].equals("bio"))
                        bio = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("name"))
                        aName = HTMLUtils.cleanQString(tempString[1]);
                    else if (tempString[0].equals("urating"))
                        uRating = Integer.parseInt(tempString[1]);
                } catch (Exception ex) {
                    out.println("<h2>Error parsing query string</h2>");
                }
            }
        }

        out.println(HTMLUtils.renderHeader("Actor", uid, "ActorServlet"));

        if (!message.equalsIgnoreCase("servus")) {
            out.println("\t\t<h1>Oracle connection failed " + message + "</h1>");
        }
        else {
            if (saveMode) {
                if (uRating == -1) {
                    Actor actor = new Actor(aid, aName, dob, bio);

                    if (editMode)
                        actor = actMethods.updateActor(actor);
                    else if (addMode)
                        actor = actMethods.addActor(actor);

                    renderActor(out, actor);
                }
                else {
                    Rating rating = new Rating(aid, tid, uid, uRating);

                    if (actMethods.hasActorTitleRating(rating)) {
                        out.println("<p>Got here</p>");
                        if (uRating == 0) {
                            out.println("<p>Rating 0</p>");
                            actMethods.removeActorTitleRating(rating);
                        }
                        else {
                            out.println("<p>Rating != 0</p>");
                            actMethods.updateRating(rating);
                        }
                    }
                    else {
                        out.println("<p>No Rating</p>");
                        actMethods.addActorTitleRating(rating);
                    }

                    Actor actor = actMethods.getActor(aid);
                    renderActor(out, actor);
                }
            }
            else if (addMode) {
                renderActorTextBoxes(out, null);
            }
            else if (editMode) {
                if (aid > -1) {
                    Actor editActor = actMethods.getActor(aid);
                    renderActorTextBoxes(out, editActor);
                }
                else {
                    out.println("<p><b>Bad actor ID</b></p>");
                }
            }
            else {
                if (aName.equals("") && aid == -1) {
                    if (!uid.equals(""))
                        out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&add=true\">Add</a></div>");

                    renderActorTable(out, actMethods.getRecentActors());
                }
                else {
                    if (aid > -1) {
                        Actor actor = actMethods.getActor(aid);
                        renderActor(out, actor);
                    }
                    else {
                        ArrayList actors = actMethods.getActorByName(aName);

                        if (actors.size() > 1) {
                            if (!uid.equals(""))
                                out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&add=true\">Add</a></div>");

                            renderActorTable(out, actors);
                        }
                        else if (actors.size() == 1) {
                            Actor actor = (Actor) actors.get(0);
                            renderActor(out, actor);
                        }
                        else {
                            out.println("<h2>No actors found with that name</h2>");
                        }
                    }
                }
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderActorTextBoxes(PrintWriter out, Actor actorToUpdate) {
        if (actorToUpdate != null) {
            out.println("\t\t<form action=\"ActorServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"aid\" value=\"" + actorToUpdate.getAID() + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"edit\" value=\"true\" />");
            out.println("\t\t\t<div>Name: <input type=\"text\" name=\"name\" value=\"" + actorToUpdate.getName() + "\" /></div>");
            out.println("\t\t\t<div>Date of Birth: <input type=\"text\" name=\"dob\" value=\"" + actorToUpdate.getDOB() + "\" /></div>");
            out.println("\t\t\t<div>Short Bio: <input type=\"text\" name=\"bio\" value=\"" + actorToUpdate.getBio() + "\" /></div>");
            out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ActorServlet?aid=" + actorToUpdate.getAID() + "&uid=" + uid + "\">Cancel</a>");
            out.println("\t\t</form>");
        }
        else {
            out.println("\t\t<form action=\"ActorServlet\" method=\"get\">");
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"save\" value=\"true\" />");
            out.println("\t\t\t<input type=\"hidden\" name=\"add\" value=\"true\" />");
            out.println("\t\t\t<div>Name: <input type=\"text\" name=\"name\" /></div>");
            out.println("\t\t\t<div>Date of Birth: <input type=\"text\" name=\"dob\" /></div>");
            out.println("\t\t\t<div>Short Bio: <input type=\"text\" name=\"bio\" /></div>");

            if (aid > -1)
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ActorServlet?uid=" + uid + "&aid=" + aid + "\">Cancel</a>");
            else
                out.println("\t\t\t<input type=\"submit\" value=\"Save\"> <a href=\"ActorServlet?uid=" + uid + "\">Cancel</a>");

            out.println("\t\t</form>");
        }
    }

    private void renderActorTable(PrintWriter out, ArrayList actors) {
        out.println("\t\t<form action=\"ActorServlet\" method=\"get\">");
        out.println("\t\t\t<h2>Actor search: </h2>");

        if (!uid.equals(""))
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");

        out.println("\t\t\t<input type=\"text\" name=\"name\" /><input type=\"submit\" value=\"Search\" />");
        out.println("\t\t</form>");
        out.println("\t\t<table>");
        out.println("\t\t\t<tr>");
        out.println("\t\t\t\t<td><b>Name</b></td>");
        out.println("\t\t\t\t<td><b>Date of Birth</b></td>");
        out.println("\t\t\t\t<td><b>Short Bio</b></td>");
        out.println("\t\t\t</tr>");

        for (int i = 0; i < actors.size(); i++) {
            Actor tempAct = (Actor)actors.get(i);

            out.println("\t\t\t<tr>");

            if (uid.equals(""))
                out.println("\t\t\t\t<td><a href=\"ActorServlet?aid=" + tempAct.getAID() + "\">" + tempAct.getName() + "</a></td>");
            else
                out.println("\t\t\t\t<td><a href=\"ActorServlet?aid=" + tempAct.getAID() + "&uid=" + uid + "\">" + tempAct.getName() + "</a></td>");

            out.println("\t\t\t\t<td>" + tempAct.getDOB() + "</td>");
            out.println("\t\t\t\t<td>" + tempAct.getBio() + "</td>");
            out.println("\t\t\t</tr>");
        }

        out.println("\t\t</table>");
    }

    private void renderActorTitles(PrintWriter out) {
        ArrayList titles = new ArrayList();

        if (!uid.equals(""))
            titles = actMethods.getTitlesAndUserRating(aid, uid);
        else
            titles = actMethods.getTitlesAndRole(aid);

        if (titles.size() > 0) {
            out.println("\t\t<h2>Titles Stared In</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b>Title Name</b></td>");
            out.println("\t\t\t\t<td><b>Release Year</b></td>");
            out.println("\t\t\t\t<td><b>Synopsis</b></td>");
            out.println("\t\t\t\t<td><b>Genre</b></td>");
            out.println("\t\t\t\t<td><b>Actor's Role</b></td>");

            if (!uid.equals(""))
                out.println("\t\t\t\t<td><b>" + uid + "'s Rating</b></td>");

            out.println("\t\t\t</tr>");

            for (int i = 0; i < titles.size(); i++) {
                out.println("\t\t\t<tr>");

                if (!uid.equals("")) {
                    ActorTitleRating title = (ActorTitleRating) titles.get(i);
                    boolean foundRating = false;

                    out.println("\t\t\t\t<td><a href=\"TitleServlet?aid=" + aid + "&uid=" + uid + "&tid=" + title.getTID() + "\">" + title.getName() + "</a></td>");
                    out.println("\t\t\t\t<td>" + title.getYear() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getSynopsis() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getGenre() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getRole() + "</td>");
                    out.print("\t\t\t\t<td><form><select name=\"urating\">");

                    for (int j = 1; j <= 5; j++) {
                        if (title.getUserRating() == j) {
                            out.print("<option selected=\"selected\">" + j + "</option>");
                            foundRating = true;
                        }
                        else
                            out.print("<option>" + j + "</option>");
                    }

                    if (!foundRating)
                        out.print("<option selected=\"selected\">" + 0 + "</option>");
                    else
                        out.print("<option>" + 0 + "</option>");

                    out.print("</select>");
                    out.print("<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");
                    out.print("<input type=\"hidden\" name=\"tid\" value=\"" + title.getTID() + "\" />");
                    out.print("<input type=\"hidden\" name=\"aid\" value=\"" + aid + "\" />");
                    out.print("<input type=\"hidden\" name=\"save\" value=\"true\" />");
                    out.print("<input type=\"submit\" value=\"Save Rating\" /></form></td>");
                    out.print("\t\t\t</tr>\n");
                    out.println("\t\t</table><br />");
                    out.println("\t\t<div><a href=\"TitleServlet?uid=" + uid + "&aid=" + aid + "&tid=" + title.getTID() + "&add=true\">Add New Movie</a>");
                    out.println("\t\t<div><a href=\"RoleServlet?uid=" + uid + "&aid=" + aid + "&add=true\">Add New Role</a><br />");
                }
                else {
                    TitleActorRole title = (TitleActorRole)  titles.get(i);

                    out.println("\t\t\t\t<td><a href=\"TitleServlet?aid=" + aid + "&tid=" + title.getTID() + "\">" + title.getName() + "</a></td>");
                    out.println("\t\t\t\t<td>" + title.getYear() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getSynopsis() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getGenre() + "</td>");
                    out.println("\t\t\t\t<td>" + title.getRole() + "</td>");
                    out.println("\t\t\t</tr>");
                    out.println("\t\t</table><br />");
                }
            }
        }
    }

    private void renderQuotes(PrintWriter out) {
        ArrayList quotes = actMethods.getQuotesByActor(aid);
        int latestQuote = 0;

        if (quotes.size() > 0) {
            out.println("\t\t<h2>Quotes</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b></b></td>");
            out.println("\t\t\t\t<td><b>Quote</b></td>");
            out.println("\t\t\t</tr>");

            for (int i = 0; i < quotes.size(); i++) {
                Quote quote = (Quote) quotes.get(i);

                out.println("\t\t\t<tr>");

                if (uid.equals(""))
                    out.println("\t\t\t\t<td><a href=\"QuoteServlet?aid=" + aid + "&qid=" + quote.getQID() + "\">View</a></td>");
                else
                    out.println("\t\t\t\t<td><a href=\"QuoteServlet?aid=" + aid + "&uid=" + uid + "&qid=" + quote.getQID() + "\">View</a></td>");

                out.println("\t\t\t\t<td>" + quote.getQuote() +"</td>");
                out.println("\t\t\t</tr>");
            }

            out.println("\t\t</table>");
            latestQuote = ((Quote) quotes.get(quotes.size() - 1)).getQID();
        }
        out.println("\t\t<div><a href=\"QuoteServlet?uid=" + uid + "&aid=" + aid + "&add=true\">Add New Quote</a>");
    }

    private void renderAwards(PrintWriter out) {
        ArrayList awards = actMethods.getAwardsByActor(aid);

        if (awards.size() > 0) {
            out.println("\t\t<h2>Awards</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b></b></td>");
            out.println("\t\t\t\t<td><b>Nomination Date</b></td>");
            out.println("\t\t\t\t<td><b>Award Date</b></td>");
            out.println("\t\t\t</tr>");

            for (int i = 0; i < awards.size(); i++) {
                Award award = (Award) awards.get(i);

                out.println("\t\t\t<tr>");

                if (uid.equals(""))
                    out.println("\t\t\t\t<td><a href=\"AwardServlet?aid=" + aid + "&awid=" + award.getAWID() + "\">View</a></td>");
                else
                    out.println("\t\t\t\t<td><a href=\"AwardServlet?aid=" + aid + "&uid=" + uid + "&awid=" + award.getAWID() + "\">View</a></td>");

                out.println("\t\t\t\t<td>" + award.getNominationDate() +"</td>");

                if (award.getAwardDate() == null)
                    out.println("\t\t\t\t<td>Did not win</td>");
                else
                    out.println("\t\t\t\t<td>" + award.getAwardDate() +"</td>");

                out.println("\t\t\t</tr>");
            }

            out.println("\t\t</table>");
        }
        out.println("\t\t<div><a href=\"AwardServlet?uid=" + uid + "&aid=" + aid + "&add=true\">Add New Award</a>");
    }

    private void renderTrivia(PrintWriter out) {
        ArrayList trivias = actMethods.getTriviaByActor(aid);

        if (trivias.size() > 0) {
            out.println("\t\t<h2>Trivia</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b></b></td>");
            out.println("\t\t\t\t<td><b>Quote</b></td>");
            out.println("\t\t\t</tr>");

            for (int i = 0; i < trivias.size(); i++) {
                Trivia trivia = (Trivia) trivias.get(i);

                out.println("\t\t\t<tr>");

                if (uid.equals(""))
                    out.println("\t\t\t\t<td><a href=\"TriviaServlet?aid=" + aid + "&trvid=" + trivia.getTriviaID() + "\">View</a></td>");
                else
                    out.println("\t\t\t\t<td><a href=\"TriviaServlet?aid=" + aid + "&uid=" + uid + "&trvid=" + trivia.getTriviaID() + "\">View</a></td>");

                out.println("\t\t\t\t<td>" + trivia.getTrivia() +"</td>");
                out.println("\t\t\t</tr>");
            }

            out.println("\t\t</table>");
        }
        out.println("\t\t<div><a href=\"TriviaServlet?uid=" + uid + "&aid=" + aid + "&add=true\">Add New Trivia</a>");
    }

    private void renderNews(PrintWriter out) {
        ArrayList newses = actMethods.getNewsByActor(aid);

        if (newses.size() > 0) {
            out.println("\t\t<h2>News</h2>");
            out.println("\t\t<table>");
            out.println("\t\t\t<tr>");
            out.println("\t\t\t\t<td><b></b></td>");
            out.println("\t\t\t\t<td><b>News</b></td>");
            out.println("\t\t\t\t<td><b>News URL</b></td>");
            out.println("\t\t\t</tr>");

            for (int i = 0; i < newses.size(); i++) {
                News news = (News) newses.get(i);

                out.println("\t\t\t<tr>");

                if (uid.equals(""))
                    out.println("\t\t\t\t<td><a href=\"NewsServlet?aid=" + aid + "&nid=" + news.getNID() + "\">View</a></td>");
                else
                    out.println("\t\t\t\t<td><a href=\"NewsServlet?aid=" + aid + "&uid=" + uid + "&nid=" + news.getNID() + "\">View</a></td>");

                out.println("\t\t\t\t<td>" + news.getNewsSource() +"</td>");
                out.println("\t\t\t\t<td><a href=\"" + news.getNewsURL() + "\">" + news.getNewsURL() + "</a></td>");
                out.println("\t\t\t</tr>");
            }

            out.println("\t\t</table>");
        }
        out.println("\t\t<div><a href=\"NewsServlet?uid=" + uid + "&aid=" + aid + "&add=true\">Add New News</a>");
    }

    private void renderActor(PrintWriter out, Actor displayActor) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&aid=" + displayActor.getAID() + "&add=true\">Add</a> " +
                    "<a href=\"ActorServlet?uid=" + uid + "&aid=" + displayActor.getAID() + "&edit=true\">Edit</a></div>\n");
        }

        out.println("\t\t<form action=\"ActorServlet\" method=\"get\">");
        out.println("\t\t\t<h2>Actor search: </h2>");

        if (!uid.equals(""))
            out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");

        out.println("\t\t\t<input type=\"text\" name=\"name\" /><input type=\"submit\" value=\"Search\" />");
        out.println("\t\t</form>");

        out.println("<div><b>" + displayActor.getName() + "</b></div><br />");
        out.println("<div><b>Born:</b> " + displayActor.getDOB() + "</div>");
        out.println("<div><b>Short Bio:</b> " + displayActor.getBio() + "</div>");

        renderActorTitles(out);
        renderQuotes(out);
        renderAwards(out);
        renderTrivia(out);
        renderNews(out);
    }

    private void resetValues() {
        aid = -1;
        tid = -1;
        uRating = -1;
        aName = "";
        uid = "";
        bio = "";
        editMode = false;
        addMode = false;
        saveMode = false;
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        actMethods.closeDBConnection();
    }
}