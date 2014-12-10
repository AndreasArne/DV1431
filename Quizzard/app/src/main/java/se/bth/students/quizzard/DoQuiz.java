package se.bth.students.quizzard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DoQuiz extends FragmentActivity implements DoQuizFragment.OnSubmitListener {
    private Quiz currentQuiz;
    private ArrayList<Question> questions;
    private DoQuizPageAdapter pageAdapter;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_quizz);

        fragments = createFragments();
        pageAdapter = new DoQuizPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

    }

    private List<Fragment> createFragments() {
        currentQuiz = (Quiz)getIntent().getSerializableExtra("Quiz");

        questions = currentQuiz.getQuestions();
        List<Fragment> fList = new ArrayList<Fragment>();

        int i = 1;
        for (Question q : questions) {
            fList.add(DoQuizFragment.newInstance(q, i, questions.size()));
            i++;
        }
        return fList;
    }

    // Create the score screen when the submit event fires in the fragment
    @Override
    public void onClickSubmitButton(View v) {

        int i = 0;
        ArrayList<Result> res = new ArrayList<Result>();
        int nCorrect = 0;
        for (Fragment f : fragments) {
            DoQuizFragment q = (DoQuizFragment)f;
            boolean rightAnswer = q.isRightAnswer();
            res.add(new Result(q.getQuestionText(), rightAnswer));
            if (rightAnswer) {
                nCorrect++;
            }
            i++;
        }
        // Start the ScoreScreen activity
        Intent scoreScreenIntent = new Intent(getApplicationContext(), ScoreScreen.class);
        scoreScreenIntent.putExtra("fragments", res);
        scoreScreenIntent.putExtra("nCorrect", nCorrect);
        scoreScreenIntent.putExtra("quizTitle", currentQuiz.getName());
        startActivityForResult(scoreScreenIntent, 0);
    }


    // Private FragmentPagerAdapter class
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


}


