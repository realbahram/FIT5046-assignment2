package com.example.goal.retrofit;
import com.example.goal.repository.NinjasQuote;
import com.example.goal.repository.TheySaidSoAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface RetrofitInterface {

    @GET("v1/quotes?category=inspirational")
    Call<ArrayList<TheySaidSoAPI>> quote(@Header("X-Api-Key") String key);

}
