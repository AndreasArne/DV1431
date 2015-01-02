package se.bth.students.quizzard;

import android.app.Activity;
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
    public static final String N_QUESTIONS = "N_ANSWERS";
    public static final String MULTIPLE_CORRECT = "MULTIPLE_CORRECT";

    public int questionIndex;
    public int nQuestions;

    private ArrayList<Integer> selectedValues;

    private View view;

    private RadioGroup radioGroup;
    private ArrayList<Integer> correctAnswers;
    private boolean multipleCorrect;
    private ArrayList<CheckBox> checkBoxes;
    private OnSubmitListener listener;
    private String questionString;



    public static final DoQuizFragment newInstance(Question q, int questionIndexArg, int nQuestionsArg)
    {

        DoQuizFragment f = new DoQuizFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(QUESTION_TEXT, q.getQuestionText());
        bdl.putInt(Q_INDEX , questionIndexArg);
        bdl.putInt(N_QUESTIONS, nQuestionsArg);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questionString = getArguments().getString(QUESTION_TEXT);
        ArrayList<String> answers = getArguments().getStringArrayList(ANSWER_ARRAYLIST);
        this.questionIndex = getArguments().getInt(Q_INDEX);
        this.nQuestions = getArguments().getInt(N_QUESTIONS);

        // Set question text
        view = inflater.inflate(R.layout.doquiz_fragment, container, false);
        TextView questionTextView = (TextView)view.findViewById(R.id.questionText);
        questionTextView.setText(questionString);

        TextView questionIndexView = (TextView)view.findViewById(R.id.questionIndexText);
        questionIndexView.setText(this.questionIndex + " / " + this.nQuestions);

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
                cb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(cb.isChecked()){
                            cb.setChecked(true);
                            System.out.println("Checked");

                        }else{
                            cb.setChecked(false);
                            System.out.println("Un-Checked");
                        }
                    }
                });
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
        // Only show the submit button on the last page
        Button submitBtn  = (Button) view.findViewById(R.id.SubmitAnswers);
        if (this.questionIndex == this.nQuestions) {
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            submitBtn.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    // Check if the current answer is correct
    public boolean isRightAnswer() {


        if (multipleCorrect) {
            Log.d("DoQuizFragment", "CHECKING ANSWERS");
            Log.d("DoQuizFragment", correctAnswers.toString());
            Log.d("DoQuizFragment", checkBoxes.toString());

            // 1. Check that all the correct answers are ticked
            for (int i : correctAnswers) {
                if (!checkBoxes.get(i).isChecked()) {
                    Log.d("DoQuizFragment", "Answer #" + i + " not ticked!");
                    return false;
                }
                Log.d("DoQuizFragment", "Answer #" + i + " IS ticked!");
            }
            Log.d("DoQuizFragment", "Correct number of checked boxes.");

            // 2. Look for incorrect ticks
            int count = 0;
            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked()) {
                    count++;
                }
            }
            if (count != correctAnswers.size()) {
                Log.d("DoQuizFragment", "correctAnswers = " + correctAnswers.size() + ", count = " + count);
                return false;
            }
        } else { // Check radio button answer
            if (radioGroup.getCheckedRadioButtonId() != correctAnswers.get(0)) {
                return false;
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

        super.onResume();

    }

    // Save the instance state (radio buttons/check boxes)
//    @Override
    public void onPause() {
        selectedValues = new ArrayList<Integer>();
        //    Log.d("DoQuizzFragment", "getCheckedRadioButtonId() = " + radioGroup.getCheckedRadioButtonId());
        super.onPause();
    }

}
