package se.bth.students.quizzard;

// import android.app.Activity;
// import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asdf on 2014-12-09.
 */

public class DoQuizFragment extends Fragment {
    public static final String QUESTION_TEXT = "QUESTION_TEXT";
    public static final String ANSWER_ARRAYLIST = "ANSWER_ARRAYLIST";
    public static final String Q_INDEX = "ANSWER_INDEX";
    public static final String N_ANSWERS = "N_ANSWERS";


    private ArrayList<Integer> selectedValues;

    private View view;

    private RadioGroup radioGroup;
    public static final DoQuizFragment newInstance(Question q, int questionIndex, int nQuestions)
    {

        DoQuizFragment f = new DoQuizFragment();

        Bundle bdl = new Bundle(1);
        bdl.putString(QUESTION_TEXT, q.getQuestionText());
        bdl.putInt(Q_INDEX , questionIndex);
        bdl.putInt(N_ANSWERS, nQuestions);

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
        String questionString = getArguments().getString(QUESTION_TEXT);
        ArrayList<String> answers = getArguments().getStringArrayList(ANSWER_ARRAYLIST);

        // Set question text
        view = inflater.inflate(R.layout.doquiz_fragment, container, false);
        TextView questionTextView = (TextView)view.findViewById(R.id.questionText);
        questionTextView.setText(questionString);

        // Populate the answer list
        radioGroup = (RadioGroup)view.findViewById(R.id.doQuizRadioGroup);

        int i = 0;
        for (String a : answers) {
            RadioButton rbtn = new RadioButton(getActivity());
            rbtn.setText(a);
            rbtn.setId(i);
            radioGroup.addView(rbtn);
            i++;
        }

        // Set page number text
        int index = getArguments().getInt(Q_INDEX );
        int nQuestions = getArguments().getInt(N_ANSWERS);
        TextView questionIndexTextView = (TextView)view.findViewById(R.id.questionIndexText);
        questionIndexTextView.setText( index + " / " + nQuestions);


        return view;
    }




    //    @Override
    public void onResume() {
        super.onResume();

    }

    // Save the instance state (radio buttons/check boxes)
//    @Override
    public void onPause() {
        selectedValues = new ArrayList<Integer>();


        Log.d("DoQuizzFragment", "getCheckedRadioButtonId() = " + radioGroup.getCheckedRadioButtonId());
        super.onPause();
    }
/*
    public void onDestroyView() {
    }

    public void onDestroy() {

    }
*/
}
