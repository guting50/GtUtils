package com.gt.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionActivity extends AppCompatActivity {

    public static Map<Long, PermissionUtils.PermissionGrant> permissionGrants = new HashMap<>();
    public PermissionUtils.PermissionGrant permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        permission = new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(String... requestPermissions) {
                PermissionUtils.PermissionGrant permissionGrant = permissionGrants.get(getIntent().getLongExtra("key", -1));
                if (permissionGrant != null) {
                    permissionGrant.onPermissionGranted(requestPermissions);
                    permissionGrants.remove(permissionGrant);
                }
                finish();
            }

            public void onRefuseGranted() {
                PermissionUtils.PermissionGrant permissionGrant = permissionGrants.get(getIntent().getLongExtra("key", -1));
                if (permissionGrant != null) {
                    permissionGrant.onRefuseGranted();
                    permissionGrants.remove(permissionGrant);
                }
                finish();
            }
        };
        requestPermission();
    }

    private void requestPermission() {
        PermissionUtils.checkOrRequestPermissions(PermissionActivity.this, getIntent().getStringExtra("requestPermission"), permission);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, permission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtils.REQUEST_CODE_OPEN_SETTING) {
            requestPermission();
        }
    }
}
