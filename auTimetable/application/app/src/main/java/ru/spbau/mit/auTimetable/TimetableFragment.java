package ru.spbau.mit.auTimetable;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by equi on 15.11.15.
 *
 * @author Kravchenko Dima
 */
public class TimetableFragment extends Fragment {
    private View mDayInformation;
    private ListView mDayTimetable;
    private LinearLayout mWholeScreen;
    private Button leftButton;
    private Button rightButton;

    private TextView currentDayName;
    private TextView currentDate;

    private Date date;

    private boolean wasSetUp = false;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public TimetableFragment() {
        date = new Date();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUp(inflater, container);

        date.update();
        currentDayName.setText(date.dayName);
        currentDate.setText(date.day + "." + date.month + "." + date.year);

        updateDayTimetable();

        return mWholeScreen;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void setUp(LayoutInflater inflater, ViewGroup container) {
        if (wasSetUp) {
            return;
        }

        mWholeScreen = (LinearLayout) inflater.inflate(
                R.layout.fragment_timetable, container, false);

        mDayInformation = mWholeScreen.findViewById(R.id.day_information);
        mDayTimetable = (ListView) mWholeScreen.findViewById(R.id.day_timetable);

        currentDayName = (TextView) mDayInformation.findViewById(R.id.day);
        currentDate = (TextView) mDayInformation.findViewById(R.id.date);

        leftButton = (Button) mWholeScreen.findViewById(R.id.left_button);
        rightButton = (Button) mWholeScreen.findViewById(R.id.right_button);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.previousDay();
                currentDayName.setText(date.dayName);
                currentDate.setText(date.day + "." + date.month + "." + date.year);
                updateDayTimetable();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.nextDay();
                currentDayName.setText(date.dayName);
                currentDate.setText(date.day + "." + date.month + "." + date.year);
                updateDayTimetable();
            }
        });

        wasSetUp = true;
    }

    private void updateDayTimetable() {
        int parity = 0;

        ClassInfo classes[] = new ClassInfo[] {
                new ClassInfo("10-12", "Матан", "208", "Храбров"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов"),
                new ClassInfo("12-14", "Алгебра", "206", "Всемирнов")
        };

        mDayTimetable.setAdapter(new TimetableAdapter(
                getActionBar().getThemedContext(),
                classes
        ));
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private class Date {
        public String dayName;
        public int day;
        public int month;
        public int year;
        public int dayOfWeek;

        private Calendar calendar;

        private final List<String> daysNames = Arrays.asList(
                "Воскресенье",
                "Понедельник",
                "Вторник",
                "Среда",
                "Четверг",
                "Пятница",
                "Суббота"
        );

        public Date() {
            update();
        }

        public void update() {
            updateCalendar();
            updateDate();
        }

        public void updateCalendar() {
            calendar = Calendar.getInstance();
        }

        public void updateDate() {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dayName = daysNames.get(dayOfWeek - 1);
        }

        public void previousDay() {
            calendar.add(Calendar.DATE, -1);
            updateDate();
        }

        public void nextDay() {
            calendar.add(Calendar.DATE, 1);
            updateDate();
        }
    }
}
