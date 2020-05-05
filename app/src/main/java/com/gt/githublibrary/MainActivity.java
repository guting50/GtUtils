package com.gt.githublibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.gt.utils.widget.CircleButtonView;
import com.gt.utils.widget.FlowLayout;

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
        for (int i = 0; i < 50; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_table_layout, null);
            if (i % 2 == 0) {
                view.findViewById(R.id.bgFrameLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("setOnClickListener" + "=======================");
                    }
                });
            }
            fl_flow.addView(view);
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


        findViewById(R.id.et_1).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("et_1===========" + hasFocus);
            }
        });
        findViewById(R.id.et_2).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("et_2===========" + hasFocus);
            }
        });
        findViewById(R.id.tv_3).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("tv_3===========" + hasFocus);
            }
        });
        findViewById(R.id.tv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("tv_3  TouchMode===========" + v.isFocusableInTouchMode());
            }
        });
    }

    public void bnOnRpClick(View view) {
        PermissionUtils.requestPermission(this, PermissionUtils.CAMERA, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int... requestCode) {
                Toast.makeText(MainActivity.this, "CODE_CAMERA : 允许", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefuseGranted() {
                Toast.makeText(MainActivity.this, "CODE_CAMERA : 拒绝", Toast.LENGTH_LONG).show();
            }
        });

        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_INSTALL_PACKAGES, new PermissionUtils.PermissionGrant() {
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
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "18672307670");
        params.put("password", "123456");
        RetrofitHelper.execute(
                RetrofitHelper.create(this, ApiService.ApiUrl.BASE_URL, ApiService.class).userLogin(params),
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