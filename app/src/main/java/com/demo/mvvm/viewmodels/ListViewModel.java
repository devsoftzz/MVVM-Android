package com.demo.mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.repositories.RecipeRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public ListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        mRecipeRepository.searchRecipeApi(query, pageNumber);
    }
}
