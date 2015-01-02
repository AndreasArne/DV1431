package se.bth.students.quizzard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by asdf on 2014-12-10.
 * A score screen to be displayed when the player finishes a quiz.
 */
public class ScoreScreen extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorescreen);

        ArrayList<Result> results =
                (ArrayList<Result>)getIntent().getSerializableExtra("fragments");

        int nCorrect = getIntent().getIntExtra("nCorrect", -1);
        String quizTitle = getIntent().getStringExtra("quizTitle");

        TextView header = (TextView)findViewById(R.id.resultsHeader);

        header.setText("\"" + quizTitle + "\"" + " final score.");

        TextView score = (TextView)findViewById(R.id.score);


        score.setText("You scored " + nCorrect + " out of " + results.size() +
                " - " + (int)((double)nCorrect/results.size() *100) + "%!");

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
