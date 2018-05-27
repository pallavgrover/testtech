package com.whackyapps.pallavgrover.techona.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.whackyapps.pallavgrover.techona.MyApplication;
import com.whackyapps.pallavgrover.techona.data.RemoteDataSource;
import com.whackyapps.pallavgrover.techona.data.model.Food;
import com.whackyapps.pallavgrover.techona.util.Util;

import java.util.List;


public class FoodListViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> mFoodPageIndex = new MutableLiveData<>();

    private final LiveData<List<Food>> mFood;

    private RemoteDataSource mFoodDataRespository = null;
    private Context mContext;

    private FoodListViewModel(Application application, RemoteDataSource girlsDataRepository, Context context) {
        super(application);
        mFoodDataRespository = girlsDataRepository;
        mContext=context;
        mFood = Transformations.switchMap(mFoodPageIndex, new Function<Integer, LiveData<List<Food>>>() {
            @Override
            public LiveData<List<Food>> apply(Integer input) {
                return mFoodDataRespository.getFoodList(input);
            }
        });
    }

    public LiveData<List<Food>> getmFood() {
        return mFood;
    }

    public void refreshFoodData() {
        mFoodPageIndex.setValue(1);
    }

    public void loadNextPageFood() {
        if (!Util.isNetworkConnected(mContext)) {
            return;
        }
        mFoodPageIndex.postValue((mFoodPageIndex.getValue() == null ? 1 : mFoodPageIndex.getValue() + 1));

    }

    public LiveData<Boolean> getLoadMoreState() {
        return mFoodDataRespository.isLoadingFoodList();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final RemoteDataSource mFoodDataRespository;

        private final Context mContext;

        public Factory(@NonNull Application application, RemoteDataSource mFoodDataRespository,Context context) {
            mApplication = application;
            this.mFoodDataRespository = mFoodDataRespository;
            mContext = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new FoodListViewModel(mApplication, mFoodDataRespository,mContext);
        }
    }
}
