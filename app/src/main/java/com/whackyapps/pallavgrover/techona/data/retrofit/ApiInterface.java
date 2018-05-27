package com.whackyapps.pallavgrover.techona.data.retrofit;

import com.whackyapps.pallavgrover.techona.data.model.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET(" ")
    Call<List<Food>> getFoodList(@Query("page") int index);

}
