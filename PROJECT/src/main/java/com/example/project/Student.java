package com.example.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname  = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty dateOfBirth = new SimpleStringProperty();
    private StringProperty universityName = new SimpleStringProperty();
    public Student(String name, String surname, String email, String password, String dateOfBirth, String uniName) {
        this.name.set(name);
        this.surname.set(surname);
        this.email.set(email);
        this.password.set(password);
        this.dateOfBirth.set(dateOfBirth);
        this.universityName.set(uniName);

    }
    public StringProperty getName() { return name; }
    public StringProperty getSurname() { return surname; }
    public StringProperty getEmail() { return email; }
    public StringProperty getPassword() { return password; }
    public StringProperty getDateOfBirth() { return dateOfBirth; }
    public StringProperty getUniversityName() { return universityName; }
}
