package com.gt.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;
import com.gt.utils.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;

public class DateSelectDialog extends Dialog {
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;

    private Calendar startCalendar;
    private Calendar endCalendar;

    TextView tv_cancel;
    TextView tv_sure;
    WheelView loop_year;
    WheelView loop_month;
    WheelView loop_day;
    WheelView loop_hour;
    WheelView loop_minute;

    private ArrayWheelAdapter yearAdapter;
    private ArrayWheelAdapter monthAdapter;
    private ArrayWheelAdapter dayAdapter;
    private ArrayWheelAdapter hourAdapter;
    private ArrayWheelAdapter minuteAdapter;

    private DateSelectDialog.OnSelectedListener onSelectedListener;

    private String datePattern = "yyyy-MM-dd HH:mm";
    private Date defaultDate;

    public DateSelectDialog(@NonNull Context context) {
        super(context, R.style.DateSelectDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_date_select, null);
        setContentView(view);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_sure = findViewById(R.id.tv_sure);
        loop_year = findViewById(R.id.loop_year);
        loop_month = findViewById(R.id.loop_month);
        loop_day = findViewById(R.id.loop_day);
        loop_hour = findViewById(R.id.loop_hour);
        loop_minute = findViewById(R.id.loop_minute);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        initTimeData();
        initYmdPop();
        setView();
    }

    public void show() {
        super.show();

        if (defaultDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
        }

        yearAdapter.setItems(getYearList());
        loop_year.setCurrentItem(yearAdapter.indexOf(currentYear + "年"));
        monthAdapter.setItems(getMonthList());
        loop_month.setCurrentItem(monthAdapter.indexOf(currentMonth + 1 + "月"));
        dayAdapter.setItems(getDayList());
        loop_day.setCurrentItem(dayAdapter.indexOf(currentDay + "日"));
        hourAdapter.setItems(getHourList());
        loop_hour.setCurrentItem(hourAdapter.indexOf(currentHour + "时"));
        minuteAdapter.setItems(getMinuteList());
        loop_minute.setCurrentItem(minuteAdapter.indexOf(currentMinute + "分"));
    }

    public void setView() {
        loop_year.setVisibility(View.GONE);
        loop_month.setVisibility(View.GONE);
        loop_day.setVisibility(View.GONE);
        loop_hour.setVisibility(View.GONE);
        loop_minute.setVisibility(View.GONE);
        if (this.datePattern.contains("yyyy")) {
            loop_year.setVisibility(View.VISIBLE);
        }
        if (this.datePattern.contains("MM")) {
            loop_month.setVisibility(View.VISIBLE);
        }
        if (this.datePattern.contains("dd")) {
            loop_day.setVisibility(View.VISIBLE);
        }
        if (this.datePattern.contains("HH")) {
            loop_hour.setVisibility(View.VISIBLE);
        }
        if (this.datePattern.contains("mm")) {
            loop_minute.setVisibility(View.VISIBLE);
        }
    }

    public DateSelectDialog setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public DateSelectDialog setDefaultDate(Date date) {
        this.defaultDate = date;
        return this;
    }

    public DateSelectDialog setDefaultDate(String dateStr) {
        return setDefaultDate(dateStr, datePattern);
    }

    public DateSelectDialog setDefaultDate(String dateStr, String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return setDefaultDate(date);
    }

    public DateSelectDialog setStartDate(Date date) {
        if (startCalendar == null) {
            startCalendar = Calendar.getInstance();
        }
        startCalendar.setTime(date);
        return this;
    }

    public DateSelectDialog setStartDate(String dateStr) {
        return setStartDate(dateStr, datePattern);
    }

    public DateSelectDialog setStartDate(String dateStr, String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return setStartDate(date);
    }

    public DateSelectDialog setEndDate(Date date) {
        if (endCalendar == null) {
            endCalendar = Calendar.getInstance();
        }
        endCalendar.setTime(date);
        return this;
    }

    public DateSelectDialog setEndDate(String dateStr) {
        return setEndDate(dateStr, datePattern);
    }

    public DateSelectDialog setEndDate(String dateStr, String pattern) {
        Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return setEndDate(date);
    }

