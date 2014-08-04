import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
*
* Trivia DB CRUD (minus the D) Methods for use in the Servlet classes.
*
*/
public class TriviaMethods {

   private static Connection conn = null;
   private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

   public void setConn(Connection connect) {
       conn = connect;
   }

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

   /**
    * Create a new trivia in the database.
    * @param newTrivia
    * @return
    */
   public Trivia addTrivia(Trivia newTrivia) {
       try {
           int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(trvid) FROM trivia");

           String query = "INSERT INTO trivia (trvid, aid, trivia_text) VALUES (" + sid + ", " + newTrivia.getAID() +
                   ", '" + newTrivia.getTrivia() + "')";
           DBUtils.executeUpdate(conn, query);
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return newTrivia;
   }

   /**
    * Update a trivia in the database.
    * @param changedTrivia
    * @return
    */
   public Trivia updateTrivia(Trivia changedTrivia) {
       try {
           int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM trivia WHERE trvid = " + changedTrivia.getTriviaID());

           if (cnt == 0)
               return changedTrivia;

           String query = "UPDATE Trivia SET aid = " + changedTrivia.getAID() + ", trivia_text = '" + changedTrivia.getTrivia() +
                   "' WHERE trvid = " + changedTrivia.getTriviaID();
           DBUtils.executeUpdate(conn, query);

           query = "SELECT trvid, trivia_text, aid FROM trivia WHERE trvid = " + changedTrivia.getTriviaID();

           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           changedTrivia = new Trivia(result.getInt("trvid"), result.getInt("aid"), result.getString("trivia_text"));
 
           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return changedTrivia;
   }

   /**
    * Get a trivia from the database by the primary key.
    * @param nid
    * @return
    */
   public Trivia getTrivia(int trvid) {
       Trivia trivia = null;

       try {
           String query = "SELECT trvid, aid, trivia_text FROM trivia WHERE trvid = " + trvid;
           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           trivia = new Trivia(result.getInt("trvid"), result.getInt("aid"), result.getString("trivia_text"));

           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return trivia;
   }
}