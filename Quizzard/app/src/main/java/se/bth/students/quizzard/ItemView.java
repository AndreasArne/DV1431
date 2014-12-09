package se.bth.students.quizzard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gadden on 2014-12-08.
 */
public class ItemView extends BaseAdapter{
    private Activity activity;
    private ArrayList<Quiz> quizzes;
    private static LayoutInflater inflater=null;
    public ItemView(Activity a, ArrayList<Quiz> quiz) {
        activity = a;
        quizzes = quiz;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return quizzes.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);

        TextView courseTextFiled=(TextView)vi.findViewById(R.id.textViewItemCourse);
        TextView nameTextFiled=(TextView)vi.findViewById(R.id.textViewItemName);
        TextView authorTextFiled=(TextView)vi.findViewById(R.id.textViewItemAuthor);
        courseTextFiled.setText(quizzes.get(0).getCourse());
        nameTextFiled.setText(quizzes.get(0).getName());
        authorTextFiled.setText(quizzes.get(0).getAuthor());
        return vi;
    }
}
