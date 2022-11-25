package com.gt.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.gt.utils.BR;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此适配器适用于简单、单一的列表（每个item的布局都一样）
 * 通过反射获取自定义的Holder
 * 反射创建匿名内部类实例的时候（最好是使用静态匿名内部类）
 * 如果不是静态的匿名内部类，需要外部类的实例
 * 如果嵌套的有多层外部类，需要循环创建（尽量避免多层嵌套）
 * 当最外层的外部类为activity且该activity存在于activity堆栈中时，
 * ***不能直接newInstance，需要从堆栈中获取，如果堆栈中没有就newInstance
 * ***最好是把VHolder放在当前activity里面或者单独放在一个类中
 * 每层外部类都需要有一个无参构造函数,且构造函数里面尽量不要有逻辑性的东西，
 * ***防止在newInstance的时候报错
 * 当最外层为Fragment的时候，把当前fragment的实例传进来
 * ***（目前还没找到很好的方法来根据class获取堆栈中fragment的实例）
 * <p>
 * 当obj为null且VHolder为内部类时，因为是通过获取VHolder最外层的calss来反射的
 * 如果最外层的class是activity时，不能直接newInstance，需要从堆栈里面找，而找的
 * 方法是通过类名来找的，所以当如果堆栈里面有多个相同类名的activity实例时，就会
 * 因为获取的实例不对而出错，目前最好的办法就是带上obj
 * 当最外层为fragment的时候，也是不能直接newInstance的，所以也要带上obj
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VHolder> {

    private Class<? extends SimpleAdapter.VHolder> clazz;
    private Context context;
    private int resId;
    private List data = new ArrayList<>();
    private Object obj;

    public SimpleAdapter(Context context, int resId) {
        this.clazz = SimpleAdapter.DataBindingHolder.class;
        this.context = context;
        this.resId = resId;
    }

    public SimpleAdapter(@NonNull Class<? extends SimpleAdapter.VHolder> holder) {
        this.clazz = holder;
    }

    public SimpleAdapter(List data, @NonNull Class<? extends SimpleAdapter.VHolder> holder) {
        this.clazz = holder;
        this.data = data;
    }

    public SimpleAdapter(Object obj, @NonNull Class<? extends SimpleAdapter.VHolder> holder) {
        this.clazz = holder;
        this.obj = obj;
    }

    public SimpleAdapter(Object obj, List data, @NonNull Class<? extends SimpleAdapter.VHolder> holder) {
        this.clazz = holder;
        this.obj = obj;
        this.data = data;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            if (resId > 0) {
                Constructor ct = clazz.getDeclaredConstructor(View.class);
                ct.setAccessible(true);
                return (VHolder) ct.newInstance(LayoutInflater.from(context).inflate(resId, parent, false));
            }

            try {
                //如果不是匿名内部类 或者是静态的匿名内部类
                Constructor ct = clazz.getDeclaredConstructor(ViewGroup.class);
                ct.setAccessible(true);
                return (SimpleAdapter.VHolder) ct.newInstance(parent);
            } catch (NoSuchMethodException e) {
                List<Class> clss = new ArrayList<>();
                Class enclosingClass = clazz.getEnclosingClass();
                clss.add(enclosingClass);
                while (enclosingClass.getEnclosingClass() != null) {
                    enclosingClass = enclosingClass.getEnclosingClass();
                    clss.add(enclosingClass);
                }

                String enclosingClassName = enclosingClass.getName();
                Object result = obj != null ? obj : getAllActivitys().get(enclosingClassName);
                if (result == null) {
                    if (enclosingClass.newInstance() instanceof Fragment) {
                        throw new Exception("SimpleAdapter 初始化异常，obj不能为null!");
                    }
                    result = enclosingClass.newInstance();
                }
                for (int i = clss.size() - 2; i >= 0; i--) {
                    Constructor ct = clss.get(i).getDeclaredConstructor(result.getClass());
                    ct.setAccessible(true);
                    result = ct.newInstance(result);
                }
                Constructor ct = clazz.getDeclaredConstructor(result.getClass(), ViewGroup.class);
                ct.setAccessible(true);
                return (SimpleAdapter.VHolder) ct.newInstance(result, parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.setData(data);
        holder.onBindViewHolder(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List data) {
        this.data.clear();
        if (data != null)
            addData(data);
    }

    public void addData(List data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List getData() {
        return data;
    }

    public abstract static class VHolder<T> extends RecyclerView.ViewHolder {
        private List data = new ArrayList<>();

        public VHolder(@NonNull ViewGroup parent) {
            super(parent);
        }

        private VHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(List data) {
            this.data = data;
        }

        public int getItemCount() {
            return data.size();
        }

        public abstract void onBindViewHolder(T item, int position);
    }

    public static class DataBindingHolder<T> extends SimpleAdapter.VHolder<T> {
        private List data = new ArrayList<>();

        private ViewDataBinding mBinding;

        public DataBindingHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setData(List data) {
            this.data = data;
        }

        public int getItemCount() {
            return data.size();
        }

        @Override
        public void onBindViewHolder(T item, int position) {
            mBinding.setVariable(BR.itemData, item);
            mBinding.setVariable(BR.position, position);
        }
    }

    public Map<String, Activity> getAllActivitys() {
        Map<String, Activity> map = new HashMap<>();
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            //获取主线程对象
            Object activityThreadObject = currentActivityThread.invoke(null);
            Field mActivitiesField = activityThread.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Map<Object, Object> mActivities = (Map<Object, Object>) mActivitiesField.get(activityThreadObject);
            for (Map.Entry<Object, Object> entry : mActivities.entrySet()) {
                Object value = entry.getValue();
                Class<?> activityClientRecordClass = value.getClass();
                Field activityField = activityClientRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Object o = activityField.get(value);
                map.put(o.getClass().getName(), (Activity) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
