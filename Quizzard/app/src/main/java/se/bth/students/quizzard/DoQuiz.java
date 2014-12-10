package se.bth.students.quizzard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class DoQuiz extends FragmentActivity {
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
        currentQuiz = this.getDummyQuiz();
        questions = currentQuiz.getQuestions();
        List<Fragment> fList = new ArrayList<Fragment>();

        int i = 1;
        for (Question q : questions) {
            fList.add(DoQuizFragment.newInstance(q, i, questions.size()));
            i++;
        }
        return fList;
    }

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

    // Dummy data, to be replaced with the Quiz parameter.
    private Quiz getDummyQuiz() {

        Quiz q = new Quiz("MA1111", "Linear algebra", "Saddam Hussein");
        Question q1 = new Question("1 + 1 = ?");
        q1.addAnswer("0", false);
        q1.addAnswer("1", false);
        q1.addAnswer("2", true);
        q1.addAnswer("3", false);
        Question q2 = new Question("2 + 2 = ?");
        q2.addAnswer("1", false);
        q2.addAnswer("2", false);
        q2.addAnswer("3", false);
        q2.addAnswer("4", true);
        Question q3 = new Question("3 + 3 = ?");
        q3.addAnswer("6", true);
        q3.addAnswer("8", false);
        q3.addAnswer("9", false);
        q3.addAnswer("12", false);
        Question q4 = new Question("3 * 3 = ?");
        q4.addAnswer("6", true);
        q4.addAnswer("8", false);
        q4.addAnswer("9", false);
        q4.addAnswer("12", false);
        Question q5 = new Question("1 * -1 = ?");
        q5.addAnswer("-1", true);
        q5.addAnswer("16", false);
        q5.addAnswer("1", false);
        q5.addAnswer("12", false);
        Question q6 = new Question("3 - 3 = ?");
        q6.addAnswer("0", true);
        q6.addAnswer("8", false);
        q6.addAnswer("9", false);
        q6.addAnswer("12", false);
        Question q7 = new Question("1 + 1 = ?");
        q7.addAnswer("0", false);
        q7.addAnswer("1", false);
        q7.addAnswer("2", true);
        q7.addAnswer("3", false);
        Question q8 = new Question("2 + 2 = ?");
        q8.addAnswer("1", false);
        q8.addAnswer("2", false);
        q8.addAnswer("3", false);
        q8.addAnswer("4", true);
        Question q9 = new Question("3 + 3 = ?");
        q9.addAnswer("6", true);
        q9.addAnswer("8", false);
        q9.addAnswer("9", false);
        q9.addAnswer("12", false);
        Question q10 = new Question("3 * 3 = ?");
        q10.addAnswer("6", true);
        q10.addAnswer("8", false);
        q10.addAnswer("9", false);
        q10.addAnswer("12", false);
        Question q11 = new Question("1 * -1 = ?");
        q11.addAnswer("-1", true);
        q11.addAnswer("16", false);
        q11.addAnswer("1", false);
        q11.addAnswer("12", false);
        Question q12 = new Question("3 - 3 = ?");
        q12.addAnswer("0", true);
        q12.addAnswer("8", false);
        q12.addAnswer("9", false);
        q12.addAnswer("12", false);

        q.addQuestion(q1);
        q.addQuestion(q2);
        q.addQuestion(q3);
        q.addQuestion(q4);
        q.addQuestion(q5);
        q.addQuestion(q6);
        q.addQuestion(q7);
        q.addQuestion(q8);
        q.addQuestion(q9);
        q.addQuestion(q10);
        q.addQuestion(q11);
        q.addQuestion(q12);

        System.out.println(q.getQuestions().toString());
        return q;
    }
}


