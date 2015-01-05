package se.bth.students.quizzard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gadden on 2014-12-08.
 */
public class ItemView extends ArrayAdapter<Quiz>{
    private final Activity context;
    private final ArrayList<Quiz> quizzes;

    static class ViewHolder {
        public TextView quizName;
        public TextView courseName;
        public TextView authorName;
        public RatingBar rating;
    }

    public ItemView(Activity context, ArrayList<Quiz> quizzes) {
        super(context, R.layout.list_item);
        this.context = context;
        this.quizzes = quizzes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.quizName = (TextView) rowView.findViewById(R.id.textViewQuizName);
            viewHolder.courseName = (TextView) rowView.findViewById(R.id.textViewCourse);
            viewHolder.authorName = (TextView) rowView.findViewById(R.id.textViewAuthor);
            viewHolder.rating = (RatingBar) rowView.findViewById(R.id.ratingBar);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String qName = quizzes.get(position).getName();
        String qCourse = quizzes.get(position).getName();
        String qAuthor = quizzes.get(position).getName();
        int rating = (int)quizzes.get(position).getRating();
        holder.quizName.setText(qName.toString());
        holder.courseName.setText(qCourse);
        holder.authorName.setText(qAuthor);
        holder.rating.setNumStars(rating);

        return rowView;
    }
}

