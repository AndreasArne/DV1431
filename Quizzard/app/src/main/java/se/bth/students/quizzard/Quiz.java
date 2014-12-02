package se.bth.students.quizzard;

/**
 * Created by mihai on 2014-12-02.
 */
public class Quiz {
    private Question[] questions = null;
    private String course;
    private String name;
    private String author;
    boolean finished = false; // if false this quiz has still to be completed with new questions, right answers etc.

    public Question[] getQuestions() {
        return this.questions;
    }

    public String getCourse() {
        return this.course;
    }

    public String getName() {
        return this.course;
    }

    public String getAuthor() {
        return this.author;
    }

    void setCourse(String course) {
        this.course = course;
    }

    void setName(String name) {
        this.name = name;
    }

    void setAuthor(String author) {
        this.author = author;
    }
}
