package com.phatlt.suportmedicalacrgisapi.retrofit;

public class APIUtils {
    public static final String baseUrl = "https://phatlt-hospital-api.herokuapp.com/";
    public static DataClient getData() {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}
