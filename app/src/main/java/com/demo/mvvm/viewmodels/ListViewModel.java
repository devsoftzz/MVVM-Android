package com.demo.mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.repositories.RecipeRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public ListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipes = false;
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mRecipeRepository.isQueryExhausted();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipeApi(query, pageNumber);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes && !isQueryExhausted().getValue()){
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean mIsViewingRecipes) {
        this.mIsViewingRecipes = mIsViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean onBackPressed() {
        if (mIsPerformingQuery) {
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
