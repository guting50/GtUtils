package com.gt.utils.floatingeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gt.utils.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 浮动编辑器，在键盘上面方显示
 *
 * @author kevin
 */

public class FloatEditorActivity extends Activity implements View.OnClickListener {
    public static final String KEY_EDITOR_HOLDER = "editor_holder";
    public static final String KEY_EDITOR_CHECKER = "editor_checker";
    public static final String KEY_DIM_AMOUNT = "dim_amount";
    public static final String KEY_FINISH_ON_TOUCH_OUTSIDE = "finish_on_touch_outside";
    private View cancel;
    private View submit;
    private EditText etContent;
    private static EditorCallback mEditorCallback;
    private EditorHolder holder;
    private InputCheckRule checkRule;
    private boolean isClicked, isFinishOnTouchOutside;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        holder = (EditorHolder) getIntent().getSerializableExtra(KEY_EDITOR_HOLDER);
        checkRule = (InputCheckRule) getIntent().getSerializableExtra(KEY_EDITOR_CHECKER);
        isFinishOnTouchOutside = getIntent().getBooleanExtra(KEY_FINISH_ON_TOUCH_OUTSIDE, false);
        float dimAmount = getIntent().getFloatExtra(KEY_DIM_AMOUNT, 0.5f);
        if (holder == null) {
            throw new RuntimeException("EditorHolder params not found!");
        }
        setContentView(holder.layoutResId);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setDimAmount(dimAmount);
        mEditorCallback.onAttached((ViewGroup) getWindow().getDecorView());

        initView();
        setEvent();
    }

    private void initView() {
        cancel = findViewById(holder.cancelViewId);
        submit = findViewById(holder.submitViewId);
        etContent = (EditText) findViewById(holder.editTextId);

        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
    }

    private void setEvent() {
        if (cancel != null)
            cancel.setOnClickListener(this);

        submit.setOnClickListener(this);
        setFinishOnTouchOutside(isFinishOnTouchOutside);
    }

    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder) {
        openEditor(context, editorCallback, holder, null);
    }

    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule) {
        openEditor(context, editorCallback, holder, checkRule, 0.5f);
    }

    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule, float dimAmount) {
        openEditor(context, editorCallback, holder, checkRule, dimAmount, false);
    }

    /**
     * @param context                上下文
     * @param editorCallback         回调接口
     * @param holder                 编辑框样式
     * @param checkRule              检验规则 可为null
     * @param dimAmount              背景透明度
     * @param isFinishOnTouchOutside 点击空白区域是否关闭编辑框
     */
    public static void openEditor(Context context, EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule, float dimAmount, boolean isFinishOnTouchOutside) {
        Intent intent = new Intent(context, FloatEditorActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(KEY_EDITOR_HOLDER, holder);
        intent.putExtra(KEY_EDITOR_CHECKER, checkRule);
        intent.putExtra(KEY_DIM_AMOUNT, dimAmount);
        intent.putExtra(KEY_FINISH_ON_TOUCH_OUTSIDE, isFinishOnTouchOutside);
        mEditorCallback = editorCallback;
        context.startActivity(intent);
    }

    public static void openDefaultEditor(Context context, EditorCallback editorCallback) {
        openEditor(context, editorCallback, DefaultEditorHolder.createDefaultHolder(), null);
    }

    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule) {
        openEditor(context, editorCallback, DefaultEditorHolder.createDefaultHolder(), checkRule);
    }

    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule, float dimAmount) {
        openEditor(context, editorCallback, DefaultEditorHolder.createDefaultHolder(), checkRule, dimAmount);
    }

    /**
     * @param context                上下文
     * @param editorCallback         回调接口
     * @param checkRule              检验规则 可为null
     * @param dimAmount              背景透明度
     * @param isFinishOnTouchOutside 点击空白区域是否关闭编辑框
     */
    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule, float dimAmount, boolean isFinishOnTouchOutside) {
        openEditor(context, editorCallback, DefaultEditorHolder.createDefaultHolder(), checkRule, dimAmount, isFinishOnTouchOutside);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == holder.cancelViewId) {
            mEditorCallback.onCancel();
        } else if (id == holder.submitViewId) {
            if (checkRule != null && !(checkRule.minLength == 0 && checkRule.maxLength == 0)) {
                if (!illegal()) {
                    isClicked = true;
                    mEditorCallback.onSubmit(etContent.getText().toString());
                    finish();
                }

                return;
            }
            mEditorCallback.onSubmit(etContent.getText().toString());
        }
        isClicked = true;
        finish();
    }

    private boolean illegal() {
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < checkRule.minLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_min_warn, checkRule.minLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (content.length() > checkRule.maxLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_max_warn, checkRule.maxLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!TextUtils.isEmpty(checkRule.regxRule)) {
            Pattern pattern = Pattern.compile(checkRule.regxRule);
            Matcher matcher = pattern.matcher(content);
            if (!matcher.matches()) {
                Toast.makeText(this, getString(checkRule.regxWarn), Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isClicked) {
            mEditorCallback.onCancel();
        }
        mEditorCallback = null;
    }
}
