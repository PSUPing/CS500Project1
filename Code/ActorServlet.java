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

    public void init() throws ServletException {
        bundle = ResourceBundle.getBundle("OraBundle");
        actMethods = new ActorMethods();
        message = actMethods.openDBConnection(bundle.getString("dbUser"), bundle.getString("dbPass"), bundle.getString("dbSID"),
                bundle.getString("dbHost"), Integer.parseInt(bundle.getString("dbPort")));
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int aid = -1;
        String aName = "";
        String uid = "";
        boolean editMode = false;
        boolean addMode = false;

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

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
                    else if (tempString[0].equals("edit"))
                        editMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("add"))
                        addMode = Boolean.valueOf(tempString[1]).booleanValue();
                    else if (tempString[0].equals("name"))
                        aName = tempString[1];
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
            if (!editMode && !addMode) {
                if (aName.equals("")) {
                    out.println("<form action=\"ActorServlet\" method=\"get\"><h2>Actor search: </h2><input type=\"text\" name=\"name\" /><input type=\"submit\" /></form>");
                    renderActorTable(out);
                }
                else {
                    renderActorByName(out, aName);
                }
            }
            else if (addMode) {
                renderActorTextBoxes(out, null);
            }
            else if (editMode) {
                renderActorTextBoxes(out, null);
            }
        }

        out.println(HTMLUtils.renderClosingTags());
    }

    private void renderActorTextBoxes(PrintWriter out, Actor actorToUpdate) {
        if (actorToUpdate != null) {
            out.println("<div>Name: <input type=\"text\" name=\"name\">" + actorToUpdate.getName() + "</input></div>");
            out.println("<div>Date of Birth: <input type=\"text\" name=\"dob\">" + actorToUpdate.getDOB() + "</input></div>");
            out.println("<div>Short Bio: <input type=\"text\" name=\"bio\">" + actorToUpdate.getBio() + "</input></div>");
        }
        else {
            out.println("<div>Name: <input type=\"text\" name=\"name\" /></div>");
            out.println("<div>Date of Birth: <input type=\"text\" name=\"dob\" /></div>");
            out.println("<div>Short Bio: <input type=\"text\" name=\"bio\" /></div>");

        }
    }

    private void renderActorTable(PrintWriter out) {
        ArrayList actors = actMethods.getFirst10Actors();

        out.println("\t\t<table>");

        for (int i = 0; i < actors.size(); i++) {
            Actor tempAct = (Actor)actors.get(i);
            out.println(tempAct.toViewHTML());
        }

        out.println("\t\t</table>");
    }

    private void renderActorByName(PrintWriter out, String name) {
        Actor actor = actMethods.getActorByName(name);

        out.println("<div><b>" + actor.getName() + "</b></div><br />");
        out.println("<div><b>Born:</b> " + actor.getDOB() + "</div><br />");
        out.println("<div><b>Short Bio:</b> " + actor.getBio() + "</div><br />");
    }

    private void renderActor(PrintWriter out, int aid) {
        Actor actor = actMethods.getActor(aid);

        out.println("<div><b>" + actor.getName() + "</b></div><br />");
        out.println("<div><b>Born:</b> " + actor.getDOB() + "</div><br />");
        out.println("<div><b>Short Bio:</b> " + actor.getBio() + "</div><br />");
    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void destroy() {
        actMethods.closeDBConnection();
    }
}