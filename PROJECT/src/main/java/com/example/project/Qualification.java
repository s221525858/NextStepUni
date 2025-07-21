package com.example.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Qualification {
    public StringProperty qualificationName = new SimpleStringProperty();
    public StringProperty faculty = new SimpleStringProperty();
    public StringProperty type = new SimpleStringProperty();
    public Qualification(String qualificationName, String type, String faculty) {
        this.qualificationName.set(qualificationName);
        this.faculty.set(faculty);
        this.type.set(type);
    }
    public StringProperty getQualificationName() {
        return qualificationName;
    }
    public StringProperty getFaculty() {  return faculty;  }
    public StringProperty getType() {  return type;  }
}
