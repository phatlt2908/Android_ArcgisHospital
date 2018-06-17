package com.phatlt.suportmedicalacrgisapi.retrofit;

import com.google.gson.JsonArray;

import models.Feedback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DataClient {

    @POST("feedback")
    Call<Object> sendFeedback(@Body Feedback feedback);
}
