package com.whackyapps.pallavgrover.techona.data;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.whackyapps.pallavgrover.techona.data.model.Food;
import com.whackyapps.pallavgrover.techona.data.retrofit.ApiClient;
import com.whackyapps.pallavgrover.techona.data.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE = null;

    private final MutableLiveData<Boolean> mIsLoadingFood;


    private final MutableLiveData<List<Food>> mFoodList;

    private ApiInterface apiService;



    {
        mIsLoadingFood = new MutableLiveData<>();
        mFoodList = new MutableLiveData<>();

    }

    private RemoteDataSource() {
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public MutableLiveData<Boolean> isLoadingFoodList() {
        return mIsLoadingFood;
    }



    @Override
    public LiveData<List<Food>> getFoodList(int input) {
        mIsLoadingFood.setValue(true);
        apiService.getFoodList(input).enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                mIsLoadingFood.setValue(false);
                if (response.isSuccessful()) {
                    mFoodList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                mIsLoadingFood.setValue(false);

            }
        });
        return mFoodList;
    }
}
