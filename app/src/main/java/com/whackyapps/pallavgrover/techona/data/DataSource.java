package com.whackyapps.pallavgrover.techona.data;

import android.arch.lifecycle.LiveData;

import com.whackyapps.pallavgrover.techona.data.model.Food;

import java.util.List;


public interface DataSource {


    LiveData<List<Food>> getFoodList(int input);

    LiveData<Boolean> isLoadingFoodList();
}