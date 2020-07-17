package com.gt.githublibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gt.utils.widget.BgLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestBgLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_test);
        findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "text_view", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLayout", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout1).setEnabled(false);
        findViewById(R.id.bgLayout1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLayout1", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout1_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.bgLayout1).setEnabled(!findViewById(R.id.bgLayout1).isEnabled());
                Toast.makeText(TestBgLayoutActivity.this, "bgLayout1_1", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLayout2", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgLayout3", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgLayout4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(TestBgLayoutActivity.this, "bgTextView", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.bgTextView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBgLayoutActivity.this, "bgTextView2", Toast.LENGTH_LONG).show();
            }
        });
    }
}
