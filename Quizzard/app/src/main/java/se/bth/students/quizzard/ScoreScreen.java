package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by asdf on 2014-12-10.
 * A score screen to be displayed when the player finishes a quiz.
 */
public class ScoreScreen extends Activity {


    private RatingBar ratingbar;
    private Quiz quiz;
    private Button rateButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorescreen);

        // Get Quiz object for rating

        ArrayList<Result> results =
                (ArrayList<Result>)getIntent().getSerializableExtra("fragments");

        int nCorrect = getIntent().getIntExtra("nCorrect", -1);
        String quizTitle = getIntent().getStringExtra("quizTitle");

        TextView header = (TextView)findViewById(R.id.resultsHeader);

        header.setText("\"" + quizTitle + "\"" + " final score.");

        TextView score = (TextView)findViewById(R.id.score);


        //rating
        quiz = (Quiz)getIntent().getSerializableExtra("quizObject");
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setRating(quiz.getRating());
        //rate button
        rateButton = (Button) findViewById(R.id.rateSubmit);
        //if user wants to submit rating
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calculates the new rating in Quiz.java and returns the new rating and sets the new rating
                ratingbar.setRating( quiz.rate( ratingbar.getRating()));
                Toast.makeText(getApplicationContext(), "New rating is " + String.valueOf(quiz.getRating()), Toast.LENGTH_SHORT).show();;
                Intent resultint = new Intent();
                resultint.putExtra("Quiz",quiz);
                setResult(Activity.RESULT_OK,resultint);
                finish();
            }
        });


        score.setText("You scored " + nCorrect + " out of " + results.size() +
                " - " + (int) ((double) nCorrect / results.size() * 100) + "%!");

        LinearLayout scrollViewContent = (LinearLayout)findViewById(R.id.scrollViewContent);
        int i = 0;
        for (Result r : results) {
            i++;
            TextView text = new TextView(this);

            if (r.getResult()) {
                text.setTextColor(Color.GREEN);
            } else {
                text.setTextColor(Color.RED);
            }
            text.setText(i + ": " + r.getText());
            scrollViewContent.addView(text);
        }
    }

}
