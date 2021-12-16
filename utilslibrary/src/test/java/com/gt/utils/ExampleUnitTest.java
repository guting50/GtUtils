package com.gt.utils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);

        new MuchThreadDown("http://img1.kuaimashi.com/69_1526641455014.mp4", "D:/video/")
                .isAlone(true)
                .isShowLog(true)
                .download(new MuchThreadDown.OnDownloadListener() {
                    @Override
                    protected void onDownloadComplete(String name, String url, String filePath) {
                        System.out.println("下载成功==" + "url:" + url);
                    }

                    @Override
                    protected void onDownloadError(String url, Exception e) {
                        System.out.println("下载失败==" + "url:" + url);
                    }

                    @Override
                    protected void onDownloads(String url, int completed, int endIndex) {
                        System.out.println("下载中==" + "url:" + url + ":（" + completed + "||" + endIndex + "）");
                    }
                });
    }
}