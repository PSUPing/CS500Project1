import java.sql.DatabaseMetaData;

/**
 *
 * Title Class
 *
 */
public class Title {

    private int titleID;
    private String titleName;
    private String titleGenre;
    private int titleYear;
    private String titleSynopsis;
    private String titleType;

    /********** Constructors **********/

    public Title(int tid) {
        titleID = tid;
    }

    public Title(int tid, String name, String genre, int year, String synopsis, String type) {
        titleID = tid;
        titleName = name;
        titleGenre = genre;
        titleYear = year;
        titleSynopsis = synopsis;
        titleType = type;
    }

    /********** Getters **********/

    public int getTID() {
        return titleID;
    }

    public String getName() {
        return titleName;
    }

    public String getGenre() {
        return titleGenre;
    }

    public int getYear() {
        return titleYear;
    }

    public String getSynopsis() {
        return titleSynopsis;
    }

    public String getTitleType() {
        return titleType;
    }

    /********** Setters **********/

    public void setTID(int tid) {
        titleID = tid;
    }

    public void setName(String name) {
        titleName = name;
    }

    public void setGenre(String genre) {
        titleGenre = genre;
    }

    public void setYear(int year) {
        titleYear = year;
    }

    public void setSynopsis(String synopsis) {
        titleSynopsis = synopsis;
    }

    public void setTitleType(String type) {
        titleType = type;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(TID: " + String.valueOf(titleID) + 
                ") Name: " + titleName + 
                " Genre: " + titleGenre + 
                " Year: " + String.valueOf(titleYear) +
                " Synopsis: " + titleSynopsis +
                " Type: " + titleType;
    }

    public String toViewHTML() {
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
    }
}
