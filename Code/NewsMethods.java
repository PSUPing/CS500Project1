import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
*
* News DB CRUD (minus the D) Methods for use in the Servlet classes.
*
*/
public class NewsMethods {

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
    * Create a new news in the database.
    * @param newNews
    * @return
    */
   public News addNews(News newNews) {
       try {
           int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(nid) FROM news");

           String query = "INSERT INTO news (nid, aid, news_source, news_url) VALUES (" + sid + ", " + newNews.getAID() +
                   ", " + newNews.getNewsSource() + ", '" + newNews.getNewsURL() + "')";
           DBUtils.executeUpdate(conn, query);
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return newNews;
   }

   /**
    * Update a news in the database.
    * @param changedNews
    * @return
    */
   public News updateNews(News changedNews) {
       try {
           int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM news WHERE nid = " + changedNews.getNID());

           if (cnt == 0)
               return changedNews;

           String query = "UPDATE News SET aid = " + changedNews.getAID() + ", news_source = " + changedNews.getNewsSource() +
                   ", news_url = '" + changedNews.getNewsURL() +
                   "' WHERE nid = " + changedNews.getNID();
           DBUtils.executeUpdate(conn, query);

           query = "SELECT nid, news_url, aid, news_source FROM news WHERE nid = " + changedNews.getNID();

           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           changedNews = new News(result.getInt("nid"), result.getInt("aid"), result.getString("news_source"), result.getString("news_url"));
 
           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return changedNews;
   }

   /**
    * Get a news from the database by the primary key.
    * @param nid
    * @return
    */
   public News getNews(int nid) {
       News news = null;

       try {
           String query = "SELECT nid, aid, news_source, news_url FROM news WHERE nid = " + nid;
           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           news = new News(result.getInt("nid"), result.getInt("aid"), result.getString("news_source"), result.getString("news_url"));

           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return news;
   }
}