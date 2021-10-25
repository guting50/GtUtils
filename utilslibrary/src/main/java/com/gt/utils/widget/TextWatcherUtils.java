package com.gt.utils.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextWatcherUtils {

    private View bnView;
    private Map<View, Boolean> checkList;
    private List<TextView> views;
    private List<OnCheckedListener> onCheckedListeners = new ArrayList<>();

    public TextWatcherUtils(View bnView, TextView... textViews) {
        this.bnView = bnView;
        this.views = ArrayUtils.asArrayList(textViews);
        init();
    }

    public void init() {
        this.bnView.setEnabled(false);
        checkList = new HashMap<>();
        for (TextView tv : views) {
            try {
                Field f;
                if (tv instanceof EditText) {
                    f = tv.getClass().getSuperclass().getSuperclass().getDeclaredField("mListeners");
                } else {
                    f = tv.getClass().getSuperclass().getDeclaredField("mListeners");
                }
                f.setAccessible(true);
                ArrayList<TextWatcher> obj = (ArrayList<TextWatcher>) f.get(tv);
                if (obj != null)
                    for (TextWatcher textWatcher : obj) {
                        if (textWatcher instanceof TextWatcherCustom) {
                            tv.removeTextChangedListener(textWatcher);
                        }
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            tv.addTextChangedListener(new TextWatcherCustom(tv));
            if (!TextUtils.isEmpty(tv.getText()))
                checkList.put(tv, true);
            else
                checkList.put(tv, false);
        }
        check();
    }

    class TextWatcherCustom implements TextWatcher {

        private View textView;

        public TextWatcherCustom(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s))
                checkList.put(textView, true);
            else
                checkList.put(textView, false);
            check();
        }
    }

    public void check() {
        if (checkList.containsValue(false)) {
            bnView.setEnabled(false);
        } else {
            bnView.setEnabled(true);
        }
        for (OnCheckedListener onCheckedListener : onCheckedListeners) {
            onCheckedListener.onChecked(bnView);
        }
    }

    public void addOnCheckedListener(OnCheckedListener onCheckedListener) {
        onCheckedListeners.add(onCheckedListener);
    }

    public void removeOnCheckedListener(OnCheckedListener onCheckedListener) {
        onCheckedListeners.remove(onCheckedListener);
    }

    public void removeOnCheckedListener() {
        onCheckedListeners.clear();
    }

    public void addView(TextView... textViews) {
        for (TextView textView : textViews) {
            if (!views.contains(textView))
                views.add(textView);
        }
        init();
    }

    public void removeView(TextView... textViews) {
        for (TextView textView : textViews) {
            views.remove(textView);
        }
        init();
    }

    public interface OnCheckedListener {
        void onChecked(View bnView);
    }
}
