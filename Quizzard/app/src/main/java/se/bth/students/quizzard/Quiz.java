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
    double lastResult = 0.0;
    private float rating = 0;
    private int numberOfRates = 0;
    boolean finished = false; // if false this quiz has still to be completed with new questions, right answers etc.

    public Quiz() {
        this.course = "N/A";
        this.name = "N/A";
        this.author = "N/A";
    }

    public Quiz(String name, String course, String author) {
        this.name = name;
        this.course = course;
        this.author = author;
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

    void setCourse(String course) {
        this.course = course;
    }

    void setName(String name) {
        this.name = name;
    }

    void setAuthor(String author) {
        this.author = author;
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

    public float rate(float rate){
        this.rating = (this.rating+rate)/(this.numberOfRates+1);
        this.numberOfRates++;
        return this.rating;
    }
    public float getRating(){
        return this.rating;
    }

    @Override
    public String toString() {
        String s = "";
        s = this.name;
        return s;
    }

}