    private void initTimeData() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        if (endCalendar == null) {
            endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.YEAR, currentYear + 5);
        }
        if (startCalendar == null) {
            startCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.YEAR, currentYear - 5);
        }
    }

    private void initYmdPop() {
        loop_year.setAdapter(yearAdapter = new DateSelectDialog.ArrayWheelAdapter());
        loop_month.setAdapter(monthAdapter = new DateSelectDialog.ArrayWheelAdapter());
        loop_day.setAdapter(dayAdapter = new DateSelectDialog.ArrayWheelAdapter());
        loop_hour.setAdapter(hourAdapter = new DateSelectDialog.ArrayWheelAdapter());
        loop_minute.setAdapter(minuteAdapter = new DateSelectDialog.ArrayWheelAdapter());

        loop_year.setOnItemSelectedListener(index -> {
            updateMonthList(index);
            updateWheel();
        });
        loop_month.setOnItemSelectedListener(index -> {
            updateDaysList(index);
            updateWheel();
        });
        loop_day.setOnItemSelectedListener(index -> {
            updateHourList(index);
            updateWheel();
        });
        loop_hour.setOnItemSelectedListener(index -> {
            updateMinuteList(index);
            updateWheel();
        });

        tv_sure.setOnClickListener(v -> {
            try {
                String date = yearAdapter.getData(loop_year.getCurrentItem())
                        + monthAdapter.getData(loop_month.getCurrentItem())
                        + dayAdapter.getData(loop_day.getCurrentItem())
                        + hourAdapter.getData(loop_hour.getCurrentItem())
                        + minuteAdapter.getData(loop_minute.getCurrentItem());
                String selectYMD = new SimpleDateFormat(datePattern).format(new SimpleDateFormat("yyyy年MM月dd日HH时mm分").parse(date));
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(selectYMD);
                }
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tv_cancel.setOnClickListener(v -> dismiss());
    }

    private void updateYearList(int yearIndex) {
        yearAdapter.setItems(getYearList());
        loop_year.setCurrentItem(yearIndex);
        updateMonthList(0);
    }

    private void updateMonthList(int yearIndex) {
        String year = yearAdapter.getItems().get(yearIndex);
        currentYear = Integer.parseInt(year.substring(0, year.length() - 1));
        monthAdapter.setItems(getMonthList());
        loop_month.setCurrentItem(0);
        updateDaysList(0);
    }

    private void updateDaysList(int monthIndex) {
        String month = monthAdapter.getItems().get(monthIndex);
        currentMonth = Integer.parseInt(month.substring(0, month.length() - 1)) - 1;
        dayAdapter.setItems(getDayList());
        loop_day.setCurrentItem(0);
        updateHourList(0);
    }

    private void updateHourList(int dayIndex) {
        String day = dayAdapter.getItems().get(dayIndex);
        currentDay = Integer.parseInt(day.substring(0, day.length() - 1));
        hourAdapter.setItems(getHourList());
        loop_hour.setCurrentItem(0);
        updateMinuteList(0);
    }

    private void updateMinuteList(int hourIndex) {
        String hour = hourAdapter.getItems().get(hourIndex);
        currentHour = Integer.parseInt(hour.substring(0, hour.length() - 1));
        minuteAdapter.setItems(getMinuteList());
        loop_minute.setCurrentItem(0);
    }

    private ArrayList<String> getYearList() {
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = startCalendar.get(Calendar.YEAR); i <= endCalendar.get(Calendar.YEAR); i++) {
            yearList.add(i + "年");
        }
        return yearList;
    }

    private ArrayList<String> getMonthList() {
        ArrayList<String> monthList = new ArrayList<>();
        int i = 0, totalMonths = 11;
        if (currentYear == startCalendar.get(Calendar.YEAR)) {
            i = startCalendar.get(Calendar.MONTH);
        }
        if (currentYear == endCalendar.get(Calendar.YEAR)) {
            totalMonths = endCalendar.get(Calendar.MONTH);
        }
        for (; i <= totalMonths; i++) {
            monthList.add(i + 1 + "月");
        }
        return monthList;
    }

    private ArrayList<String> getDayList() {
        ArrayList<String> dayList = new ArrayList<>();
        int i = 1, totalDays = getDays(isLeapYear(currentYear), currentMonth);
        if (currentYear == startCalendar.get(Calendar.YEAR) &&
                currentMonth == startCalendar.get(Calendar.MONTH)) {
            i = startCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if (currentYear == endCalendar.get(Calendar.YEAR) &&
                currentMonth == endCalendar.get(Calendar.MONTH)) {
            totalDays = endCalendar.get(Calendar.DAY_OF_MONTH);
        }
        for (; i <= totalDays; i++) {
            dayList.add(i + "日");
        }
        return dayList;
    }

    private ArrayList<String> getHourList() {
        ArrayList<String> hourList = new ArrayList<>();
        int i = 0, totalHours = 23;
        if (currentYear == startCalendar.get(Calendar.YEAR) &&
                currentMonth == startCalendar.get(Calendar.MONTH) &&
                currentDay == startCalendar.get(Calendar.DAY_OF_MONTH)) {
            i = startCalendar.get(Calendar.HOUR_OF_DAY);
        }
        if (currentYear == endCalendar.get(Calendar.YEAR) &&
                currentMonth == endCalendar.get(Calendar.MONTH) &&
                currentDay == endCalendar.get(Calendar.DAY_OF_MONTH)) {
            totalHours = endCalendar.get(Calendar.HOUR_OF_DAY);
        }
        for (; i <= totalHours; i++) {
            hourList.add(i + "时");
        }
        return hourList;
    }

    private ArrayList<String> getMinuteList() {
        ArrayList<String> minuteList = new ArrayList<>();
        int i = 0, totalMinutes = 59;
        if (currentYear == startCalendar.get(Calendar.YEAR) &&
                currentMonth == startCalendar.get(Calendar.MONTH) &&
                currentDay == startCalendar.get(Calendar.DAY_OF_MONTH) &&
                currentHour == startCalendar.get(Calendar.HOUR_OF_DAY)) {
            i = startCalendar.get(Calendar.MINUTE);
        }
        if (currentYear == endCalendar.get(Calendar.YEAR) &&
                currentMonth == endCalendar.get(Calendar.MONTH) &&
                currentDay == endCalendar.get(Calendar.DAY_OF_MONTH) &&
                currentHour == endCalendar.get(Calendar.HOUR_OF_DAY)) {
            totalMinutes = endCalendar.get(Calendar.MINUTE);
        }
        for (; i <= totalMinutes; i++) {
            minuteList.add(i + "分");
        }
        return minuteList;
    }

    long delay = 500;
    Timer timer;

    //    private void updateWheel(final WheelView wheelView) {
    private void updateWheel() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    loop_year.setCurrentItem(loop_year.getCurrentItem());
                    loop_month.setCurrentItem(loop_month.getCurrentItem());
                    loop_day.setCurrentItem(loop_day.getCurrentItem());
                    loop_hour.setCurrentItem(loop_hour.getCurrentItem());
                    loop_minute.setCurrentItem(loop_minute.getCurrentItem());
                    timer.cancel();
                    timer = null;
                }
            }, delay);
        }
    }

    /*建立一个判断闰年的方法*/
    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            /*非闰年*/
            if (year % 100 == 0 && year % 400 != 0) {
                return false;
            }
            /*闰年*/
            else {
                return true;
            }
        } else {
            return false;
        }
    }

    /*建立一个获取当年当月天数的方法*/
    private int getDays(boolean isLeapYear, int month) {
        int days = 0;
        switch (month) {
            case 0:
                days = 31;
                break;
            case 1:
                if (isLeapYear) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            case 2:
                days = 31;
                break;
            case 3:
                days = 30;
                break;
            case 4:
                days = 31;
                break;
            case 5:
                days = 30;
                break;
            case 6:
                days = 31;
                break;
            case 7:
                days = 31;
                break;
            case 8:
                days = 30;
                break;
            case 9:
                days = 31;
                break;
            case 10:
                days = 30;
                break;
            case 11:
                days = 31;
                break;
        }
        return days;
    }

    class ArrayWheelAdapter implements WheelAdapter {
        // items
        private List<String> items = new ArrayList<>();

        @Override
        public Object getItem(int index) {
            if (index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return "";
        }

        @Override
        public int getItemsCount() {
            return items.size();
        }

        @Override
        public int indexOf(Object o) {
            return items.indexOf(o);
        }

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public String getData(int position) {
            return items.get(position);
        }
    }

    public DateSelectDialog setOnSelectedListener(DateSelectDialog.OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelected(String data);
    }
}