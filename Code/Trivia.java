import java.sql.DatabaseMetaData;

/**
 *
 * Trivia Class
 *
 */
public class Trivia {

    private int triviaID;
    private int actorID;
    private String triviaText;

    /********** Constructors **********/

    public Trivia(int trvid, int aid, String trvText) {
        triviaID = trvid;
        actorID = aid;
        triviaText = trvText;
    }

    /********** Getters **********/

    public int getTrivaiID() {
        return triviaID;
    }

    public int getAID() {
        return actorID;
    }

    public String getTrivia() {
        return triviaText;
    }

    /********** Setters **********/

    public void setTrivaiID(int trvid) {
        triviaID = trvid;
    }

    public void setAID(int aid) {
        actorID = aid;
    }

    public void setTrivia(String trvText) {
        triviaText = trvText;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(TrvID: " + String.valueOf(triviaID) + 
                ") AID: " + String.valueOf(actorID) + 
                " Trivia: " + triviaText;
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(triviaID) + 
                "</td><td>" + String.valueOf(actorID) + 
                "</td><td>" + triviaText +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"TrvID\" />" + String.valueOf(triviaID) +
               "</td><td><input type=\"password\" name=\"AID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"text\" name=\"trivia\" />" + triviaText +
               "</td><td></tr>";
    }
}
