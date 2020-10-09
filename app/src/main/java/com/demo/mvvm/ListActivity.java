package com.demo.mvvm;

import android.os.Bundle;
import android.util.Log;

import com.demo.mvvm.models.Recipe;
import com.demo.mvvm.requests.Api;
import com.demo.mvvm.requests.ServiceGenerator;
import com.demo.mvvm.requests.responses.RecipeSearchResponse;
import com.demo.mvvm.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends BaseActivity {

    private static final String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        testRetrofitRequest();
    }

    private void testRetrofitRequest() {
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
}