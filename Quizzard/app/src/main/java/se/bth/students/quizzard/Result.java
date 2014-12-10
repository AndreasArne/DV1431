package se.bth.students.quizzard;

import java.io.Serializable;

/**
 * Created by asdf on 2014-12-10.
 * Serializable container class for sending quiz results to the score screen.
 */
public class Result implements Serializable {
    private String questionText;
    private boolean result;
    public Result(String textArg, boolean resultArg) {
        this.questionText = textArg;
        this.result = resultArg;
    }

    public String getText() {
        return this.questionText;
    }

    public boolean getResult() {
        return this.result;
    }
}
