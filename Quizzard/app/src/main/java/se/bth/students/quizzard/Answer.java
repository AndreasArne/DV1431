package se.bth.students.quizzard;

/**
 * Created by mihai on 2014-12-02.
 */
public class Answer {
    private String answerText = "some_answer";
    private boolean isRight = false;

    public Answer(String answer, boolean isRight) {
        this.answerText = answer;
        this.isRight = isRight;
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public boolean isRight() {
        return this.isRight;
    }

    public void setAnswerText(String new_answer) {
        this.answerText = new_answer;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }
}
