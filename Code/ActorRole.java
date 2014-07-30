import java.sql.DatabaseMetaData;

/**
 *
 * ActorRole Class
 *
 */
public class ActorRole {

    private int actorID;
    private int titleID;
    private String aRole;

    /********** Constructors **********/

    public ActorRole(int aid, int tid) {
        actorID = aid;
        titleID = tid;
    }

    public ActorRole(int aid, int tid, String role) {
        actorID = aid;
        titleID = tid;
        aRole = role;
    }

    /********** Getters **********/

    public int getAID() {
        return actorID;
    }

    public int getTID() {
        return titleID;
    }

    public String getRole() {
        return aRole;
    }

    /********** Setters **********/

    public void setAID(int aid) {
        actorID = aid;
    }

    public void setTID(int tid) {
        titleID = tid;
    }

    public void setRole(String role) {
        aRole = role;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(AID: " + String.valueOf(actorID) + 
                " TID: " + String.valueOf(titleID) + 
                ") Role: " + aRole;
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(actorID) + 
                "</td><td>" + String.valueOf(titleID) + 
                "</td><td>" + aRole + 
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"AID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"password\" name=\"TID\" />" + String.valueOf(titleID) +
               "</td><td><input type=\"text\" name=\"role\" />" + aRole + "</td><td></tr>";
    }
}
