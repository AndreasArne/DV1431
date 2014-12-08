package se.bth.students.quizzard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mihai on 2014-12-02.
 */
public class ListQuizzes extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_quizzes);

        ArrayList<Quiz> quizzes =(ArrayList<Quiz>) getIntent().getSerializableExtra("quizzes");
        Toast.makeText(getApplicationContext(), quizzes.get(0).getAuthor() + quizzes.get(1).getCourse(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),quizzes.get(1).getName(),Toast.LENGTH_SHORT).show();
    }
}
