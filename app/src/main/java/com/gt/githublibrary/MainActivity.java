package com.gt.githublibrary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.utils.PermissionUtils;
import com.gt.utils.floatingeditor.DefaultEditorHolder;
import com.gt.utils.floatingeditor.EditorCallback;
import com.gt.utils.floatingeditor.FloatEditorActivity;
import com.gt.utils.floatingeditor.InputCheckRule;
import com.gt.utils.http.RetrofitHelper;
import com.gt.utils.view.CircleButtonView;
import com.gt.utils.view.FlowLayout;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    FlowLayout fl_flow;
    CircleButtonView circleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        fl_flow = findViewById(R.id.fl_flow);
        for (int i = 0; i < 10; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(250, 100));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(0xff123456);
            tv.setTextColor(Color.WHITE);
            tv.setText("测试" + i);
            fl_flow.addView(tv);
        }
        circleButton = findViewById(R.id.circle_button);
        circleButton.setOnClickListener(new CircleButtonView.OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_LONG).show();
            }
        });
        circleButton.setOnLongClickListener(new CircleButtonView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                Toast.makeText(MainActivity.this, "onLongClick", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNoMinRecord(int currentTime) {
                Toast.makeText(MainActivity.this, "onNoMinRecord", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRecordFinished() {
                Toast.makeText(MainActivity.this, "onRecordFinished", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void bnOnRpClick(View view) {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int... requestCode) {
                Toast.makeText(MainActivity.this, "CODE_CAMERA : 允许", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefuseGranted() {
                Toast.makeText(MainActivity.this, "CODE_CAMERA : 拒绝", Toast.LENGTH_LONG).show();
            }
        });

        PermissionUtils.requestPermission(this, PermissionUtils.CODE_RECORD_AUDIO, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int... requestCode) {
                Toast.makeText(MainActivity.this, "CODE_RECORD_AUDIO : 允许", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefuseGranted() {
                Toast.makeText(MainActivity.this, "CODE_RECORD_AUDIO : 拒绝", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void bnOnHttpRClick(View view) {
        RetrofitHelper.init(this, ApiService.ApiUrl.BASE_URL);
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "18672307670");
        params.put("password", "123456");
        RetrofitHelper.execute(
                RetrofitHelper.create(ApiService.class).userLogin(params),
                new DefaultObserver() {
                    @Override
                    public void onSuccess(Object response) {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void bnOnSeClick(View view) {
        FloatEditorActivity.openDefaultEditor(MainActivity.this, new EditorCallback() {
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSubmit(String content) {
                Toast.makeText(MainActivity.this, "submit:" + content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAttached(final ViewGroup rootView) {
                TextView title = (TextView) rootView.findViewById(DefaultEditorHolder.DEFAULT_TITLE);
                title.setText("哈哈");
                EditText editText = (EditText) rootView.findViewById(DefaultEditorHolder.DEFAULT_ID_WRITE);
                editText.setHint("输入中...");
            }
        }, new InputCheckRule(20, 1), 0f, true);
    }
}