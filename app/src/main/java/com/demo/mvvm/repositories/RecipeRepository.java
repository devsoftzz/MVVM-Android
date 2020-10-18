package com.demo.mvvm.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.requests.ApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private ApiClient mApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        mApiClient = ApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListApiSource = mApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }else {
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> list){
        if(list != null){
            if(list.size() % 30 != 0){
                mIsQueryExhausted.setValue(true);
            }
        }else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe() {
        return mApiClient.getRecipe();
    }
    public LiveData<Boolean> isRecipeTimeOut() {
        return mApiClient.isRecipeTimeOut();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mApiClient.searchRecipesApi(query, pageNumber);
    }

    public void RecipeApi(String recipeId) {
        mApiClient.RecipeApi(recipeId);
    }

    public void searchNextPage(){
        searchRecipeApi(mQuery, mPageNumber+1);
    }

    public void cancelRequest(){
        mApiClient.cancelRequest();
    }
}
