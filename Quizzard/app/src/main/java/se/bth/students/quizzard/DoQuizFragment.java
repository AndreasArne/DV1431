package se.bth.students.quizzard;

// import android.app.Activity;
// import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asdf on 2014-12-09.
 */

public class DoQuizFragment extends Fragment implements Serializable {
    public static final String QUESTION_TEXT = "QUESTION_TEXT";
    public static final String ANSWER_ARRAYLIST = "ANSWER_ARRAYLIST";
    public static final String CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String Q_INDEX = "ANSWER_INDEX";
    public static final String N_ANSWERS = "N_ANSWERS";
    public static final String MULTIPLE_CORRECT = "MULTIPLE_CORRECT";

    private ArrayList<Integer> selectedValues;

    private View view;

    private RadioGroup radioGroup;
    private ArrayList<Integer> correctAnswers;
    private boolean multipleCorrect;
    private ArrayList<CheckBox> checkBoxes;
    private OnSubmitListener listener;
    private String questionString;



    public static final DoQuizFragment newInstance(Question q, int questionIndex, int nQuestions)
    {

        DoQuizFragment f = new DoQuizFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(QUESTION_TEXT, q.getQuestionText());
        bdl.putInt(Q_INDEX , questionIndex);
        bdl.putInt(N_ANSWERS, nQuestions);
        bdl.putIntegerArrayList(CORRECT_ANSWERS, f.getCorrectAnswers(q));
        if (q.hasOneRightAnswer()) {
            bdl.putBoolean(MULTIPLE_CORRECT, false);
        } else {
            bdl.putBoolean(MULTIPLE_CORRECT, true);
        }

        ArrayList<String> answerStrings = new ArrayList<String>();
        ArrayList<Answer> answers = q.getAnswers();

        for (Answer a : answers) {
            answerStrings.add(a.getAnswerText());
        }
        bdl.putStringArrayList(ANSWER_ARRAYLIST, answerStrings);
        f.setArguments(bdl);
        return f;
    }
    //    doQuizRadioGroup
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questionString = getArguments().getString(QUESTION_TEXT);
        ArrayList<String> answers = getArguments().getStringArrayList(ANSWER_ARRAYLIST);

        // Set question text
        view = inflater.inflate(R.layout.doquiz_fragment, container, false);
        TextView questionTextView = (TextView)view.findViewById(R.id.questionText);
        questionTextView.setText(questionString);

        multipleCorrect = getArguments().getBoolean(MULTIPLE_CORRECT);

        // Populate the answer list
        correctAnswers = getArguments().getIntegerArrayList(CORRECT_ANSWERS);
        checkBoxes = new ArrayList<CheckBox>();

        if (multipleCorrect) {    // Checkbox question
            LinearLayout answersContainer = (LinearLayout)view.findViewById(R.id.answerscontainer);
            int i = 0;
            for (String a : answers) {
                final CheckBox cb = new CheckBox(getActivity());
                cb.setText(a);
                cb.setChecked(false);
                cb.setId(i);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      cb.setChecked(isChecked);
                                                      Log.d("DoQuizzFragment", "checkbox ticked = " + cb.isChecked());
                                                      Log.d("DoQuizzFragment", "isRightAnswer() = " + isRightAnswer());
                                                  }
                                              }
                );
                checkBoxes.add(cb);
                answersContainer.addView(cb);
                i++;
            }
        } else {                            // Radio button question
            radioGroup = (RadioGroup) view.findViewById(R.id.doQuizRadioGroup);

            int i = 0;
            for (String a : answers) {
                RadioButton rbtn = new RadioButton(getActivity());
                rbtn.setText(a);
                rbtn.setId(i);
                radioGroup.addView(rbtn);
                i++;
            }

        }
        // Set page number text
/*        int index = getArguments().getInt(Q_INDEX );
        int nQuestions = getArguments().getInt(N_ANSWERS);
        TextView questionIndexTextView = (TextView)view.findViewById(R.id.questionIndexText);
        questionIndexTextView.setText( index + " / " + nQuestions);*/

/*        Button btn_submitAnswers = (Button) view.findViewById(R.id.SubmitAnswers);
        btn_submitAnswers.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                Toast.makeText(getActivity(), "Score screen", Toast.LENGTH_SHORT).show();
                Intent submitAnswersInt= new Intent(getActivity(),ScoreScreen.class);
                submitAnswersInt.putExtra("quizzes",1);
                startActivity(submitAnswersInt);
            }
        });*/


        return view;
    }

    // Check if the current answer is correct
    public boolean isRightAnswer() {
        if (multipleCorrect) {
            // Check that all the correct answers are ticked
            for (int i : correctAnswers) {
                if (!checkBoxes.get(i).isChecked()) {
                    return false;
                }
            }
            // Count the number of checked boxes
            int count = 0;
            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked()) {
                    count++;
                }
                if (count > correctAnswers.size()) {
                    return false;
                }
            }
        } else {
            for (int i : correctAnswers) {
                if (radioGroup.getCheckedRadioButtonId() != correctAnswers.get(0)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getQuestionText() {
        return this.questionString;
    }

    // Define the events that the fragment will use to communicate
    public interface OnSubmitListener {
        public void onClickSubmitButton(View v);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnSubmitListener) {
            listener = (OnSubmitListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DoQuizFragment.OnSubmitListener");
        }
    }

    // Fire the submit event in the parent when the user clicks submit in the fragment
    public void onClickSubmit() {
        Button btn_submitAnswers = (Button) view.findViewById(R.id.SubmitAnswers);
        listener.onClickSubmitButton(btn_submitAnswers);
    }

    private void checkBoxListener(CheckBox cb) {
        Log.d("CHeckBox OnClickListener", "cb " + cb.getText());
    }

    // Returns a arraylist of the indices of the correct answers, e.g. [2, 4] if the correct answers are 2 and 4.
    private ArrayList<Integer> getCorrectAnswers(Question q) {
        ArrayList<Integer> correctAnswerList = new ArrayList<Integer>();
        ArrayList<Answer> answers = q.getAnswers();
        int i = 0;
        for (Answer a : answers) {
            if (a.isRight()) {
                correctAnswerList.add(i);
            }
            i++;
        }
        return correctAnswerList;
    }

    //    @Override
    public void onResume() {
        Log.d("DoQuizzFragment", "DoQuizzFragment.onResume() called");
        super.onResume();

    }

    // Save the instance state (radio buttons/check boxes)
//    @Override
    public void onPause() {
        selectedValues = new ArrayList<Integer>();


        //    Log.d("DoQuizzFragment", "getCheckedRadioButtonId() = " + radioGroup.getCheckedRadioButtonId());
        super.onPause();
    }
/*
    public void onDestroyView() {
    }

    public void onDestroy() {

    }
*/
}
