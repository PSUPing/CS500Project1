import java.sql.DatabaseMetaData;

/**
 *
 * TitleActorRole Class
 *
 */
public class ActorTitleRating extends TitleActorRole {

    private String uid;
    private int userRating;

    /********** Constructors **********/

    public ActorTitleRating(int tid) {
        super(tid);
    }

    public ActorTitleRating(int tid, String name, String genre, int year, String synopsis, String type, String role, String userID, int rating) {
        super(tid, name, genre, year, synopsis, type, role);
        uid = userID;
        userRating = rating;
    }

    /********** Getters **********/

    public String getUID() {
        return uid;
    }

    public int getUserRating() {
        return userRating;
    }

    /********** Setters **********/

    public void setUID(String userID) {
        uid = userID;
    }

    public void setUserRating(int rating) {
        userRating = rating;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(TID: " + String.valueOf(super.getTID()) +
                ") Name: " + super.getName() +
                " Genre: " + super.getGenre() +
                " Year: " + String.valueOf(super.getYear()) +
                " Synopsis: " + super.getSynopsis() +
                " Type: " + super.getTitleType() +
                " Role: " + super.getRole() +
                " User: " + uid +
                " Rating: " + userRating;
    }

/*    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(titleID) +
                "</td><td>" + titleName +
                "</td><td>" + titleGenre +
                "</td><td>" + String.valueOf(titleYear) +
                "</td><td>" + titleSynopsis +
                "</td><td>" + titleType +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"TID\" />" + String.valueOf(titleID) +
               "</td><td><input type=\"password\" name=\"tName\" />" + titleName +
               "</td><td><input type=\"text\" name=\"tGenre\" />" + titleGenre +
               "</td><td><input type=\"text\" name=\"tYear\" />" + String.valueOf(titleYear) +
               "</td><td><input type=\"text\" name=\"tSynopsis\" />" + titleSynopsis +
               "</td><td><input type=\"text\" name=\"tTYpe\" />" + titleType + "</td><td></tr>";
    }*/
}
