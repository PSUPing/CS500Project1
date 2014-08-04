import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
*
* Quote DB CRUD (minus the D) Methods for use in the Servlet classes.
*
*/
public class QuoteMethods {

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
    * Create a new quote in the database.
    * @param newQuote
    * @return
    */
   public Quote addQuote(Quote newQuote) {
       try {
           int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(qid) FROM quotes");

           String query = "INSERT INTO quotes VALUES (" + sid + ", '" + newQuote.getQuote() + "', " +
                   newQuote.getAID() + "')";
           DBUtils.executeUpdate(conn, query);
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return newQuote;
   }

   /**
    * Update an new quote in the database.
    * @param changedQuote
    * @return
    */
   public Quote updateQuote(Quote changedQuote) {
       Quote quote = null;

       try {
           int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM Quotes WHERE qid = " + changedQuote.getQID());

           if (cnt == 0)
               return quote;

           String query = "UPDATE quotes SET qt = '" + changedQuote.getQuote() + "',  aid = " +
        		   changedQuote.getAID() + "' WHERE qid = " + changedQuote.getQID();
           DBUtils.executeUpdate(conn, query);

           query = "SELECT qid, qt, aid FROM quotes WHERE qid = " + changedQuote.getQID();

           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           quote = new Quote(changedQuote.getQID(), result.getInt("aid"), result.getString("qt"));

           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return quote;
   }

   /**
    * Get an new quote from the database by the primary key.
    * @param qid
    * @return
    */
   public Quote getQuote(int qid) {
       Quote quote = null;

       try {
           String query = "SELECT qid, qt, aid FROM quotes WHERE qid = " + qid;
           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           quote = new Quote(result.getInt("qid"), result.getInt("aid"), result.getString("qt"));

           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return quote;
   }

}