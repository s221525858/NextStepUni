package com.example.project;

import javafx.beans.property.*;

import java.time.LocalDate;

public class WriteBursaryReview {
    private IntegerProperty BursaryReviewID = new SimpleIntegerProperty();
    private IntegerProperty BursaryID = new SimpleIntegerProperty();
    private IntegerProperty StudentID = new SimpleIntegerProperty();
    private StringProperty Content = new SimpleStringProperty();
    private IntegerProperty Rating = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> Dateposted = new SimpleObjectProperty<>();

    public IntegerProperty getBursaryReviewID() {
        return BursaryReviewID;
    }
    public IntegerProperty getBursaryID() {
        return BursaryID;
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
    public WriteBursaryReview(int BursaryreviewID, int BursaryID, int StudentID, String Content, int Rating, LocalDate Dateposted) {
        this.BursaryReviewID.set(BursaryreviewID);
        this.BursaryID.set(BursaryID);
        this.StudentID.set(StudentID);
        this.Content.set(Content);
        this.Rating.set(Rating);
        this.Dateposted.set(Dateposted);
    }
}
