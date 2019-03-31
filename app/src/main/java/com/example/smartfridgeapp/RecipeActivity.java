package com.example.smartfridgeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class RecipeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        //Toast.makeText(this, "" + MainActivity.mFoodItems.size(),Toast.LENGTH_SHORT).show();
        StringBuilder s = new StringBuilder();
        for(ListFoodItemsQuery.Item i : MainActivity.mFoodItems){
            s.append(i.name()+ "%2C");
        }
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService service = retrofit.create(RecipeService.class);
        Call<List<Recipe>> call = service.getListRecipes(s.toString());
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe >> response) {
                //Toast.makeText(RecipeActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                generateRecipeList(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                Toast.makeText(RecipeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateRecipeList(List<Recipe> recipeList) {
        recyclerView = findViewById(R.id.recipe_recycler_view);
        adapter = new RecipeAdapter(this);
        adapter.setItems(recipeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
interface RecipeService {
    @Headers({
            "X-RapidAPI-Key: 11f118d368mshd1e6592bdae368ep17e8c6jsn465913d15c49"
    })
    @GET("recipes/findByIngredients?number=10&ranking=1&ignorePantry=false")
    Call<List<Recipe>> getListRecipes(@Query("ingredients") String ingredients);
}

