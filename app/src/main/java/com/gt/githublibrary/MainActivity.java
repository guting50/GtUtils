package com.gt.githublibrary;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.gt.utils.CacheUtils;
import com.gt.utils.PermissionUtils;
import com.gt.utils.floatingeditor.DefaultEditorHolder;
import com.gt.utils.floatingeditor.EditorCallback;
import com.gt.utils.floatingeditor.FloatEditorActivity;
import com.gt.utils.floatingeditor.InputCheckRule;
import com.gt.utils.http.RetrofitHelper;
import com.gt.utils.ormlite.DbHelper;
import com.gt.utils.widget.*;
import com.gt.utils.widget.multigridview.MultiGridView;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    FlowLayout fl_flow;
    CircleButtonView circleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        MultiGridView multiGridView = findViewById(R.id.multiGridView);
        List<String> datas = new ArrayList<>();
        datas.add("https://vrimg.kuaimashi.com/qiniu_1597807617179.png");
        multiGridView.setFilenamesData(datas);
        fl_flow = findViewById(R.id.fl_flow);
        for (int i = 0; i < 50; i++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(250, 100));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(0xff123456);
            tv.setTextColor(Color.WHITE);
            tv.setText("测试" + i);
            fl_flow.addView(tv);
        }
        ImagePagerActivity.watermark = "?watermark/1/image/aHR0cHM6Ly92cmltZy5rdWFpbWFzaGkuY29tL3Fpbml1XzE2MDA1MDg3Njc3NjkucG5n/dissolve/100/gravity/Center/ws/0/wst/0";
        DbHelper.updateDB(27);
        CacheUtils.put("aaa", "bbb");
        Log.e("aaa", CacheUtils.getVal("aaa"));
        circleButton = findViewById(R.id.circle_button);
        circleButton.setOnClickListener(() -> Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_LONG).show());
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

        findViewById(R.id.tv_3).setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                startActivity(new Intent(MainActivity.this, TestBgLayoutActivity.class));
            }
        });

        findViewById(R.id.textView).setOnClickListener(v -> {
            String pattern = "yyyy-MM-dd HH:mm";
            TextView tv = findViewById(R.id.textView);
            Date date = new Date();
            try {
                if (!TextUtils.isEmpty(tv.getText().toString()))
                    date = new SimpleDateFormat(pattern).parse(tv.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DateSelectDialog(this).setOnSelectedListener(data -> {
                tv.setText(data);
            }).setDefaultDate(date).setDatePattern(pattern).show();
        });
    }

    public void bnOnRpClick(View view) {
        new PermissionUtils.Builder(this)
                .permission(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                .onGranted(() -> {
                    Toast.makeText(this, "允许", Toast.LENGTH_LONG).show();
                })
                .onDenied(requestPermissions -> {
                    Toast.makeText(this, requestPermissions + " : 拒绝", Toast.LENGTH_LONG).show();
                })
                .start();
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