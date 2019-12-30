package com.gt.utils;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程下载，支持断点续传
 *
 * @author gt.
 */
public class MuchThreadDown {

    /**
     * 下载路径
     */
    private String fileUrl = "";
    /**
     * 下载文件存放目录
     */
    private String targetFilePath = "/";
    /**
     * 下载后的文件路径
     */
    private String filePath;
    /**
     * 线程数量
     */
    private int threadCount;
    /**
     * 回调接口
     */
    private OnDownloadListener downloadListener;
    /**
     * 记录完成下载的线程数
     */
    private int completeNum;
    /**
     * 开始下载时的时间
     */
    private long startTime;
    /**
     * 线程池
     */
    private static ExecutorService SCHEDULEDTHREADPOOL;
    /**
     * 文件的总长度
     */
    private int fileTotalLength;
    /**
     * 已下载的总长度
     */
    private int completedTotalLength;

    private boolean showLog = false, isAlone = false;

    public MuchThreadDown(String fileUrl) {
        this(fileUrl, Environment.getExternalStorageDirectory() + File.separator + "kms" + File.separator + "image");
    }

    /**
     * 构造方法
     *
     * @param fileUrl        要下载文件的网络路径
     * @param targetFilePath 保存下载文件的目录
     */
    public MuchThreadDown(String fileUrl, String targetFilePath) {
        this.fileUrl = fileUrl;
        this.targetFilePath = targetFilePath;
        completeNum = 0;
        if (SCHEDULEDTHREADPOOL == null) {
            SCHEDULEDTHREADPOOL = Executors.newScheduledThreadPool(15);
        }
    }

    public MuchThreadDown isAlone(boolean isAlone) {
        this.isAlone = isAlone;
        return this;
    }

    public MuchThreadDown isShowLog(boolean showLog) {
        this.showLog = showLog;
        return this;
    }

