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
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import com.whackyapps.pallavgrover.techona.MyApplication;
import com.whackyapps.pallavgrover.techona.R;
import com.whackyapps.pallavgrover.techona.adapter.FoodListAdapter;
import com.whackyapps.pallavgrover.techona.data.RemoteDataSource;
import com.whackyapps.pallavgrover.techona.data.model.Food;
import com.whackyapps.pallavgrover.techona.viewmodel.FoodListViewModel;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends android.support.v4.app.Fragment implements Filterable{

    public static final String TAG = "FirstFragment";
    private FoodListViewModel mListViewModel;
    private FoodListAdapter mFoodAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private View root;
    private ProgressBar mLoadMorebar;
    private SearchView mSearchView;
    private FoodFilter foodFilter;


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
        mSearchView = view.findViewById(R.id.search_bar);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mFoodAdapter != null) {
                    getFilter().filter(query);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mFoodAdapter != null) {
                    getFilter().filter(newText);
                    if(TextUtils.isEmpty(newText)){
                        mListViewModel.refreshFoodData();
                    }
                    return true;
                }

                return false;
            }
        });
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
                    mListViewModel.loadNextPageFood();
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

        mLoadMorebar = (ProgressBar) view.findViewById(R.id.load_more_bar);
        root = view.findViewById(R.id.root_frag);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscribeUI();
    }

    private void subscribeUI() {
        FoodListViewModel.Factory factory = new FoodListViewModel
                .Factory(MyApplication.getInstance()
                , RemoteDataSource.getInstance(),getContext());
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

    @Override
    public Filter getFilter() {
        if (foodFilter == null)
            foodFilter = new FoodFilter();
        return foodFilter;
    }

    class FoodFilter extends Filter {
        private List<Food> copyList = new ArrayList<>();

        public FoodFilter() {
            copyList.addAll(mFoodAdapter.getFoodList());
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filterResults.count = copyList.size();
                filterResults.values = copyList;
            } else {
                List<Food> filteredList = new ArrayList<>();
                for (Food food : copyList) {
                    if (food.getTitle().contains(constraint)) {
                        filteredList.add(food);
                    }
                }
                if (filteredList.size() < 3) {
                    mListViewModel.loadNextPageFood();
                    for (Food food : copyList) {
                        if (food.getTitle().contains(constraint)) {
                            filteredList.add(food);
                        }
                    }
                }
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFoodAdapter.updateList((List<Food>) results.values);
        }
    }
}
