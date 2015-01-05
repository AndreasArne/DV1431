package se.bth.students.quizzard;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DoQuiz extends FragmentActivity implements DoQuizFragment.OnSubmitListener, DoQuizFragment.RadioGroupListener, DoQuizFragment.CheckBoxListener  {
    private Quiz currentQuiz;
    private ArrayList<Question> questions;
    private DoQuizPageAdapter pageAdapter;
    private List<Fragment> fragments;
    private boolean[] questionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DoQuiz.onCreate", "hello");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.questionResult = savedInstanceState.getBooleanArray("answers");
        }

        setContentView(R.layout.activity_do_quizz);
        Toast.makeText(getApplicationContext(), "Swipe to view the next question", Toast.LENGTH_SHORT).show();
        fragments = createFragments();
        pageAdapter = new DoQuizPageAdapter(getSupportFragmentManager(), fragments);




        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBooleanArray("answers", this.questionResult);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Create fragments, each fragments represents a question within the Quiz.
     * @return
     */
    private List<Fragment> createFragments() {
        currentQuiz = (Quiz)getIntent().getSerializableExtra("Quiz");
        getActionBar().setTitle(currentQuiz.getName());
        questions = currentQuiz.getQuestions();
        List<Fragment> fList = new ArrayList<Fragment>();

        int i = 1;
        for (Question q : questions) {
            DoQuizFragment frag = DoQuizFragment.newInstance(q, i, questions.size());


            Log.d("DoQuiz.createFragments", "Fragment #" + i + " id = " + frag.getId());
            fList.add(frag);
            i++;
        }

        return fList;
    }


    /**
     * Create the score screen when the submit event fires in the fragment
     * @param v
     */
    @Override
    public void onClickSubmitButton(View v) {

        Log.d("DoQuiz.createFragments", "onClickSubmitButton ");
        Log.d("DoQuiz.createFragments", "View v: " + v.toString());

        int nCorrect = 0;

        // int i = 0;
        ArrayList<Result> res = new ArrayList<Result>();
/*        for (Fragment f : fragments) {

            DoQuizFragment q = (DoQuizFragment)f;

            Log.d("DoQuiz.createFragments", "q.toString() " + q.toString());
            Log.d("DoQuiz.createFragments", "q.getQuestionText() " + q.getQuestionText());
            boolean rightAnswer = q.isRightAnswer();
            res.add(new Result(q.getQuestionText(), rightAnswer));
            if (rightAnswer) {
                nCorrect++;
            }
            i++;
        } */

        ArrayList<Question> q = currentQuiz.getQuestions();
        for (int i = 0; i < q.size(); i++) {
            boolean isCorrect = this.questionResult[i];
            res.add(new Result(q.get(i).getQuestionText(), isCorrect));
            if (isCorrect) {
                nCorrect++;
            }
        }

        // Start the ScoreScreen activity
        Intent scoreScreenIntent = new Intent(getApplicationContext(), ScoreScreen.class);
        scoreScreenIntent.putExtra("fragments", res);
        scoreScreenIntent.putExtra("nCorrect", nCorrect);
        scoreScreenIntent.putExtra("quizTitle", currentQuiz.getName());
        scoreScreenIntent.putExtra("quizObject", currentQuiz);
        startActivityForResult(scoreScreenIntent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            //99 == from scorescreen
            if(requestCode == 99){
                setResult(Activity.RESULT_OK,data);
                finish();
            }
        }
    }



        private String getResultsString() {
        String res = "";
        for (boolean b : questionResult) {
            res += b + " ";
        }
        return res;
    }

    public void onCheckRadioButton(int qIndex, boolean isAnswerCorrect) {
        if (questionResult == null) {
            questionResult = new boolean[currentQuiz.getQuestions().size()];
        }
        Log.d("DoQuiz.createFragments", "RADIO BUTTON CLICKED, index: "+ qIndex + ", correctAnswer: " + isAnswerCorrect);
        this.questionResult[qIndex-1] = isAnswerCorrect;
        Log.d("DoQuiz.createFragments", "ALL ANSWERS: "+ getResultsString());
    }

    public void onCheckCheckBoxUpdate(int qIndex, boolean isAnswerCorrect) {
        if (questionResult == null) {
            questionResult = new boolean[currentQuiz.getQuestions().size()];
        }
        Log.d("DoQuiz.createFragments", "CheckBox CLICKED, index: "+ qIndex + ", correctAnswer: " + isAnswerCorrect);
        this.questionResult[qIndex-1] = isAnswerCorrect;
        Log.d("DoQuiz.createFragments", "ALL ANSWERS: "+ getResultsString());
    }

    /**
     * Private FragmentPagerAdapter class used by DoQuiz
     */
    private class DoQuizPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public DoQuizPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    private class QuestionAnswers {
        private boolean isAnswerCorrect;
        public QuestionAnswers(boolean isAnswerCorrectArg) {
            this.isAnswerCorrect = isAnswerCorrectArg;
        }
        public boolean getAnswer() {
            return this.isAnswerCorrect;
        }
    }


}


