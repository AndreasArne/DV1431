package se.bth.students.quizzard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-07.
 */
public class AddQuestion extends Activity  {

    //Quiz quiz;
    Question question;

    ArrayList<Answer> answers = new ArrayList<Answer>();
    ArrayList<RadioButton> rbs = new ArrayList<RadioButton>();
    ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();
    int radio_checked_id = -1;
    final int UNIQUE = Question.UNIQUE;
    final int MULTIPLE = Question.MULTIPLE;
    int question_type = UNIQUE;
    final int MAX_NR_ANSWERS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);

        if (savedInstanceState != null) {
            ArrayList<Answer> ans = (ArrayList<Answer>) savedInstanceState.getSerializable("answers");
            if (ans != null && ans.size() > 0) {
                this.answers = ans;
                int type = savedInstanceState.getInt("question_type");
                if (type == Question.UNIQUE) {
                    refreshAnswerListU();
                }
                else if (type == Question.MULTIPLE) {
                    refreshAnswerListM();
                }
            }
        }
    }

    public void saveQuestion(View v) {
        Log.i("mytag", "entered saveQuestion!");
        EditText questionTxt = (EditText) findViewById(R.id.question_text);
        String questionStr = questionTxt.getText().toString();

        if (questionStr != null && !questionStr.equals("")) {
            Question question = new Question(questionStr);
            question.setQuestionType(this.question_type);
            question.attachAnswers(this.answers);
            this.question = question;
           // this.quiz.addQuestion(question);
            //Log.i("mytag", "in saveQuestion: quiz name: " + quiz.getName() + ", nr of questions: " + quiz.getQuestions().size());
            saveAndFinish(Activity.RESULT_OK);
        }
        else Toast.makeText(getBaseContext(), "You must write the text of the question." , Toast.LENGTH_SHORT).show();
    }

    private void saveAndFinish(int code) {
        if (this.question != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Question", this.question);
            setResult(code, resultIntent);
        }

        finish();

    }

 /*   @Override
    public void onBackPressed() {

    }*/

    public void saveQuestionAddNew (View v) {
        Log.i("mytag", "entered saveQuestion!");
        EditText questionTxt = (EditText) findViewById(R.id.question_text);
        String questionStr = questionTxt.getText().toString();

        if (questionStr != null && !questionStr.equals("")) {
            Question question = new Question(questionStr);
            question.setQuestionType(this.question_type);
            question.attachAnswers(this.answers);
            this.question = question;
           // this.quiz.addQuestion(question);
            //Log.i("mytag", "in saveQuestion: quiz name: " + quiz.getName() + ", nr of questions: " + quiz.getQuestions().size());
            saveAndFinish(Activity.RESULT_FIRST_USER);
        }
        else Toast.makeText(getBaseContext(), "You must write the text of the question." , Toast.LENGTH_SHORT).show();

    }

    public void addAnswer(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestion.this);
        final EditText answer_txt = new EditText(this);
        builder.setView(answer_txt);

        builder.setMessage("Enter answer:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String answer = answer_txt.getText().toString();
                        if (answers.size() < MAX_NR_ANSWERS) {
                            answers.add(new Answer(answer, false));
                            if (question_type == UNIQUE)
                                refreshAnswerListU();
                            else if (question_type == MULTIPLE)
                                refreshAnswerListM();
                        }
                        else Toast.makeText(AddQuestion.this, "Too many answers, delete one first", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void refreshAnswerListU() {
        if (this.answers.size() > MAX_NR_ANSWERS) {
            Toast.makeText(AddQuestion.this, "Too many answers, delete one first", Toast.LENGTH_LONG).show();
        }
        else {
            LinearLayout ans_container = (LinearLayout) findViewById(R.id.answers_container);

            if (null != ans_container && ans_container.getChildCount() > 0) {
                try {
                    ans_container.removeViews(0, ans_container.getChildCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setOrientation(LinearLayout.VERTICAL);

            this.rbs.clear();
            for (int i=0; i<this.answers.size(); i++) {
                final int index = i;
                LinearLayout lineLayout = new LinearLayout(this);
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                RadioButton rb = new RadioButton(this);
                rb.setId(i);
                rb.setText(this.answers.get(i).getAnswerText());
                if (i == radio_checked_id)
                    rb.setChecked(true);
                else rb.setChecked(false);

                this.rbs.add(rb);

                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radio_checked_id = v.getId();
                        for (int i=0; i<answers.size(); i++) {
                            if (i == v.getId()) {
                                answers.get(i).setIsRight(true);
                            }
                            else answers.get(i).setIsRight(false);
                        }
                        refreshAnswerListU();
                    }
                });

                ImageButton delete_btn = new ImageButton (this);
                delete_btn.setImageResource(R.drawable.redx);
                delete_btn.setBackgroundColor(0xfff3f3f3);
                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAnswer(index);
                    }
                });

                lineLayout.addView(rb);
                lineLayout.addView(delete_btn);

                tempLayout.addView(lineLayout);

            }

            ans_container.addView(tempLayout);
        }
    }



    private void refreshAnswerListM() {
        if (this.answers.size() > MAX_NR_ANSWERS) {
            Toast.makeText(AddQuestion.this, "Too many answers, delete one first", Toast.LENGTH_LONG).show();
        }
        else {
            LinearLayout ans_container = (LinearLayout) findViewById(R.id.answers_container);

            if (null != ans_container && ans_container.getChildCount() > 0) {
                try {
                    ans_container.removeViews(0, ans_container.getChildCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setOrientation(LinearLayout.VERTICAL);
            this.cbs.clear();

            for (int i=0; i<answers.size(); i++) {
                final int index = i;
                LinearLayout lineLayout = new LinearLayout(this);
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                CheckBox cb = new CheckBox(this);
                cb.setId(i);
                cb.setText(answers.get(i).getAnswerText());
                cb.setChecked(answers.get(i).isRight());
                this.cbs.add(cb);

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        boolean isChecked = cbs.get(id).isChecked();
                        Answer tempAnswer = answers.get(id);
                        tempAnswer.setIsRight(isChecked);
                        answers.set(id, tempAnswer);

                    }
                });

                ImageButton delete_btn = new ImageButton (this);
                delete_btn.setImageResource(R.drawable.redx);
                delete_btn.setBackgroundColor(0xfff3f3f3);
                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAnswer(index);
                    }
                });

                lineLayout.addView(cb);
                lineLayout.addView(delete_btn);

                tempLayout.addView(lineLayout);

            }

            ans_container.addView(tempLayout);
        }
    }

    private void deleteAnswer(int index) {

        if (question_type==UNIQUE) {
            this.answers.remove(index);
            if (this.radio_checked_id == index)
                this.radio_checked_id = -1;
            if (this.radio_checked_id > index)
                this.radio_checked_id--;
            this.rbs.remove(index);
            refreshAnswerListU();
        }
        else if (question_type == MULTIPLE) { // preserve checked state of remaining multiple answers when deleting an item
            for (int i=0; i<answers.size(); i++) {
                if (i < index) {
                    // ignore
                }
                else {
                    if (i < answers.size()-1) { // we're not on last row
                        answers.set(i, answers.get(i+1));
                    }

                }
            }
            answers.remove(answers.size()-1);

            this.cbs = new ArrayList<CheckBox>(); // this just added, not tested
            refreshAnswerListM();
        }
    }

    public void onUniqueRightAnswer(View v) {
        RadioButton rb = (RadioButton) findViewById(R.id.unique_radiobtn);
        CheckBox cb = (CheckBox) findViewById(R.id.multiple_checkbox);

        cb.setChecked(false);
        this.question_type = UNIQUE;
        int rightAnswers = 0;
        int right_id = -1;
        for (int i=0; i<answers.size(); i++) {
            if (answers.get(i).isRight()) {
                rightAnswers++;
                right_id = i;
            }
        }
        if (rightAnswers == 1) {
            this.radio_checked_id = right_id;
        }
        else this.radio_checked_id = -1;

        this.rbs.clear();
        refreshAnswerListU();
    }

    public void onMultipleRightAnswers(View v) {
        RadioButton rb = (RadioButton) findViewById(R.id.unique_radiobtn);
        CheckBox cb = (CheckBox) findViewById(R.id.multiple_checkbox);
        if (cb.isChecked()) { // activate MULTIPLE
            rb.setChecked(false);
            this.question_type = MULTIPLE;

            // set right answer
            for (int i=0; i<this.answers.size(); i++) {
                if (this.radio_checked_id != -1) {
                    if (i == this.radio_checked_id) {
                        this.answers.get(i).setIsRight(true);

                    }
                    else this.answers.get(i).setIsRight(false);
                }
                else { // there was nothing selected on the radio buttons
                    for (Answer ans:answers) {
                        ans.setIsRight(false);
                    }
                }
            }

            this.cbs.clear();
            refreshAnswerListM();
        }
        else { // activate UNIQUE
            rb.setChecked(true);
            this.question_type = UNIQUE;
            int rightAnswers = 0;
            int right_id = -1;
            for (int i=0; i<answers.size(); i++) {
                if (answers.get(i).isRight()) {
                    rightAnswers++;
                    right_id = i;
                }
            }
            if (rightAnswers == 1) {
                this.radio_checked_id = right_id;
            }
            else {
                this.radio_checked_id = -1;
                for (Answer ans:answers) {
                    ans.setIsRight(false);
                }
            }
            this.rbs.clear();
            refreshAnswerListU();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("answers", this.answers);
        savedInstanceState.putInt("question_type", this.question_type);
        super.onSaveInstanceState(savedInstanceState);
    }
}