package se.bth.students.quizzard;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class Quiz implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private String course = "some_course";
    private String name = "some_name";
    private String author = "some_author";
    private boolean isFavorite = false;
    double lastResult = 0.0;
    private int rating = 0;
    private int numberOfRates = 0;
    boolean finished = false; // if false this quiz has still to be completed with new questions, right answers etc.

    public Quiz() {
        this.course = "N/A";
        this.name = "N/A";
        this.author = "N/A";
        this.isFavorite = false;
    }

    public Quiz(String name, String course, String author, boolean makeAsFavorite) {
        this.name = name;
        this.course = course;
        this.author = author;
        this.isFavorite = makeAsFavorite;
    }

    public void attachQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public String getCourse() {
        return this.course;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public boolean isFavorite() { return this.isFavorite; }

    void setCourse(String course) {
        this.course = course;
    }

    void setName(String name) {
        this.name = name;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    void setAsFavorite(boolean makeAsFavorite) {
        this.isFavorite = makeAsFavorite;
    }
    void addQuestion(Question question) {
        this.questions.add(question);
    }

    public double getLastResult() {
        return lastResult;
    }

    public void setLastResult(double lastResult) {
        this.lastResult = lastResult;
    }


    public int rate(float rate){
        this.rating+=rate;
        this.numberOfRates++;
        return this.rating/this.numberOfRates;
    }
    public int getRating(){
        if(this.rating != 0)
            return this.rating/this.numberOfRates;
        else
            return 0;
    }


            @Override
    public String toString() {
        String s = "";
        s = this.name;
        return s;
    }

}
