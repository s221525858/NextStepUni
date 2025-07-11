package com.example.project;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ViewUniReviews {
    private IntegerProperty UniversityReviewID = new SimpleIntegerProperty();
    private IntegerProperty UniversityID = new SimpleIntegerProperty();
    private IntegerProperty StudentID = new SimpleIntegerProperty();
    private StringProperty Content = new SimpleStringProperty();
    private IntegerProperty Rating = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> Dateposted = new SimpleObjectProperty<>();

    public IntegerProperty getUniversityReviewID() {
        return UniversityReviewID;
    }
    public IntegerProperty getUniversityID() {
        return UniversityID;
    }
    public IntegerProperty getStudentID() {
        return StudentID;
    }
    public StringProperty getContent() {
        return Content;
    }
    public IntegerProperty getRating() {
        return Rating;
    }
    public LocalDate getDateposted() {
        return Dateposted.get();
    }
    public ObjectProperty<LocalDate> DatepostedProperty() {
        return Dateposted;
    }
    public ViewUniReviews(int UnireviewID, int UniversityID, int StudentID, String Content, int Rating, LocalDate Dateposted) {
        this.UniversityReviewID.set(UnireviewID);
        this.UniversityID.set(UniversityID);
        this.StudentID.set(StudentID);
        this.Content.set(Content);
        this.Rating.set(Rating);
        this.Dateposted.set(Dateposted);
    }
}
