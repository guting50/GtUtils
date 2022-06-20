package com.gt.utils.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitHelper {

    private static final int DEFAULT_TIMEOUT = 10;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static List<Interceptor> mInterceptors;

    public static boolean DEBUG = true;

    public static <T> T create(Context context, String baseUrl, Class<T> clazz) {
        return create(context, baseUrl, null, clazz);
    }

    public static <T> T create(Context context, String baseUrl, List<Interceptor> interceptors, Class<T> clazz) {
        mContext = context;
        mInterceptors = interceptors;
        return getRetrofitBuilder(baseUrl).build().create(clazz);
    }

    public static <T> void execute(Observable<? super T> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        GtHttpLoggingInterceptor loggingInterceptor = new GtHttpLoggingInterceptor();
        if (DEBUG)
            loggingInterceptor.setLevel(GtHttpLoggingInterceptor.Level.BODY);

        File cacheFile = new File(mContext.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(mContext));

        OkHttpClient.Builder builder =
                new OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(loggingInterceptor)
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(new HttpHeaderInterceptor())
//                .addNetworkInterceptor(new HttpCacheInterceptor())
                        // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
//                .sslSocketFactory(createBadSslSocketFactory())
//                .hostnameVerifier(new SafeHostnameVerifier())
                        .addInterceptor(chain -> {
                            Request request = chain.request()
                                    .newBuilder()
                                    .removeHeader("User-Agent")//移除旧的
                                    .addHeader("User-Agent", WebSettings.getDefaultUserAgent(mContext))//添加真正的头部
                                    .build();
                            return chain.proceed(request);
                        })
                        .cache(cache);
        if (mInterceptors != null) {
            for (Interceptor interceptor : mInterceptors)
                builder.addInterceptor(interceptor);
        }
        return builder;
    }

    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            context.init(null, new TrustManager[]{permissive}, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