    /**
     * 下载文件
     *
     * @param listener 下载回调监听
     */
    public void download(OnDownloadListener listener) {
        this.downloadListener = listener;
        startTime = System.currentTimeMillis();
        SCHEDULEDTHREADPOOL.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接资源
                    URL url = new URL(fileUrl);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(100000);
                    connection.setReadTimeout(100000);

                    int code = connection.getResponseCode();
                    if (code == 200) {
                        //获取资源大小
                        fileTotalLength = connection.getContentLength();
                        connection.disconnect();
                        File path = new File(targetFilePath);
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                        File file = new File(targetFilePath, getFileName(url));
                        if (file.exists()) {
                            //如果这个文件存在，不在重复下载。
                            if (downloadListener != null) {
                                downloadListener.onDownloadComplete(getFileName(url), fileUrl, file.getPath());
                                return;
                            }
                        }
                        filePath = file.getPath();
                        /*
                         * 将下载任务分配给每个线程
                         */
                        int blockSize = 1024 * 10000;//每个线程下载的数量.最多10M;
                        if (isAlone || fileTotalLength <= blockSize) {
                            threadCount = 1;
                        } else {
                            threadCount = fileTotalLength % blockSize == 0 ? fileTotalLength / blockSize : fileTotalLength / blockSize + 1;
                        }
                        //加载下载位置的文件
                        File downThreadFile = new File(targetFilePath, "downThread_" + getFileName(url).split("\\.")[0] + "_.dt");
                        RandomAccessFile downThreadStream = new RandomAccessFile(downThreadFile, "rwd");
                        String tempStr = downThreadStream.readLine();
                        downThreadStream.close();
                        String[] tempList = tempStr == null ? null : tempStr.split("\\*");
                        for (int threadId = 0; threadId < threadCount; threadId++) {//为每个线程分配任务
                            int startIndex = threadId * blockSize; //线程开始下载的位置
                            int endIndex = (threadId + 1) * blockSize; //线程结束下载的位置
                            if (threadId == (threadCount - 1)) {  //如果是最后一个线程,将剩下的文件全部交给这个线程完成
                                endIndex = fileTotalLength;
                            }

                            int completed = 0;
                            if (tempList != null && tempList.length == threadCount) {
                                completed = Integer.parseInt(tempList[threadId].split("&")[2]);
                            } else {
                                String tempStrItem = startIndex + "&" + endIndex + "&" + completed + (threadId == threadCount - 1 ? "" : "*");
                                RandomAccessFile stream = new RandomAccessFile(downThreadFile, "rwd");
                                stream.seek(threadId * (fileTotalLength + "").length() * 4);
                                stream.write(tempStrItem.getBytes("UTF-8"));
                                stream.close();
                            }

                            completedTotalLength += completed;
//                            if (endIndex - startIndex > completed) // 已下载完的就不用再开线程下载了
                            SCHEDULEDTHREADPOOL.execute(new DownLoadRunnable(threadId, startIndex, endIndex, completed));
                        }
                        Log("======" + getFileName(url) + "======" + "总共已完成 " + completedTotalLength + "  总长度: " + fileTotalLength);
                    } else {
                        if (downloadListener != null) {
                            downloadListener.onDownloadError(fileUrl, new Exception(code + ""));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (downloadListener != null) {
                        downloadListener.onDownloadError(fileUrl, e);
                    }
                }
            }
        });
    }

    private class DownLoadRunnable implements Runnable {

        private int threadId;
        private int startIndex;
        private int endIndex;
        private int completed;

        public DownLoadRunnable(int threadId, int startIndex, int endIndex, int completed) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.completed = completed;
        }

        @Override
        public void run() {
            try {
                //分段请求网络连接,分段将文件保存到本地.
                URL url = new URL(MuchThreadDown.this.fileUrl);
                Log("======" + getFileName(url) + "======" + "线程" + threadId + "开始下载");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(100000);

                //设置分段下载的头信息。  Range:做分段数据请求用的。格式: Range bytes=0-1024  或者 bytes:0-1024
                connection.setRequestProperty("Range", "bytes=" + (startIndex + completed) + "-" + endIndex);

                Log("======" + getFileName(url) + "======" + "线程_" + threadId + "的下载起点是 " + startIndex + "  下载终点是: " + endIndex + " 已完成：" + completed);

                if (connection.getResponseCode() == 206) {//200：请求全部资源成功， 206代表部分资源请求成功
                    InputStream inputStream = connection.getInputStream();//获取流
                    RandomAccessFile randomAccessFile = new RandomAccessFile(
                            new File(filePath + ".bak"), "rw");//获取前面已创建的文件.
                    randomAccessFile.seek(startIndex + completed);//文件写入的开始位置.

                    //加载下载位置的文件
                    File downThreadFile = new File(targetFilePath, "downThread_" + getFileName(url).split("\\.")[0] + "_.dt");
                    RandomAccessFile downThreadStream = new RandomAccessFile(downThreadFile, "rwd");
                    /*
                     * 将网络流中的文件写入本地
                     */
                    byte[] buffer = new byte[5 * 1024];
                    int length = -1;
                    while ((length = inputStream.read(buffer)) != -1) {
                        randomAccessFile.write(buffer, 0, length);
                        completed += length;
                        /*
                         * 将当前现在到的位置保存到文件中
                         */
                        int seek = (threadId * (fileTotalLength + "").length() * 4) + (startIndex + "&" + endIndex + "&").length();
                        downThreadStream.seek(seek);
                        downThreadStream.write((completed + (threadId == threadCount - 1 ? "" : "*")).getBytes("UTF-8"));
                        completedTotalLength += length;
                        if (downloadListener != null) {
                            downloadListener.onDownloads("《threadId:" + threadId + "》" + fileUrl, completedTotalLength, fileTotalLength);
                        }
                    }

                    inputStream.close();
                    randomAccessFile.close();
                    downThreadStream.close();
                    connection.disconnect();
                    Log("======" + getFileName(url) + "======" + "线程" + threadId + "下载完毕");
                    completeNum++;
//                    if (completedTotalLength >= fileTotalLength) {
                    if (completeNum == threadCount) {
                        if (downloadListener != null) {
                            downloadListener.onDownloadComplete(getFileName(url), fileUrl, filePath);
                            Log("======" + getFileName(url) + "======" + "耗时" + format(System.currentTimeMillis() - startTime));
                            new File(filePath + ".bak").renameTo(new File(filePath)); // 重命名

                            cleanTemp(downThreadFile);//删除临时文件
                        }
                    }
                } else {
                    completeNum++;
                    Log("======" + getFileName(url) + "======" + "线程" + threadId + "响应码是" + connection.getResponseCode() + ". 服务器不支持多线程下载");
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (downloadListener != null) {
                    downloadListener.onDownloadError("线程" + threadId + "==" + fileUrl, e);
                }
            }
        }
    }

    private void Log(String msg) {
        if (showLog)
            System.out.println(msg);
    }

    // 将毫秒数格式化
    private String format(long elapsed) {
        int hour, minute, second;
        elapsed = elapsed / 1000;
        second = (int) (elapsed % 60);
        elapsed = elapsed / 60;
        minute = (int) (elapsed % 60);
        elapsed = elapsed / 60;
        hour = (int) (elapsed % 60);
        if (hour == 0) {
            return String.format("%02d:%02d", minute, second);
        } else {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
    }

    //删除线程产生的临时文件
    private synchronized void cleanTemp(File file) {
        file.delete();
    }

    //获取下载文件的名称
    private String getFileName(URL url) {
        String filename = url.getFile();
        String name = filename.substring(filename.lastIndexOf("/") + 1);
        if (name.equals("0")) {
            name = filename.substring(filename.indexOf("/") + 1).split("\\.")[0] + ".jpg";
        }
        return name;
    }

    /**
     * 接着结束后回调
     */
    public abstract static class OnDownloadListener {
        /**
         * 下载成功后回调
         *
         * @param name     文件名
         * @param url      网络地址
         * @param filePath 本地地址
         */
        protected abstract void onDownloadComplete(String name, String url, String filePath);

        /**
         * 下载失败回调
         *
         * @param url 网络地址
         * @param e   异常
         */
        protected void onDownloadError(String url, Exception e) {
            Log.e("onDownloadError", url + "下载失败");
//            Log("onDownloadError:" + url + "下载失败");
            e.printStackTrace();
        }

        /**
         * 下载过程中回调
         *
         * @param url       网络地址
         * @param completed 已完成
         * @param endIndex  总数
         */
        protected void onDownloads(String url, int completed, int endIndex) {
            Log.i("onDownloads", url + ":（" + completed + "||" + endIndex + "）");
//            Log("onDownloads:" + url + ":（" + completed + "||" + endIndex + "）");
        }
    }

    public static void main(String[] args) {
        try {
            new MuchThreadDown("http://img1.kuaimashi.com/69_1526640731373.mp4", "D:/video/").isShowLog(true).download(new OnDownloadListener() {
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
                    System.out.println("下载中==" + "url:" + url + ":（" + completed + "||" + endIndex + "）" + "======（" + ((float) completed / (float) endIndex) * 100 + "）");
                }
            });
            /*new MuchThreadDown("http://img1.kuaimashi.com/69_1526640731373.mp4", "D:/video/").download(new OnDownloadListener() {
                @Override
                protected void onDownloadComplete(String name, String url, String filePath) {
                    System.out.println("下载成功==" + "url:" + url);
                }
            });
            new MuchThreadDown("http://img1.kuaimashi.com/69_1526640991160.mp4", "D:/video/").download(new OnDownloadListener() {
                @Override
                protected void onDownloadComplete(String name, String url, String filePath) {
                    System.out.println("下载成功==" + "url:" + url);
                }
            });
            new MuchThreadDown("http://img1.kuaimashi.com/69_1526641311541.mp4", "D:/video/").download(new OnDownloadListener() {
                @Override
                protected void onDownloadComplete(String name, String url, String filePath) {
                    System.out.println("下载成功==" + "url:" + url);
                }
            });
            new MuchThreadDown("http://vrvideo.kuaimashi.com/xinghuwan1.mp4", "D:/video/").download(new OnDownloadListener() {
                @Override
                protected void onDownloadComplete(String name, String url, String filePath) {
                    System.out.println("下载成功==" + "url:" + url);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
