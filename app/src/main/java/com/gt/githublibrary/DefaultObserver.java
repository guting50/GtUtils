package com.gt.githublibrary;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.gt.utils.widget.LoadingDialog;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class DefaultObserver implements Observer<Object> {

    @Override
    public void onNext(Object response) {
        onSuccess(response);
        onFinish();
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(Object response);

    /**
     * 服务器返回数据，但响应码不为200
     */
    public void onFail(String message) {
//        ToastUtils.showLong(message);
        Log.e("=======", message);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("Retrofit", e.getMessage());
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } /*else if (e instanceof ServerResponseException) {
            onFail(e.getMessage());
        } */ else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        onFinish();
    }

    public void onFinish() {
        LoadingDialog.closeDialog();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        String msg = "";
        switch (reason) {
            case CONNECT_ERROR:
                msg = "连接错误";
                break;

            case CONNECT_TIMEOUT:
                msg = "连接超时";
                break;

            case BAD_NETWORK:
                msg = "服务器异常";
                break;

            case PARSE_ERROR:
                msg = "解析数据失败";
                break;

            case UNKNOWN_ERROR:
            default:
                msg = "未知错误";
                break;
        }
        onFail(msg);
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}