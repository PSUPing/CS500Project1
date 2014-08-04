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
    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

    public String openDBConnection(String dbUser, String dbPass, String dbSID, String dbHost, int port) {

        String res="";
        if (conn != null) {
            closeDBConnection();
        }

        try {
            conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            System.out.println("Opened a connection");
            res = DBUtils.testConnection(conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
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

    public Connection getConn() {
        return conn;
    }

    /**
     * Create a new actor in the database.
     * @param newActor
     * @return
     */
    public Actor addActor(Actor newActor) {
        try {
            int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(aid) FROM actors");

            String query = "INSERT INTO actors VALUES (" + sid + ", '" + newActor.getName() + "', " +
                    "to_date('" + df.format(newActor.getDOB()) + "', 'MM/DD/YYYY')" + ", '" +
                    newActor.getBio() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newActor;
    }

    /**
     * Create a new user rating in the database.
     * @param addRating
     * @return
     */
    public Rating addActorTitleRating(Rating addRating) {
        try {
            String query = "INSERT INTO ratings (aid, tid, userid, score) VALUES (" + addRating.getAID() + ", " + addRating.getTID() +
                    ", '" + addRating.getUID() + "', " + addRating.getScore() + ")";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return addRating;
    }

    /**
     * Update an new actor in the database.
     * @param changedActor
     * @return
     */
    public Actor updateActor(Actor changedActor) {
        Actor actor = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM actors WHERE aid = " + changedActor.getAID());

            if (cnt == 0)
                return actor;

            String query = "UPDATE actors SET name = '" + changedActor.getName() +
                    "', dob = to_date('" + df.format(changedActor.getDOB()) + "', 'MM/DD/YYYY')" +
                    ", bio = '" + changedActor.getBio() + "' WHERE aid = " + changedActor.getAID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT aid, name, dob, bio FROM actors WHERE aid = " + changedActor.getAID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(changedActor.getAID(), result.getString("name"), result.getDate("dob"), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Update a user rating in the database.
     * @param changedTitle
     * @return
     */
    public Rating updateRating(Rating changedRating) {
        Rating rating = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM ratings WHERE tid = " + changedRating.getTID() +
                    " AND aid = " + changedRating.getAID() + " AND userid = '" + changedRating.getUID() + "'");

            if (cnt == 0)
                return rating;

            String query = "UPDATE ratings SET score = " + changedRating.getScore() + " WHERE tid = " + changedRating.getTID() +
                    " AND aid = " + changedRating.getAID() + " AND userid = '" + changedRating.getUID() + "'";
            DBUtils.executeUpdate(conn, query);

            query = "SELECT tid, aid, userid, score FROM rating WHERE tid = " + changedRating.getTID() +
                    " AND aid = " + changedRating.getAID() + " AND userid = '" + changedRating.getUID() + "'";

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            rating = new Rating(result.getInt("tid"), result.getInt("aid"), result.getString("userid"), result.getInt("score"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return rating;
    }

    /**
     * Create a new user rating in the database.
     * @param removeRating
     */
    public void removeActorTitleRating(Rating removeRating) {
        try {
            String query = "DELETE FROM ratings WHERE aid = " + removeRating.getAID() + " AND tid = " + removeRating.getTID() +
                    " AND userid = '" + removeRating.getUID() + "'";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }
    }

    public boolean hasActorTitleRating(Rating ratingQuery) {
        int cnt = 0;

        try {
            cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM ratings WHERE aid = " + ratingQuery.getAID() + " AND tid = " + ratingQuery.getTID() +
                    " AND userid = '" + ratingQuery.getUID() + "'");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        if (cnt == 0)
            return false;
        else
            return true;
    }

    /**
     * Get an new actor from the database by the primary key.
     * @param aid
     * @return
     */
    public Actor getActor(int aid) {
        Actor actor = null;

        try {
            String query = "SELECT aid, name, dob, bio FROM actors WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio"));

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
     * @return
     */
    public ArrayList getActorByName(String name) {
        ArrayList actors = new ArrayList();

        try {
            String query = "SELECT aid, name, dob, bio FROM actors WHERE UPPER(name) LIKE UPPER('" + name + "%')";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next() )
                actors.add(new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actors;
    }

    /**
     * Grab the first 10 actors from the DB.
     * @return
     */
    public ArrayList getRecentActors() {
        ArrayList actors = new ArrayList();
        int currCount = 0;

        try {
            String query = "SELECT aid, name, dob, bio FROM actors ORDER BY aid DESC";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next() && currCount <= 10)
                actors.add(new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return actors;
    }

    /**
     * Grab the titles for an Actor and their role in that title.
     * @param aid
     * @return
     */
    public ArrayList getTitlesAndRole(int aid) {
        ArrayList extendedTitles = new ArrayList();

        try {
            String query = "SELECT t.tid AS tid, name, genre, year, synopsis, title_type, role FROM titles t INNER JOIN Actors_Role_In ar ON (t.tid = ar.tid) WHERE ar.aid = " +
                    aid + " ORDER BY title_type, name";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next())
                extendedTitles.add(new TitleActorRole(result.getInt("tid"), result.getString("name"), result.getString("genre"),
                        result.getInt("year"), result.getString("synopsis"), result.getString("title_type"), result.getString("role")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return extendedTitles;
    }

    /**
     * Grab the titles for an Actor, their role in that title and the users rating.
     * @return
     */
    public ArrayList getTitlesAndUserRating(int aid, String uid) {
        ArrayList ratedTitles = new ArrayList();

        try {
            String query = "SELECT a.tid AS tid, name, genre, year, synopsis, title_type, role, score " +
                            "FROM (SELECT t.tid AS tid, ar.aid AS aid, name, genre, year, synopsis, title_type, role " +
                                 "FROM titles t INNER JOIN Actors_Role_In ar ON (t.tid = ar.tid) WHERE ar.aid = " + aid + ") a " +
                                 "LEFT OUTER JOIN (SELECT aid, score FROM ratings WHERE userid = '" + uid + "') r ON (a.aid = r.aid)";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next())
                ratedTitles.add(new ActorTitleRating(result.getInt("tid"), result.getString("name"), result.getString("genre"),
                        result.getInt("year"), result.getString("synopsis"), result.getString("title_type"), result.getString("role"),
                        uid, result.getInt("score")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return ratedTitles;
    }

    /**
     * Get actor quotes from the database by the actor ID.
     * @param aid
     * @return
     */
    public ArrayList getQuotesByActor(int aid) {
        ArrayList quotes = new ArrayList();

        try {
            String query = "SELECT qid, qt, aid FROM quotes WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next())
                quotes.add(new Quote(result.getInt("qid"), result.getInt("aid"), result.getString("qt")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return quotes;
    }

    /**
     * Get actor awards from the database by the actor ID.
     * @param aid
     * @return
     */
    public ArrayList getAwardsByActor(int aid) {
        ArrayList awards = new ArrayList();

        try {
            String query = "SELECT awid, aid, nomination_date, award_date FROM awards WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next())
                awards.add(new Award(result.getInt("awid"), result.getInt("aid"), result.getDate("nomination_date"), result.getDate("award_date")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return awards;
    }

    /**
     * Get actor awards from the database by the actor ID.
     * @param aid
     * @return
     */
    public ArrayList getTriviaByActor(int aid) {
        ArrayList trivias = new ArrayList();

        try {
            String query = "SELECT trvid, aid, trivia_text FROM trivia WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next())
                trivias.add(new Trivia(result.getInt("trvid"), result.getInt("aid"), result.getString("trivia_text")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return trivias;
    }
    //getNewsByActor

    /**
     * Get actor news from the database by the actor ID.
     * @param aid
     * @return
     */
    public ArrayList getNewsByActor(int aid) {
        ArrayList newses = new ArrayList();

        try {
            String query = "SELECT nid, aid, news_source, news_url FROM news WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next())
                newses.add(new News(result.getInt("nid"), result.getInt("aid"), result.getString("news_source"), result.getString("news_url")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newses;
    }
}