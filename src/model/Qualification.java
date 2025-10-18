package model;

public class Qualification {
    private int qualID;
    private String qualType;
    private String qualFaculty;

    public Qualification(int qualID, String qualType, String qualFaculty) {
        this.qualID = qualID;
        this.qualType = qualType;
        this.qualFaculty = qualFaculty;
    }

    public int getQualID() {
        return qualID;
    }

    @Override
    public String toString() {
        return qualType + " (" + qualFaculty + ")";
    }

}