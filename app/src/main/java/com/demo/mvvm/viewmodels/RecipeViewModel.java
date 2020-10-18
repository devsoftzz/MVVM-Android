package com.demo.mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    public RecipeViewModel() {
        this.mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void RecipeApi(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.RecipeApi(recipeId);
    }

    public String getRecipeId(){
        return mRecipeId;
    }
}
