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
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.gt.utils.R;

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
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;

    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;
    private int defaultHour;
    private int defaultMinute;
    /*结束的年份*/
    private int endYear;

    TextView tv_cancle;
    TextView tv_sure;
    WheelView loop_year;
    WheelView loop_month;
    WheelView loop_day;
    WheelView loop_hour;
    WheelView loop_minute;

    private ArrayList<String> yearList;
    private ArrayList<String> monthList;
    private ArrayList<String> dayList;
    private ArrayList<String> hourList;
    private ArrayList<String> minuteList;

    private OnSelectedListener onSelectedListener;

    private String datePattern = "yyyy-MM-dd HH:mm";
    private Date defauleDate;

    public DateSelectDialog(@NonNull Context context) {
        super(context, R.style.DateSelectDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_date_select, null);
        setContentView(view);
        tv_cancle = findViewById(R.id.tv_cancle);
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
        defauleDate = date;
        return this;
    }

    private void initTimeData() {
        Calendar calendar = Calendar.getInstance();
        if (defauleDate != null) {
            calendar.setTime(defauleDate);
        }
        defaultYear = currentYear = calendar.get(Calendar.YEAR);
        endYear = currentYear + 4;
        defaultMonth = currentMonth = calendar.get(Calendar.MONTH);
        defaultDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        defaultHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        defaultMinute = currentMinute = calendar.get(Calendar.MINUTE);
    }

    private void initYmdPop() {
        yearList = new ArrayList<>();
        monthList = new ArrayList<>();
        dayList = new ArrayList<>();
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();
        //设置年份数据
        for (int i = defaultYear; i <= endYear; i++) {
            yearList.add(i + "年");
        }
        loop_year.setAdapter(new ArrayWheelAdapter(yearList));
        loop_year.setCurrentItem(0);
        updateMonthList(0);

        loop_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                updateMonthList(index);
                updateWheel();
            }
        });
        loop_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                updateDaysList(index);
                updateWheel();
            }
        });
        loop_day.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                updateHourList(index);
                updateWheel();
            }
        });
        loop_hour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                updateMinuteList(index);
                updateWheel();
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String date = yearList.get(loop_year.getCurrentItem()) + monthList.get(loop_month.getCurrentItem()) + dayList.get(loop_day.getCurrentItem())
                            + hourList.get(loop_hour.getCurrentItem()) + minuteList.get(loop_minute.getCurrentItem());
                    String selectYMD = new SimpleDateFormat(datePattern).format(new SimpleDateFormat("yyyy年MM月dd日HH时mm分").parse(date));
                    if (onSelectedListener != null) {
                        onSelectedListener.onSelected(selectYMD);
                    }
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void updateMonthList(int index) {
        currentYear = defaultYear + index;

        monthList.clear();
        if (currentYear == defaultYear) {
            for (int i = defaultMonth; i <= 11; i++) {
                monthList.add(i + 1 + "月");
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                monthList.add(i + "月");
            }
        }
        if (loop_month.getAdapter() == null)
            loop_month.setAdapter(new ArrayWheelAdapter(monthList));
        loop_month.setCurrentItem(0);

        updateDaysList(0);
    }

    private void updateDaysList(int index) {
        if (currentYear == defaultYear) {
            currentMonth = defaultMonth + index;
        } else {
            currentMonth = index;
        }

        dayList.clear();
        int totalDays = getDays(isLeapYear(currentYear), currentMonth);
        if (currentYear == defaultYear && currentMonth == defaultMonth) {
            for (int i = defaultDay; i <= totalDays; i++) {
                dayList.add(i + "日");
            }
        } else {
            for (int i = 1; i <= totalDays; i++) {
                dayList.add(i + "日");
            }
        }
        if (loop_day.getAdapter() == null)
            loop_day.setAdapter(new ArrayWheelAdapter(dayList));
        loop_day.setCurrentItem(0);

        updateHourList(0);
    }

    private void updateHourList(int index) {
        if (currentMonth == defaultMonth) {
            currentDay = defaultDay + index;
        } else {
            currentDay = index;
        }

        hourList.clear();
        if (currentYear == defaultYear && currentMonth == defaultMonth && currentDay == defaultDay) {
            for (int i = defaultHour; i <= 23; i++) {
                hourList.add(i + "时");
            }
        } else {
            for (int i = 0; i <= 23; i++) {
                hourList.add(i + "时");
            }
        }
        if (loop_hour.getAdapter() == null)
            loop_hour.setAdapter(new ArrayWheelAdapter(hourList));
        loop_hour.setCurrentItem(0);

        updateMinuteList(0);
    }

    private boolean aaa = false;

    private void updateMinuteList(int index) {
        if (currentDay == defaultDay) {
            currentHour = defaultHour + index;
        } else {
            currentHour = index;
        }

        minuteList.clear();
        if (aaa) {
            for (int i = 0; i <= 45; i += 15) {
                minuteList.add(i + "分");
            }
        } else {
            if (currentYear == defaultYear && currentMonth == defaultMonth && currentDay == defaultDay && currentHour == defaultHour) {
                for (int i = defaultMinute; i <= 59; i++) {
                    minuteList.add(i + "分");
                }
            } else {
                for (int i = 0; i <= 59; i++) {
                    minuteList.add(i + "分");
                }
            }
        }
        if (loop_minute.getAdapter() == null)
            loop_minute.setAdapter(new ArrayWheelAdapter(minuteList));
        loop_minute.setCurrentItem(0);
    }

    long delay = 300;
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

    class ArrayWheelAdapter<T> implements WheelAdapter {
        // items
        private List<T> items;

        /**
         * Constructor
         *
         * @param items the items
         */
        public ArrayWheelAdapter(List<T> items) {
            this.items = items;

        }

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

    }

    public DateSelectDialog setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelected(String data);
    }
}
