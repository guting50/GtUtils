package com.gt.utils.http;

import android.util.Log;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GtHttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY,
        BODY_DETAILS
    }

    public interface Logger {
        void log(List<String> messages);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = GtHttpLoggingInterceptor::printLog;
    }

    public GtHttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public GtHttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public GtHttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBodyDetails = level == Level.BODY_DETAILS;
        boolean logBody = logBodyDetails || level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        List<String> logStrs = new ArrayList();
        logStrs.add(requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logStrs.add("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logStrs.add("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logStrs.add(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                logStrs.add("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logStrs.add("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logStrs.add("");
                if (isPlaintext(buffer)) {
                    String result = buffer.readString(charset);
                    if (logBodyDetails) {
                        logStrs.add(result);
                    } else {
                        logStrs.add(result.substring(0, Math.min(result.length(), 1000)));
                    }
                    logStrs.add("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logStrs.add("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logStrs.add("<-- HTTP FAILED: " + e);
            logger.log(logStrs);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logStrs.add("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logStrs.add(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logStrs.add("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logStrs.add("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logStrs.add("");
                    logStrs.add("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    logger.log(logStrs);
                    return response;
                }

                if (contentLength != 0) {
                    logStrs.add("");
                    String result = buffer.clone().readString(charset);
                    if (logBodyDetails) {
                        logStrs.add(result);
                    } else {
                        logStrs.add(result.substring(0, Math.min(result.length(), 1000)));
                    }
                }

                logStrs.add("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        logger.log(logStrs);
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    /**
     * 打印log
     *
     * @param list
     */
    public static synchronized void printLog(List<String> list) {
        int totalLength = 140;//计算每行最长的长度
        int padding = 12;//左右间距
        StringBuilder sb = new StringBuilder();
//        for (String str : list) {
//            if (str.contains("\n")) {//有换行的拆分处理
//                String[] split = str.split("\n");
//                for (String s : split) {
//                    s = s.replace("\t", "    ");//缩进替换空格
//                    if (totalLength < s.length()) {
//                        totalLength = s.length();
//                    }
//                }
//            } else {
//                if (totalLength < str.length()) {
//                    totalLength = str.length();
//                }
//            }
//        }
//        totalLength += padding;//左右间距
        String head = "HTTP REQUEST START";
        sb.append(" \n");
        //打印头部
        String logHead = "┏" + getEmptyStr((totalLength - head.length()) / 2, "━") +
                head + getEmptyStr((totalLength - head.length()) / 2, "━") + "┓";
        sb.append(logHead).append("\n");

        //打印内容
        for (String str : list) {
            if (str.contains("\n")) {//内部换行替换
                String[] strs = str.split("\n");
                for (String str1 : strs) {
                    handleLogStr(sb, str1, padding, totalLength);
                }
            } else {
                handleLogStr(sb, str, padding, totalLength);
            }
        }
        String end = "HTTP REQUEST END";
        //打印结尾
        sb.append("┗").append(getEmptyStr((totalLength - end.length()) / 2, "━"))
                .append(end).append(getEmptyStr((totalLength - end.length()) / 2, "━"))
                .append("┛\n");
        sb.append(" \n\n\n");
        //Logger.DEFAULT.log(sb.toString());//打印log，避免多个log语句，导致log输出时其他线程的log输出切入此输出阵列内
        String[] split = sb.toString().split("\n");
        for (String str : split) {
            Log.e("ღღ___", str);
//            System.out.println(str);
        }
    }

    private static void handleLogStr(StringBuilder sb, String str, int padding, int totalLength) {
        try {
            str = URLDecoder.decode(str, "utf-8");
        } catch (Exception e) {
        }

        List<String> list = interception(str, totalLength - padding);
        for (String s : list) {
            StringBuilder strSb = new StringBuilder(s);
            int count = totalLength - padding / 2 - hasCNchar(strSb);
            sb.append("┃").append(getEmptyStr(padding / 2, " "))
                    .append(strSb).append(getEmptyStr(count, " ")).append("┃\n");
        }
    }

    private static String getEmptyStr(int totalLength, String charToFill) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < totalLength; i++) {
            str.append(charToFill);
        }
        return str.toString();
    }

    static String chinese = "[\u0391-\uFFE5]";

    public static int hasCNchar(StringBuilder str) {
        //3个中文字符和5个英文字符长度相等
        int strCount1 = 0;//中文字符个数
        int strCount2 = 0;//英文字符个数
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                strCount1++;
            } else {
                strCount2++;
            }
        }
        //三个中文字符的宽度=五个英文字符的宽度,因此只有当中文字符为3的倍数时才能对齐
        //所以这里需要添加中文空格来占位
        switch (strCount1 % 3) {
            case 1:
                str.append("\u3000\u3000");
                strCount1 += 2;//添加了两个中文空格
                break;
            case 2:
                str.append("\u3000");
                strCount1 += 1;//添加了一个中文空格
                break;
        }
        //一个中文字符的宽度=5/3个英文的宽度
        return strCount1 * 5 / 3 + strCount2;
    }

    /**
     * 字符串按字节数截取
     *
     * @param string
     * @param n
     */
    public static List<String> interception(String string, int n) {
        String[] str = new String[string.length()];
        for (int i = 0; i < str.length; i++) {
            str[i] = string.substring(i, i + 1);
        }
        List<String> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        float count = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i].matches(chinese)) {
                //如果当前字符是汉子，计数器加2
//                count += 2;
                count += (float) 5 / (float) 3;
            } else {
                //如果当前字符不是是汉子，计数器加1
                count += 1;
            }
            //如果当前计数器的值小于n，则直接输出当前字符
            if (count < n) {
                sb.append(str[i]);
                //最后一行的最后一个
                if (i == str.length - 1) {
                    results.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else if (count == n) {
                sb.append(str[i]);
                //满足n个字节后，就换行
                results.add(sb.toString());
                sb = new StringBuilder();
                count = 0;
            } else {//如果当前计数器count的值大于n，说明有汉子，换行输出，且此时计数器count=2
                results.add(sb.toString());
                sb = new StringBuilder();
                sb.append(str[i]);
//                count = 2;
                count = (float) 5 / (float) 3;
            }
        }
        return results;
    }

}