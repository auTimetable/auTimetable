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


public class TimetableFragment extends Fragment {
    private ListView mDayTimetable;
    private LinearLayout mWholeScreen;
    private TextView currentDayName;
    private TextView currentDate;

    private Date date;

    private boolean wasSetUp = false;

    private XMLParser parser = null;

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

        Bundle args = getArguments();
        int groupNumber = args.getInt("group_number", 0);
        int subgroupNumber = args.getInt("subgroup_number", 0);

        parser = XMLParser.Builder.build(getActionBar().getThemedContext(),
                getActivity(), groupNumber, subgroupNumber);

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

        View mDayInformation = mWholeScreen.findViewById(R.id.day_information);
        mDayTimetable = (ListView) mWholeScreen.findViewById(R.id.day_timetable);

        currentDayName = (TextView) mDayInformation.findViewById(R.id.day);
        currentDate = (TextView) mDayInformation.findViewById(R.id.date);

        Button leftButton = (Button) mWholeScreen.findViewById(R.id.left_button);
        Button rightButton = (Button) mWholeScreen.findViewById(R.id.right_button);

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

        WeekInfo weekInfo = parser.getWeek(parity);
        DayInfo dayInfo = weekInfo.getDay(date.dayNameEn);

        ClassInfo classes[] = new ClassInfo[dayInfo.classList.size()];
        classes = dayInfo.classList.toArray(classes);

        mDayTimetable.setAdapter(new TimetableAdapter(
                getActionBar().getThemedContext(),
                classes,
                date
        ));
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }
}
