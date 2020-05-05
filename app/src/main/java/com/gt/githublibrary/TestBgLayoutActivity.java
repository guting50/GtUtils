package com.gt.githublibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestBgLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_test);
        findViewById(R.id.bgLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "登录成功", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLogin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLogin1 登录成功", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLogin2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLogin2 登录成功", Toast.LENGTH_LONG).show();
            }
        });
    }
}
