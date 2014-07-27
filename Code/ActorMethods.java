import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Actor DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class ActorMethods {

    private static Connection conn = null;

    /**
     * Open the database connection.
     */
    public String openDBConnection(String dbUser, String dbPass, String dbSID, String dbHost, int port) {
        String res="";

        if (conn != null) {
            closeDBConnection();
        }

        try {
            conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            System.out.println("Opened a connection");
            res = DBUtils.testConnection(conn);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfEx) {
            cnfEx.printStackTrace(System.err);
        }

        return res;
    }

    /**
     * Close the database connection.
     */
    public void closeDBConnection() {
        try {
            DBUtils.closeDBConnection(conn);
            System.out.println("Closed a connection");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }
    }

    /**
     * Create a new actor in the database.
     * @param newActor
     * @return
     */
    public Actor addActor(Actor newActor) {
        try {
            String query = "INSERT INTO actors (name, dob, bio) VALUES ('" +
                    newActor.getName() + "', '" + newActor.getDOB() + "', '" + newActor.getBio() + "');";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newActor;
    }

    /**
     * Update an new actor in the database.
     * @param changedActor
     * @return
     */
    public Actor updateActor(Actor changedActor) {
        Actor actor = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM actors WHERE name = '" + changedActor.getName() +
                "' AND '" + changedActor.getDOB() + "';";

            if (cnt == 0)
                return actor;

            String query = "UPDATE actors SET bio = '" + changedActor.getBio() + "' WHERE name = '" + changedActor.getName() +
                    "' AND '" + changedActor.getDOB() + "';";
            DBUtils.executeUpdate(conn, query);

            query "SELECT name, dob, bio FROM actors WHERE name = '" + changedActor.getName() +
                    "' AND '" + changedActor.getDOB() + "';";

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(changedActor.getName(), changedActor.getDOB(), rs.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Get an new actor from the database by the primary key.
     * @param name
     * @param dob
     * @return
     */
    public Actor getActor(String name, java.sql.Date dob) {
        Actor actor = null;

        try {
            String query = "SELECT name, dob, bio FROM actors WHERE name = '" + name + "' AND '" + dob + "';";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(result.getString("name"), result.getDate("dob"), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Get all the actors.
     * @return
     */
    public ArrayList getSQLTopActors() {
        ArrayList actors = new ArrayList();

        try {
            String query = "SELECT TOP 25 name, dob, bio FROM actors;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (rs.next())
                actors.add(new Actor(result.getString("name"), result.getDate("dob"), result.getString("bio")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actors;
    }

    /**
     * Get all the actors.
     * @return
     */
    public ArrayList getAllActors() {
        ArrayList actors = new ArrayList();

        try {
            String query = "SELECT name, dob, bio FROM actors;";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (rs.next())
                actors.add(new Actor(result.getString("name"), result.getDate("dob"), result.getString("bio")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actors;
    }

/*    public static void main (String args[]) {

        if (args.length < 4) {
            System.out.println("Not enough arguments: Registrar dbUser dbPass dbSID dbHost");
            System.exit(0);
        }
        String dbUser = args[0].trim();
        String dbPass = args[1].trim();
        String dbSID = args[2].trim();
        String dbHost = args[3].trim();
        int dbPort = 1521;

        Registrar reg = new Registrar();
        try {
            String response = reg.openDBConnection(dbUser, dbPass, dbSID, dbHost, dbPort);
            System.out.println(response);

            Student newStudent = reg.registerStudent(new Student("Julia"));
            System.out.println("\nRegistered a new student: " + newStudent.toString());

            newStudent = reg.setGPA(newStudent.getId(), 3.9);
            System.out.println("\nUpdated GPA for student: " + newStudent.toString());

            // Student [] roster = reg.getRoster();
            ArrayList roster = reg.getRoster();
            System.out.println("\nPrinting the roster");
            //for (Student student : roster) {
            for (int i=0; i<roster.size(); i++) {
                Student student = (Student)roster.get(i);
                System.out.println(student.toString());
            }

            String [] terms = {"Summer 2010", "Fall 2010", "Spring 2011", "Summer 2011"};
            reg.addTermsDynamicSQL(terms);

            String [] moreTerms = {"Summer 2012", "Fall 2012"};
            reg.addTermsPreparedStatement(moreTerms);

        } catch (RuntimeException rte) {
            rte.printStackTrace();
        } finally {
            reg.closeDBConnection();
        }
    }*/
}