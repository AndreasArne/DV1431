package se.bth.students.quizzard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class Question implements Serializable {


    private String questionText = "some_question";
    private ArrayList<Answer> answers = new ArrayList<Answer>();
    private boolean hasUniqueRightAnswer;

    public Question() {this.questionText = "N/A";}

    public Question(String question) {
        this.questionText = question;
    }

    /**
     * adds a new answer to this question
     * @param answer
     * @param right
     */
    public void addAnswer(String answer, boolean right) {
        Answer ans = new Answer(answer,right);
        this.answers.add(ans);
    }

    public ArrayList<Answer> getAnswers() {
        return this.answers;
    }
    public void attachAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    /**
     *
     * @return a list of Answer objects that belong to this question and are marked as 'right'
     */
    public ArrayList<Answer> getRightAnswers() {
        ArrayList<Answer> rightAnswers = new ArrayList<Answer>();
        for (Answer ans:this.answers) {
            if (ans.isRight())
                rightAnswers.add(ans);
        }

        return rightAnswers;
    }

    public void setHasUniqueRightAnswer(boolean hasUniqueRightAnswer) {
        this.hasUniqueRightAnswer = hasUniqueRightAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean hasUniqueRightAnswer() {
        int right = 0;
        for (int i=0; right < 2 & i<answers.size(); i++) {
            if (answers.get(i).isRight())
                right++;
        }
        return (right == 1);


    }

/*    *//**
     *
     * @return true if this question has only one right answer, false if it has multiple right answers
     *//*
    public boolean hasUniqueRightAnswer() {
        boolean ret;
        int counter = 0;
        for (Answer ans:this.answers) {
            if (ans.isRight())
                counter++;
        }

        if (counter == 1)
            ret = true;
        else ret = false;

        return ret;
    }*/



}
