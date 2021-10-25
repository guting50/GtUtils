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
    /*初始显示的年份*/
    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;
    private int defaultHour;
    private int defaultMinute;

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;

    /*结束的年份*/
    private int endYear;

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

    private OnSelectedListener onSelectedListener;

    private String datePattern = "yyyy-MM-dd HH:mm";
    private Date defaultDate;
    private boolean showGoOver = false;

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
            defaultYear = calendar.get(Calendar.YEAR);
            defaultMonth = calendar.get(Calendar.MONTH);
            defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
            defaultHour = calendar.get(Calendar.HOUR_OF_DAY);
            defaultMinute = calendar.get(Calendar.MINUTE);

            yearAdapter.setItems(getYearList());
            loop_year.setCurrentItem(yearAdapter.indexOf(defaultYear + "年"));
            monthAdapter.setItems(getMonthList());
            loop_month.setCurrentItem(monthAdapter.indexOf(defaultMonth + 1 + "月"));
            dayAdapter.setItems(getDayList());
            loop_day.setCurrentItem(dayAdapter.indexOf(defaultDay + "日"));
            hourAdapter.setItems(getHourList());
            loop_hour.setCurrentItem(hourAdapter.indexOf(defaultHour + "时"));
            minuteAdapter.setItems(getMinuteList());
            loop_minute.setCurrentItem(minuteAdapter.indexOf(defaultMinute + "分"));
        }
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

    public DateSelectDialog setDefauleDate(Date date) {
        defaultDate = date;
        return this;
    }

    private void initTimeData() {
        Calendar calendar = Calendar.getInstance();
        if (showGoOver) {
            SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
            try {
                calendar.setTime(formatter.parse("1970-01-01 00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        defaultYear = currentYear = calendar.get(Calendar.YEAR);
        defaultMonth = currentMonth = calendar.get(Calendar.MONTH);
        defaultDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        defaultHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        defaultMinute = currentMinute = calendar.get(Calendar.MINUTE);
        endYear = defaultYear + 4;
    }

    private void initYmdPop() {
        loop_year.setAdapter(yearAdapter = new ArrayWheelAdapter());
        loop_month.setAdapter(monthAdapter = new ArrayWheelAdapter());
        loop_day.setAdapter(dayAdapter = new ArrayWheelAdapter());
        loop_hour.setAdapter(hourAdapter = new ArrayWheelAdapter());
        loop_minute.setAdapter(minuteAdapter = new ArrayWheelAdapter());

        updateYearList(0);

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

    private void updateYearList(int index) {
        yearAdapter.setItems(getYearList());
        loop_year.setCurrentItem(index);
        updateMonthList(0);
    }

    private void updateMonthList(int yearIndex) {
        defaultYear = currentYear + yearIndex;
        monthAdapter.setItems(getMonthList());
        loop_month.setCurrentItem(0);
        updateDaysList(0);
    }

    private void updateDaysList(int monthIndex) {
        if (defaultYear == currentYear) {
            defaultMonth = currentMonth + monthIndex;
        } else {
            defaultMonth = monthIndex;
        }
        dayAdapter.setItems(getDayList());
        loop_day.setCurrentItem(0);
        updateHourList(0);
    }

    private void updateHourList(int index) {
        if (defaultMonth == currentMonth) {
            defaultDay = currentDay + index;
        } else {
            defaultDay = index;
        }
        hourAdapter.setItems(getHourList());
        loop_hour.setCurrentItem(0);
        updateMinuteList(0);
    }

    private void updateMinuteList(int index) {
        if (defaultDay == currentDay) {
            defaultHour = currentHour + index;
        } else {
            defaultHour = index;
        }
        minuteAdapter.setItems(getMinuteList());
        loop_minute.setCurrentItem(0);
    }

    private ArrayList<String> getYearList() {
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = currentYear; i <= endYear; i++) {
            yearList.add(i + "年");
        }
        return yearList;
    }

    private ArrayList<String> getMonthList() {
        ArrayList<String> monthList = new ArrayList<>();
        int i = 0;
        if (defaultYear == currentYear) {
            i = currentMonth;
        }
        for (; i <= 11; i++) {
            monthList.add(i + 1 + "月");
        }
        return monthList;
    }

    private ArrayList<String> getDayList() {
        ArrayList<String> dayList = new ArrayList<>();
        int i = 1;
        int totalDays = getDays(isLeapYear(defaultYear), defaultMonth);
        if (defaultYear == currentYear
                && defaultMonth == currentMonth) {
            i = currentDay;
        }
        for (; i <= totalDays; i++) {
            dayList.add(i + "日");
        }
        return dayList;
    }

    private ArrayList<String> getHourList() {
        ArrayList<String> hourList = new ArrayList<>();
        int i = 0;
        if (defaultYear == currentYear
                && defaultMonth == currentMonth
                && defaultDay == currentDay) {
            i = currentHour;
        }
        for (; i <= 23; i++) {
            hourList.add(i + "时");
        }
        return hourList;
    }

    private ArrayList<String> getMinuteList() {
        ArrayList<String> minuteList = new ArrayList<>();
        int i = 0;
        if (defaultYear == currentYear
                && defaultMonth == currentMonth
                && defaultDay == currentDay
                && defaultHour == currentHour) {
            i = currentMinute;
        }
        for (; i <= 59; i++) {
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

    public DateSelectDialog setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelected(String data);
    }
}
