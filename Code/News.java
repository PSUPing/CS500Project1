import java.sql.DatabaseMetaData;

/**
 *
 * News Class
 *
 */
public class News {

    private int newsID;
    private int actorID;
    private String newsSrc;
    private String newsURL;

    /********** Constructors **********/

    public News(int nid, int aid, String src, String url) {
        newsID = nid;
        actorID = aid;
        newsSrc = src;
        newsURL = url;
    }

    /********** Getters **********/

    public int getNID() {
        return newsID;
    }
  
    public int getAID() {
        return actorID;
    }

    public String getNewsSource() {
        return newsSrc;
    }

    public String getNewsURL() {
        return newsURL;
    }

    /********** Setters **********/

    public void setNID(int nid) {
        newsID = nid;
    }

    public void setAID(int aid) {
        actorID = aid;
    }

    public void setNewsSource(String src) {
        newsSrc = src;
    }

    public void setNewsURL(String url) {
        newsURL = url;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(NID: " + String.valueOf(newsID) + 
                ") TID: " + String.valueOf(actorID) + 
                " Source: " + newsSrc +
                " Text: " + newsURL;
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(newsID) + 
                "</td><td>" + String.valueOf(actorID) + 
                "</td><td>" + newsSrc +
                "</td><td>" + newsURL +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"NID\" />" + String.valueOf(newsID) +
               "</td><td><input type=\"password\" name=\"AID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"text\" name=\"newsSrc\" />" + newsSrc +
               "</td><td><input type=\"text\" name=\"newsURL\" />" + newsURL + 
               "</td><td></tr>";
    }
}
