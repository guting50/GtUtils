package com.gt.utils.widget.multigridview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RainhowChan on 2016/4/22.
 */
public class CommonViewHolder {

    public final View convertView;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, View> views = new HashMap<Integer, View>();

    public CommonViewHolder(View conventView) {
        this.convertView = conventView;
        this.convertView.setTag(this);
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T getView(int viewId) {
        if (views.get(viewId) == null) {
            View v = convertView.findViewById(viewId);
            views.put(viewId, v);
        }
        return (T) views.get(viewId);
    }

    public <T extends View> T getView(int viewId, Class<T> clazz) {
        return getView(viewId);
    }

    public TextView getTextView(int viewId) {
        return getView(viewId, TextView.class);
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId, ImageView.class);
    }

    public EditText getEditView(int viewId) {
        return getView(viewId, EditText.class);
    }

    public static CommonViewHolder getCommonViewHolder(Context context, View convertView, int itemLayout) {
        if (convertView == null) {
            convertView = View.inflate(context, itemLayout, null);
            return new CommonViewHolder(convertView);
        } else {
            return (CommonViewHolder) convertView.getTag();
        }
    }
}
