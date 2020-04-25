package com.gt.githublibrary;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    class ApiUrl {

        //    public static final String BASE_URL = "http://www.byebird.com/dds/user/login";
        public static final String BASE_URL = "http://47.93.116.3:8080/";

        public static final String projectName = "/dds";
    }

    /**
     * 登录
     */
    @POST(ApiUrl.projectName + "/user/login")
    @FormUrlEncoded
    Observable<Object> userLogin(@FieldMap Map<String, Object> maps);

}
