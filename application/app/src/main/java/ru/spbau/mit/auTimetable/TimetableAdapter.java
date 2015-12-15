package ru.spbau.mit.auTimetable;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;

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

    private Calendar calendar;
    private Date dateToShow;

    public TimetableAdapter(Context context, ClassInfo[] values, Date dateToShow) {
        super(context, R.layout.day_timetable_item, values);
        this.context = context;
        this.values = values;
        this.dateToShow = dateToShow;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        setUp();

        // TODO needs optimization here. See http://habrahabr.ru/post/133575/ and
        // https://stackoverflow.com/questions/25381435/unconditional-layout-inflation-from-view-adapter-should-use-view-holder-patter
        View rowView = inflater.inflate(R.layout.day_timetable_item, parent, false);
        View shortInfo = inflater.inflate(R.layout.class_short_info, parent, false);

        LinearLayout item_cover = (LinearLayout) rowView.findViewById(R.id.item_cover);
        TextView time = (TextView) rowView.findViewById(R.id.class_time_interval);
        FrameLayout container = (FrameLayout) rowView.findViewById(R.id.class_container);
        container.removeAllViews();

        TextView className = (TextView) shortInfo.findViewById(R.id.class_name);
        TextView classType = (TextView) shortInfo.findViewById(R.id.class_type);
        TextView classroom = (TextView) shortInfo.findViewById(R.id.classroom);
        TextView teacherName = (TextView) shortInfo.findViewById(R.id.teacher_name);

        TimeInterval time_int = values[position].time;
        time.setText(time_int.toString());
        Resources res = this.context.getResources();

        Date nowDate = new Date();

        if (nowDate.compare(dateToShow) == 0) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            switch (time_int.compare(hour, minute)) {
                case (-1):
                    item_cover.setBackgroundColor(res.getColor(R.color.blue));
                    break;
                case (0):
                    item_cover.setBackgroundColor(res.getColor(R.color.yellow));
                    break;
                case (1):
                    item_cover.setBackgroundColor(res.getColor(R.color.green));
                    break;
            }
        } else {
            if (nowDate.compare(dateToShow) == -1) {
                item_cover.setBackgroundColor(res.getColor(R.color.green));
            } else {
                item_cover.setBackgroundColor(res.getColor(R.color.blue));
            }
        }

        className.setText(values[position].subject);
        classType.setText(values[position].classType);
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
