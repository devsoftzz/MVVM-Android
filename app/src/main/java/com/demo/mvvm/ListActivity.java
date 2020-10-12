package com.demo.mvvm;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.mvvm.adapters.OnRecipeListener;
import com.demo.mvvm.adapters.RecipeRecyclerAdapter;
import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.requests.Api;
import com.demo.mvvm.requests.ServiceGenerator;
import com.demo.mvvm.requests.responses.RecipeResponse;
import com.demo.mvvm.requests.responses.RecipeSearchResponse;
import com.demo.mvvm.util.Constants;
import com.demo.mvvm.util.VerticalSpacingItemDecorator;
import com.demo.mvvm.viewmodels.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "ListActivity";

    private ListViewModel mListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);
        mListViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        initRecyclerView();
        initSearchView();
        subscribeObservers();

        if (!mListViewModel.isViewingRecipes()) {
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //searchRecipeApi("pizza",1);
        //testRetrofitSearchRequest();
        //testRetrofitRequest();
    }

    private void initRecyclerView() {
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!mRecyclerView.canScrollVertically(1)){
                    mListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipeApi(query, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers() {
        mListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null && mListViewModel.isViewingRecipes()) {
                    mAdapter.setRecipes(recipes);
                    mListViewModel.setIsPerformingQuery(false);
                }
            }
        });
    }

    private void searchRecipeApi(String query, int pageNumber) {
        mAdapter.displayLoading();
        mListViewModel.searchRecipeApi(query, pageNumber);
    }

    private void testRetrofitRequest() {

        Api api = ServiceGenerator.getApi();

        Call<RecipeResponse> responseCall = api
                .getRecipe("", "49421");

        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG, "onResponse: " + response.toString());

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: " + recipe.getTitle());
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });

    }

    private void testRetrofitSearchRequest() {
        Api api = ServiceGenerator.getApi();

        Call<RecipeSearchResponse> responseCall = api
                .searchRecipe(Constants.API_KEY, "chicken", "1");

        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "onResponse: " + response.toString());

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for (Recipe r : recipes) {
                        Log.d(TAG, "onResponse: " + r.getTitle());
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipeApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories() {
        mListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if (mListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            displaySearchCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}