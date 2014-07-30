import java.sql.DatabaseMetaData;

/**
 *
 * Rating Class
 *
 */
public class Rating {

    private int actorID;
    private int titleID;
    private int userID;
    private int ratingScore;

    /********** Constructors **********/

    public Rating(int aid, int tid, int uid, int score) {
        actorID = aid;
        titleID = tid;
        userID = uid;
        ratingScore = score;
    }

    /********** Getters **********/

    public int getAID() {
        return actorID;
    }

    public int getTID() {
        return titleID;
    }

    public int getUID() {
        return userID;
    }

    public int getScore() {
        return ratingScore;
    }

    /********** Setters **********/

    public void setAID(int aid) {
        actorID = aid;
    }

    public void setTID(int tid) {
        titleID = tid;
    }

    public void setUID(int uid) {
        userID = uid;
    }

    public void setScore(int score) {
        ratingScore = score;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(AID: " + String.valueOf(actorID) + 
                " TID: " + String.valueOf(titleID) + 
                " UID: " + String.valueOf(userID) + 
                ") Score: " + String.valueOf(ratingScore);
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(actorID) + 
                "</td><td>" + String.valueOf(titleID) + 
                "</td><td>" + String.valueOf(userID) + 
                "</td><td>" + String.valueOf(ratingScore) +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"AID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"password\" name=\"TID\" />" + String.valueOf(titleID) +
               "</td><td><input type=\"text\" name=\"UID\" />" + String.valueOf(userID) +
               "</td><td><input type=\"text\" name=\"score\" />" + String.valueOf(ratingScore) + "</td><td></tr>";
    }
}
