package ru.spbau.mit.auTimetable;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;


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

        /**
         * TODO
         * needs optimization here. See http://habrahabr.ru/post/133575/ and
         * https://stackoverflow.com/questions/25381435/unconditional-layout-inflation-from-view-adapter-should-use-view-holder-patter
         */
        View rowView = inflater.inflate(R.layout.day_timetable_item, parent, false);
        View shortInfo = inflater.inflate(R.layout.class_short_info, parent, false);

        FrameLayout container = (FrameLayout) rowView.findViewById(R.id.class_container);
        container.removeAllViews();

        RowItem rowItem = new RowItem(position);
        rowItem.setUp(rowView, shortInfo);

        container.addView(shortInfo);

        return rowView;
    }

    private void setUp() {
        if (wasSetUp)
            return;
        wasSetUp = true;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class RowItem {
        public TextView time;
        public TextView className;
        public TextView classType;
        public TextView classroom;
        public TextView teacherName;

        public LinearLayout item_cover;

        private int position;

        private TimeInterval time_int;

        public RowItem(int position) {
            this.position = position;
            time_int = values[position].time;
        }

        public void setUp(View rowView, View shortInfo) {
            setUpComponents(rowView, shortInfo);
            setClassComponents();
            setColour();
        }

        private void setUpComponents(View rowView, View shortInfo) {
            item_cover = (LinearLayout) rowView.findViewById(R.id.item_cover);
            time = (TextView) rowView.findViewById(R.id.class_time_interval);
            className = (TextView) shortInfo.findViewById(R.id.class_name);
            classType = (TextView) shortInfo.findViewById(R.id.class_type);
            classroom = (TextView) shortInfo.findViewById(R.id.classroom);
            teacherName = (TextView) shortInfo.findViewById(R.id.teacher_name);
        }

        private void setClassComponents() {
            time.setText(time_int.toString());
            className.setText(values[position].subject);
            classType.setText(values[position].classType);
            classroom.setText(values[position].classroom);
            teacherName.setText(values[position].teacherName);
        }

        private void setColour() {
            Date nowDate = new Date();
            Resources res = TimetableAdapter.this.context.getResources();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            setColor(res, nowDate.compare(dateToShow), time_int.compare(hour, minute));
        }

        @SuppressWarnings("deprecation")
        private void setColor(Resources res, int compareDay, int compareInterval) {
            if (compareDay == 0) {
                switch (compareInterval) {
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
                if (compareDay == -1) {
                    item_cover.setBackgroundColor(res.getColor(R.color.blue));
                } else {
                    item_cover.setBackgroundColor(res.getColor(R.color.green));
                }
            }
        }
    }
}
