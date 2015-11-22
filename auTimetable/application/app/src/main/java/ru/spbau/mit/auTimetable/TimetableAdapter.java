package ru.spbau.mit.auTimetable;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by equi on 22.11.15.
 *
 * @author Kravchenko Dima
 */
public class TimetableAdapter extends ArrayAdapter<ClassInfo> {
    private final Context context;
    private final ClassInfo[] values;

    private boolean wasSetUp = false;

    private LayoutInflater inflater;

    public TimetableAdapter(Context context, ClassInfo[] values) {
        super(context, R.layout.day_timetable_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        setUp();

        // TODO needs optimization here. See http://habrahabr.ru/post/133575/ and
        // https://stackoverflow.com/questions/25381435/unconditional-layout-inflation-from-view-adapter-should-use-view-holder-patter
        View rowView = inflater.inflate(R.layout.day_timetable_item, parent, false);
        View shortInfo = inflater.inflate(R.layout.class_short_info, parent, false);

        TextView time = (TextView) rowView.findViewById(R.id.class_time_interval);
        FrameLayout container = (FrameLayout) rowView.findViewById(R.id.class_container);
        container.removeAllViews();

        TextView className = (TextView) shortInfo.findViewById(R.id.class_name);
        TextView classroom = (TextView) shortInfo.findViewById(R.id.classroom);
        TextView teacherName = (TextView) shortInfo.findViewById(R.id.teacher_name);

        time.setText(values[position].time);

        className.setText(values[position].className);
        classroom.setText(values[position].classroom);
        teacherName.setText(values[position].teacherName);

        container.addView(shortInfo);

        return rowView;
    }

    private void setUp() {
        if (wasSetUp)
            return;
        wasSetUp = true;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
