import java.sql.DatabaseMetaData;

/**
 *
 * TitleActorRole Class
 *
 */
public class TitleActorRole extends Title {

    private String aRole;

    /********** Constructors **********/

    public TitleActorRole(int tid) {
        super(tid);
    }

    public TitleActorRole(int tid, String name, String genre, int year, String synopsis, String type, String role) {
        super(tid, name, genre, year, synopsis, type);
        aRole = role;
    }

    /********** Getters **********/

    public String getRole() {
        return aRole;
    }

    /********** Setters **********/

    public void setRole(String role) {
        aRole = role;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(TID: " + String.valueOf(super.getTID()) + 
                ") Name: " + super.getName() + 
                " Genre: " + super.getGenre() + 
                " Year: " + String.valueOf(super.getYear()) +
                " Synopsis: " + super.getSynopsis() +
                " Type: " + super.getTitleType() +
                " Role: " + aRole;
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
