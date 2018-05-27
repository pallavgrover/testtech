package com.whackyapps.pallavgrover.techona.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whackyapps.pallavgrover.techona.R;
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
        holder.getPrice().setText("$"+food.getPrice());
        Glide.with(holder.getFoodImage().getContext())
                .load(food.getImage())
                .into(holder.getFoodImage());
    }

    @Override
    public int getItemCount() {
        return mFoodList == null ? 0 : mFoodList.size();
    }

    public void setmFoodList(List<Food> foodList) {
        for (Food food:foodList) {
            if(!mFoodList.contains(food)){
                mFoodList.add(food);
            }
        }
//        mFoodList.addAll(foodList);
        notifyDataSetChanged();
    }

    public void clearFoodList() {
        mFoodList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View mRoot;

        private TextView mTVGirlName;

        private TextView mTVGirlAge;

        private ImageView mIVGirlAvatar;

        ViewHolder(View view) {
            super(view);
            mRoot = view.findViewById(R.id.rl_girl_item_root);
            mTVGirlName = (TextView) view.findViewById(R.id.title);
            mTVGirlAge = (TextView)view.findViewById(R.id.tv_girl_age);
            mIVGirlAvatar = (ImageView) view.findViewById(R.id.food_image);
        }

        private View getRoot() {
            return mRoot;
        }

        private TextView getTitle() {
            return mTVGirlName;
        }

        private TextView getPrice() {
            return mTVGirlAge;
        }

        private ImageView getFoodImage() {
            return mIVGirlAvatar;
        }
    }
}
