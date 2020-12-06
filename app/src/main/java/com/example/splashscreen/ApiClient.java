package com.example.splashscreen;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://newsapi.org/v2/";
    private static ApiClient sApiClient;
    private static Retrofit sRetrofit;

    private ApiClient() {
        sRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized ApiClient getInstance() {
        if (sApiClient == null) {
            sApiClient = new ApiClient();
        }
        return sApiClient;
    }

    public ApiInterface getApi() {
        return sRetrofit.create(ApiInterface.class);
    }
}
