package ru.spbau.mit.auTimetable;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class ScoresAdapter extends ArrayAdapter<ScoresLink> {
    private final Context context;
    private final ScoresLink[] values;

    private boolean wasSetUp = false;

    private LayoutInflater inflater;

    public ScoresAdapter(Context context, ScoresLink[] values) {
        super(context, R.layout.scores_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        setUp();

        // TODO needs optimization here. See http://habrahabr.ru/post/133575/ and
        // https://stackoverflow.com/questions/25381435/unconditional-layout-inflation-from-view-adapter-should-use-view-holder-patter
        View rowView = inflater.inflate(R.layout.scores_item, parent, false);
        TextView scoresSubject = (TextView) rowView.findViewById(R.id.scores_subject);
        TextView scoresLink = (TextView) rowView.findViewById(R.id.scores_link);

        scoresSubject.setText(values[position].subject);
        scoresLink.setText(values[position].link);

        return rowView;
    }

    private void setUp() {
        if (wasSetUp)
            return;
        wasSetUp = true;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
