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
    public static final String CHECKED_ANSWERS = "CHECKED_ANSWERS";


    // State data
    private String questionString;
    private ArrayList<String> answers;
    private ArrayList<Integer> correctAnswers;
    public int questionIndex;
    public int nQuestions;
    private boolean multipleCorrect;
    private boolean singleAnswer;

    private View view;
    private RadioGroup radioGroup;
    private ArrayList<CheckBox> checkBoxes;
    private OnSubmitListener onSubmitListener;
    private RadioGroupListener radioGroupListener;
    private CheckBoxListener checkBoxListener;
    private Bundle savedState;


    public static final DoQuizFragment newInstance(Question q, int questionIndexArg, int nQuestionsArg) {

        DoQuizFragment f = new DoQuizFragment();
        ArrayList<Integer> correctAnswersArg = f.getCorrectAnswers(q);
        ArrayList<String> answersArg = new ArrayList<String>();
        for (Answer a : q.getAnswers()) {
            answersArg.add(a.getAnswerText());
        }

        Bundle bdl = f.setInstance(q.getQuestionText(), questionIndexArg, nQuestionsArg, correctAnswersArg, q.hasOneRightAnswer(), answersArg, null);
        f.setArguments(bdl);
        return f;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("DoQuizzFragment", "onSaveInstanceState");
        // Save the user's current game state
        ArrayList<Integer> checkedAnswers = this.getCheckedAnswers();
        savedInstanceState = setInstance(this.questionString, this.questionIndex, this.nQuestions, this.correctAnswers, this.multipleCorrect, this.answers, checkedAnswers);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Method for instance persistence, used by both the constructer and onSaveInstanceState
     *
     * @param questionTextArg
     * @param questionIndexArg
     * @param nQuestionsArg
     * @param correctAnswersArg
     * @param singleAnswerArg
     * @param answersArg
     * @return
     */
    private Bundle setInstance(String questionTextArg, int questionIndexArg, int nQuestionsArg, ArrayList<Integer> correctAnswersArg, boolean singleAnswerArg, ArrayList<String> answersArg, ArrayList<Integer> checkedAnswersArg) {
        Bundle bdl = new Bundle(1);
        bdl.putString(QUESTION_TEXT, questionTextArg);
        bdl.putInt(Q_INDEX, questionIndexArg);
        bdl.putInt(N_QUESTIONS, nQuestionsArg);
        bdl.putIntegerArrayList(CORRECT_ANSWERS, correctAnswersArg);

        if (singleAnswerArg) {
            bdl.putBoolean(MULTIPLE_CORRECT, false);
        } else {
            bdl.putBoolean(MULTIPLE_CORRECT, true);
        }

        ArrayList<String> answerStrings = new ArrayList<String>();

        for (String a : answersArg) {
            answerStrings.add(a);
        }
        bdl.putStringArrayList(ANSWER_ARRAYLIST, answerStrings);
        if (checkedAnswersArg != null) {
            bdl.putIntegerArrayList(CHECKED_ANSWERS, checkedAnswersArg);
        }

        return bdl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DoQuizzFragment", "onCreateView");
        super.onCreate(savedInstanceState);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DoQuizzFragment", "onCreateView");
        questionString = getArguments().getString(QUESTION_TEXT);
        answers = getArguments().getStringArrayList(ANSWER_ARRAYLIST);
        this.questionIndex = getArguments().getInt(Q_INDEX);
        this.nQuestions = getArguments().getInt(N_QUESTIONS);
        multipleCorrect = getArguments().getBoolean(MULTIPLE_CORRECT);
        correctAnswers = getArguments().getIntegerArrayList(CORRECT_ANSWERS);

        // Set question text
        view = inflater.inflate(R.layout.doquiz_fragment, container, false);
        TextView questionTextView = (TextView) view.findViewById(R.id.questionText);
        questionTextView.setText(questionString);

        TextView questionIndexView = (TextView) view.findViewById(R.id.questionIndexText);
        questionIndexView.setText(this.questionIndex + " / " + this.nQuestions);



        // Populate the answer list

        checkBoxes = new ArrayList<CheckBox>();

        if (multipleCorrect) {    // Checkbox question
            LinearLayout answersContainer = (LinearLayout) view.findViewById(R.id.answerscontainer);
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
                        if (cb.isChecked()) {
                            cb.setChecked(true);
                            System.out.println("Checked");

                        } else {
                            cb.setChecked(false);
                            System.out.println("Un-Checked");
                        }
                        // Call the activity
                        onCheckBoxChange();
                    }
                });
                checkBoxes.add(cb);
                answersContainer.addView(cb);
                i++;
            }
            // Add listener


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
            // Set a listener to pass checks to the parent activity
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    onRadioGroupChange();
                }
            });

        }
        // Only show the submit answers button on the last page
        Button btnSubmit = (Button) view.findViewById(R.id.SubmitAnswers);
        if (this.questionIndex == this.nQuestions) {
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    /**
     * Check if the current answer is correct
     *
     * @return
     */
    public boolean isRightAnswer() {
        // Checkboxes
        if (multipleCorrect) {
            // 1. Check that all the correct answers are ticked
            for (int i : correctAnswers) {
                if (!checkBoxes.get(i).isChecked()) {
//                    Log.d("DoQuizFragment", "Answer #" + i + " not ticked!");
                    return false;
                }
//                Log.d("DoQuizFragment", "Answer #" + i + " IS ticked!");
            }
//            Log.d("DoQuizFragment", "Correct number of checked boxes.");

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
        } else { // Radio buttons

            if (radioGroup.getCheckedRadioButtonId() != correctAnswers.get(0)) {
                return false;
            }
        }
        return true;
    }

    public String getQuestionText() {
        return this.questionString;
    }



    /**
     * Store the listeners that will have events fired once the fragment is attached
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnSubmitListener) {
            this.onSubmitListener = (OnSubmitListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DoQuizFragment.OnSubmitListener");
        }

        if (activity instanceof RadioGroupListener) {
            this.radioGroupListener = (RadioGroupListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DoQuizFragment.RadioGroupListener");
        }

        if (activity instanceof CheckBoxListener) {
            this.checkBoxListener = (CheckBoxListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DoQuizFragment.CheckBoxListener");
        }
    }

    /**
     * Defines the events that the fragment will use to communicate
     */
    public interface OnSubmitListener {
        public void onClickSubmitButton(View v);
    }


    public interface RadioGroupListener {
        public void onCheckRadioButton(int questionIndex, boolean isCorrectAnswer);
    }

    public interface CheckBoxListener {
        public void onCheckCheckBoxUpdate(int questionIndex, boolean isCorrectAnswer);
    }

    /**
     * Fire the submit event in the parent when the user clicks submit in the fragment
     */
    public void onClickSubmit() {
        Button btn_submitAnswers = (Button) view.findViewById(R.id.SubmitAnswers);
        onSubmitListener.onClickSubmitButton(btn_submitAnswers);
    }

    public void onRadioGroupChange() {
        Log.d("DoQuizzFragment", "onRadioGroupChange");
        this.radioGroupListener.onCheckRadioButton(this.questionIndex, this.isRightAnswer());
    }

    public void onCheckBoxChange() {
        Log.d("DoQuizzFragment", "onCheckBoxChange");
        this.checkBoxListener.onCheckCheckBoxUpdate(this.questionIndex, this.isRightAnswer());
    }
    /**
     * Returns an arraylist of the indices of the correct answers, e.g. [2, 4] if the correct answers are 2 and 4.
     *
     * @param q
     * @return
     */
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
        Log.d("DoQuizzFragment", "onResume");
        super.onResume();
        // getFragmentManager().findFragmentByTag(Integer.toString(this.questionIndex)).getRetainInstance();
    }

    /**
     * Save the instance state (radio buttons/check boxes)
     */
    public void onPause() {
        Log.d("DoQuizzFragment", "onPause");
        if (this.multipleCorrect) {
            Log.d("DoQuizzFragment", this.checkBoxes.toString());
        } else {
            Log.d("DoQuizzFragment", this.radioGroup.toString());
        }
        savedState = new Bundle(1);
        this.onSaveInstanceState(savedState);

        //    Log.d("DoQuizzFragment", "getCheckedRadioButtonId() = " + radioGroup.getCheckedRadioButtonId());
        super.onPause();
        // getFragmentManager().findFragmentById(this.questionIndex).setRetainInstance(true);
    }


    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putString(QUESTION_TEXT, this.questionString);
        return state;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("DoQuizzFragment", "onActivityCreated CALLED");
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            questionString = getArguments().getString(QUESTION_TEXT);
            answers = getArguments().getStringArrayList(ANSWER_ARRAYLIST);
            this.questionIndex = getArguments().getInt(Q_INDEX);
            this.nQuestions = getArguments().getInt(N_QUESTIONS);
            multipleCorrect = getArguments().getBoolean(MULTIPLE_CORRECT);
            correctAnswers = getArguments().getIntegerArrayList(CORRECT_ANSWERS);

            if (multipleCorrect) {    // Checkbox question
                /*
                LinearLayout answersContainer = (LinearLayout) view.findViewById(R.id.answerscontainer);
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
                            if (cb.isChecked()) {
                                cb.setChecked(true);
                                System.out.println("Checked");

                            } else {
                                cb.setChecked(false);
                                System.out.println("Un-Checked");
                            }
                        }
                    });
                    checkBoxes.add(cb);
                    answersContainer.addView(cb);
                    i++;
                }
                for (int a : correctAnswers) {
                    checkBoxes.get(a).setChecked(true);
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
                radioGroup.check(correctAnswers.get(0));

                */
            }

        }
    }

    ArrayList<Integer> getCheckedAnswers() {
        ArrayList<Integer> checkedAnswers = new ArrayList<Integer>();
        if (this.multipleCorrect) {
            int i = 0;
            for (CheckBox cb : this.checkBoxes) {
                if (cb.isChecked()) {
                    checkedAnswers.add(i);
                }
                i++;
            }
        } else {
            checkedAnswers.add(this.radioGroup.getCheckedRadioButtonId());
        }
        return checkedAnswers;
    }


}
