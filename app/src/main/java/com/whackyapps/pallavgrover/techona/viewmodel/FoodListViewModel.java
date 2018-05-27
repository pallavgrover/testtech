package com.whackyapps.pallavgrover.techona.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
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

    private FoodListViewModel(Application application, RemoteDataSource girlsDataRepository) {
        super(application);
        mFoodDataRespository = girlsDataRepository;
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

    public void loadNextPageGirls() {
//        if (!Util.isNetworkConnected(MyApplication.getInstance())) {
//            return;
//        }
        mFoodPageIndex.setValue((mFoodPageIndex.getValue() == null ? 1 : mFoodPageIndex.getValue() + 1));
    }

    public LiveData<Boolean> getLoadMoreState() {
        return mFoodDataRespository.isLoadingFoodList();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final RemoteDataSource mFoodDataRespository;

        public Factory(@NonNull Application application, RemoteDataSource mFoodDataRespository) {
            mApplication = application;
            this.mFoodDataRespository = mFoodDataRespository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new FoodListViewModel(mApplication, mFoodDataRespository);
        }
    }
}
