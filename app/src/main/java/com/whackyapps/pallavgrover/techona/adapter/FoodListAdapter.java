package com.whackyapps.pallavgrover.techona.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whackyapps.pallavgrover.techona.R;
import com.whackyapps.pallavgrover.techona.data.RemoteDataSource;
import com.whackyapps.pallavgrover.techona.data.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {


    private List<Food> mFoodList = null;

    public FoodListAdapter() {
        mFoodList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Food food = mFoodList.get(position);
        holder.getTitle().setText(food.getTitle());
        holder.getPrice().setText("$" + food.getPrice());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.ic_launcher);
        requestOptions.placeholder(R.drawable.ic_launcher);

        Glide.with(holder.getFoodImage().getContext()).applyDefaultRequestOptions(requestOptions)
                .load(food.getImage())
                .into(holder.getFoodImage());
    }

    @Override
    public int getItemCount() {
        return mFoodList == null ? 0 : mFoodList.size();
    }

    public void setmFoodList(List<Food> foodList) {
        for (Food food : foodList) {
            if (!mFoodList.contains(food)) {
                mFoodList.add(food);
            }
        }
        notifyDataSetChanged();
    }

    public void updateList(List<Food> list) {
        this.mFoodList = list;
        notifyDataSetChanged();
    }

    public void clearFoodList() {
        mFoodList.clear();
        notifyDataSetChanged();
    }

    public List<Food> getFoodList(){
        return mFoodList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mRoot;

        private TextView mTitle;

        private TextView mPrice;

        private ImageView mThumbnail;

        ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            mPrice = (TextView) view.findViewById(R.id.price);
            mThumbnail = (ImageView) view.findViewById(R.id.food_image);
        }

        private TextView getTitle() {
            return mTitle;
        }

        private TextView getPrice() {
            return mPrice;
        }

        private ImageView getFoodImage() {
            return mThumbnail;
        }
    }

}
