package com.willnjames.android.morgbook;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.willnjames.android.morgbook.Database.DatabaseAccess;
import com.willnjames.android.morgbook.Model.Meeting;
import com.willnjames.android.morgbook.Model.Person;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public abstract class MeetingsActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;


    private TextView dateText;

    private Button button;

    private ArrayList<WeekViewEvent> mNewEvents;

    final Context context = this;

    private DatePicker datePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    private String datemonth;
    private String start;
    private String end;

    DatabaseAccess dbAccess;

    private int startHour;
    private int startMinute;

    private int endHour;
    private int endMinute;

    private int meetingDate;
    private int meetingMonth;

    String[] topic;

    private Spinner topicSpinner;
    private Spinner studentSpinner;
    private EditText locationText;

    private String studentEntry;

    private String topicEntry;
    private String locationEntry;

    private TextView selectedEvent;

    private ArrayList<String> students = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetings_activity);


        dbAccess = DatabaseAccess.getInstance(this);

        topic = new String[]{"General", "Assignment", "Consultation", "Other"};

        dbAccess.open();

            ArrayList<Person> persons = dbAccess.getStudents();

        dbAccess.close();

        for(int i=0;i<persons.size();i++){
            String st = String.valueOf(persons.get(i).getZ_ID());
            students.add(st);
        }


        dateText = (TextView) findViewById(R.id.dateText);

        selectedEvent = (TextView) findViewById(R.id.textView11);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        SimpleDateFormat sdm = new SimpleDateFormat("MMM");
        Date dd = new Date();
        String month = sdm.format(dd);

        String sDate = dayOfTheWeek +", "+c.get(Calendar.DAY_OF_MONTH)+" "+month+" "+c.get(Calendar.YEAR);

        dateText.setText(sDate);

        mNewEvents = new ArrayList<WeekViewEvent>();

        dbAccess.open();
        ArrayList<Meeting> meet = dbAccess.getMeeting();
        dbAccess.close();

        for(int i=0;i<meet.size();i++){

            String q = meet.get(i).getDate();
            String[] parts = q.split("/");
            String part1 = parts[0];
            String part2 = parts[1];

            String w = meet.get(i).getStartTime();
            String[] parts2 = w.split(":");
            String part3 = parts2[0];
            String part4 = parts2[1];

            String e = meet.get(i).getEndTime();
            String[] parts3 = e.split(":");
            String part5 = parts3[0];
            String part6 = parts3[1];

            String stu = "z"+String.valueOf(meet.get(i).getStudentZID());
            String top = meet.get(i).getTopic();
            String place = meet.get(i).getRoom();

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.DAY_OF_MONTH, Integer.valueOf(part1));
            startTime.set(Calendar.MONTH, Integer.valueOf(part2));
            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(part3));
            startTime.set(Calendar.MINUTE, Integer.valueOf(part4));

            Calendar endTime = (Calendar) startTime.clone();
            endTime.set(Calendar.HOUR, Integer.valueOf(part5));
            endTime.set(Calendar.MINUTE, Integer.valueOf(part6));

            WeekViewEvent event = new WeekViewEvent(0, stu+" "+"\n"+top+"\n" , place, startTime, endTime);
            mNewEvents.add(event);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, students);

        button = (Button) findViewById(R.id.button6);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Custom Dialog");

                dialog.show();

                datePicker = (DatePicker) dialog.findViewById(R.id.datePicker1);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);

                startTimePicker = (TimePicker) dialog.findViewById(R.id.timePicker1);
                startTimePicker.setIs24HourView(true);

                endTimePicker = (TimePicker) dialog.findViewById(R.id.timePicker2);
                endTimePicker.setIs24HourView(true);

                topicSpinner = (Spinner) dialog.findViewById(R.id.spinner);
                studentSpinner = (Spinner) dialog.findViewById(R.id.spinner2);

                studentSpinner.setAdapter(adapter);

                locationText = (EditText) dialog.findViewById(R.id.editText);

                Button button = (Button) dialog.findViewById(R.id.button);

                Button cancel = (Button) dialog.findViewById(R.id.button2);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        meetingDate = datePicker.getDayOfMonth();
                        meetingMonth = datePicker.getMonth();

                        datemonth = String.valueOf(meetingDate)+"/"+String.valueOf(meetingMonth);

                        startHour = startTimePicker.getHour();
                        startMinute = startTimePicker.getMinute();

                        start = String.valueOf(startHour)+":"+String.valueOf(startMinute);

                        endHour = endTimePicker.getHour();
                        endMinute = endTimePicker.getMinute();

                        end = String.valueOf(endHour)+":"+String.valueOf(endMinute);

                        locationEntry = String.valueOf(locationText.getText());
                        topicEntry = topicSpinner.getSelectedItem().toString();
                        studentEntry = studentSpinner.getSelectedItem().toString();

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_MONTH, meetingDate);
                        startTime.set(Calendar.MONTH, meetingMonth);
                        startTime.set(Calendar.HOUR_OF_DAY, startHour);
                        startTime.set(Calendar.MINUTE, startMinute);

                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.set(Calendar.HOUR, endHour);
                        endTime.set(Calendar.MINUTE, endMinute);

                        String eventInfo = "z"+studentEntry+" " + "\n" +topicEntry+"\n";

                        WeekViewEvent event = new WeekViewEvent(0, eventInfo, locationEntry, startTime, endTime);
                        mNewEvents.add(event);
                        getWeekView().notifyDatasetChanged();


                        String evenMoreInfo = eventInfo + "\n" + "Location: "+locationEntry+ "\n" + meetingDate+"/"+meetingMonth+ "\n"+ startHour+":"+startMinute+ " - " +endHour+":"+endMinute;

                        Meeting meeting = new Meeting(Integer.valueOf(studentEntry), 5018453, datemonth, start, end, topicEntry, locationEntry);
                        dbAccess.open();
                        dbAccess.addMeeting(meeting);
                        dbAccess.close();

                        dialog.dismiss();
                    }
                });
            }
        });

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);


        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);


        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        mWeekView.goToHour(Calendar.HOUR_OF_DAY);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id) {
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     *
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        String str = event.getName();
        String[] split = str.split(" ");
        String p0 = split[0];
        p0 = p0.trim();
        String p1 = split[1];
        p1 = p1.trim();
        //Toast.makeText(this, p1+" Appointment", Toast.LENGTH_SHORT).show();

        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        fmt.setCalendar(event.getStartTime());
        String dateFormatted = fmt.format(event.getStartTime().getTime());

        Calendar cal = event.getStartTime();
        String a = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":"+String.valueOf(cal.get(Calendar.MINUTE));

        Calendar cale = event.getEndTime();
        String b = String.valueOf(cale.get(Calendar.HOUR_OF_DAY)) + ":"+String.valueOf(cale.get(Calendar.MINUTE));


        String info = "Student: "+p0+"\n"
                        + "Time: "+a+ " - "+b+"\n"
                        + "Topic: "+p1+"             "
                        + "Location: "+event.getLocation();

        selectedEvent.setText(info);
        selectedEvent.setLineSpacing(0, 1.5f);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();

    }
    List<WeekViewEvent> events;

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        ArrayList<WeekViewEvent> newEvents = getNewEvents(newYear, newMonth);

        events.addAll(newEvents);

        return events;
    }

    public WeekView getWeekView() {

        return mWeekView;
    }

    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 0);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                event.setColor(getResources().getColor(R.color.event_color_04));
                events.add(event);
            }
        }
        return events;
    }

}