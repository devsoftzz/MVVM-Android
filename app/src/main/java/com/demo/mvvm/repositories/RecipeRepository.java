package com.demo.mvvm.repositories;

import androidx.lifecycle.LiveData;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.requests.ApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private ApiClient mApiClient;
    private String mQuery;
    private int mPageNumber;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        mApiClient = ApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mApiClient.getRecipes();
    }
    public LiveData<Recipe> getRecipe() {
        return mApiClient.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
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
