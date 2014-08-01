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
    private String aName = "";
    private String uid = "";
    private String bio;
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
                        bio = tempString[1].replace('+', ' ');
                    else if (tempString[0].equals("name"))
                        aName = tempString[1].replace('+', ' ');
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
                Actor actor = new Actor(aid, aName, dob, bio);

                if (editMode)
                    actor = actMethods.updateActor(actor);
                else if (addMode)
                    actor = actMethods.addActor(actor);

                renderActor(out, actor);
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

                    out.println("\t\t<form action=\"ActorServlet\" method=\"get\">");
                    out.println("\t\t\t<h2>Actor search: </h2>");

                    if (!uid.equals(""))
                        out.println("\t\t\t<input type=\"hidden\" name=\"uid\" value=\"" + uid + "\" />");

                    out.println("\t\t\t<input type=\"text\" name=\"name\" /><input type=\"submit\" value=\"Search\" />");
                    out.println("\t\t</form>");

                    renderActorTable(out, actMethods.getRecentActors());
                }
                else {
                    if (aid > -1)
                        renderActor(out, actMethods.getActor(aid));
                    else {
                        ArrayList actors = actMethods.getActorByName(aName);

                        if (actors.size() > 1) {
                            if (!uid.equals(""))
                                out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&add=true\">Add</a></div>");

                            renderActorTable(out, actors);
                        }
                        else {
                            Actor actor = (Actor) actors.get(0);
                            renderActor(out, actor);
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

    private void renderActor(PrintWriter out, Actor displayActor) {
        if (!uid.equals("")) {
            out.println("\t\t<div style=\"text-align:right\"><a href=\"ActorServlet?uid=" + uid + "&aid=" + displayActor.getAID() + "&add=true\">Add</a> " +
                    "<a href=\"ActorServlet?uid=" + uid + "&aid=" + displayActor.getAID() + "&edit=true\">Edit</a></div>\n");
        }

        out.println("<div><b>" + displayActor.getName() + "</b></div><br />");
        out.println("<div><b>Born:</b> " + displayActor.getDOB() + "</div><br />");
        out.println("<div><b>Short Bio:</b> " + displayActor.getBio() + "</div><br />");
    }

    private void resetValues() {
        aid = -1;
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