package com.whackyapps.pallavgrover.techona.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.whackyapps.pallavgrover.techona.MyApplication;
import com.whackyapps.pallavgrover.techona.R;
import com.whackyapps.pallavgrover.techona.adapter.FoodListAdapter;
import com.whackyapps.pallavgrover.techona.data.RemoteDataSource;
import com.whackyapps.pallavgrover.techona.data.model.Food;
import com.whackyapps.pallavgrover.techona.viewmodel.FoodListViewModel;

import java.util.List;

public class FirstFragment extends android.support.v4.app.Fragment{

    public static final String TAG = "FirstFragment";
    private FoodListViewModel mListViewModel;
    private FoodListAdapter mFoodAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private View RLGirlRoot;
    private ProgressBar mLoadMorebar;


    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
         mFoodAdapter = new FoodListAdapter();
        recyclerView.setAdapter(mFoodAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == mFoodAdapter.getItemCount() - 1) {
                    mListViewModel.loadNextPageGirls();
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFoodAdapter.clearFoodList();
                mRefreshLayout.setRefreshing(true);
                mListViewModel.refreshFoodData();
            }
        });
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mLoadMorebar = (ProgressBar)view.findViewById(R.id.load_more_bar);
        RLGirlRoot = view.findViewById(R.id.rl_girl_root);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeUI();
    }

    private void subscribeUI() {
        // 通过 ViewModelProviders 创建对应的 ZhihuListViewModel 对象
        FoodListViewModel.Factory factory = new FoodListViewModel
                .Factory(MyApplication.getInstance()
                , RemoteDataSource.getInstance());
        mListViewModel = ViewModelProviders.of(FirstFragment.this, factory).get(FoodListViewModel.class);
        mListViewModel.getmFood().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> stories) {
                if (stories == null || stories.size() <= 0) {
                    return;
                }
                mFoodAdapter.setmFoodList(stories);
            }
        });
        mListViewModel.getLoadMoreState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null) {
                    return;
                }
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                } else {
                    mLoadMorebar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
                }
            }
        });
        mListViewModel.refreshFoodData();
    }
}
