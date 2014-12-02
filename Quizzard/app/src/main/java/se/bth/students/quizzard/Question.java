package se.bth.students.quizzard;

/**
 * Created by mihai on 2014-12-02.
 */
public class Question {
    private String answers[] = null;
    private int[] rightAnswers = null;

    public void Question() {

    }

    public String[] getAnswers() {
        return this.answers;
    }

    public int[] getRightAnswers() {
        return this.rightAnswers;
    }

}
