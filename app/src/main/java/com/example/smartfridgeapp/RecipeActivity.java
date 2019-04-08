package com.example.smartfridgeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;
import com.google.gson.Gson;

import java.util.HashSet;
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
    public static HashSet<String> currentSet;
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
        currentSet = new HashSet<>();
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
             //   Log.w("2.0 getFeed > Full json res wrapped in gson => ",new Gson().toJson(call));
            }
        });
    }
    private void generateRecipeList(final List<Recipe> recipeList) {
        recyclerView = findViewById(R.id.recipe_recycler_view);
        adapter = new RecipeAdapter(this);
        adapter.setItems(recipeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
                        i.putExtra("id", recipeList.get(position).getId());
                        List<Ingredient> in = recipeList.get(position).getMissedIngredients();
                        currentSet.clear();
                        for(Ingredient ing : in){
                            Log.d("add", ing.getName());
                            currentSet.add(ing.getName());
                        }
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
interface RecipeService {
    @Headers({
            "X-RapidAPI-Key: 11f118d368mshd1e6592bdae368ep17e8c6jsn465913d15c49"
    })
    @GET("recipes/findByIngredients?number=10&ranking=1&ignorePantry=false")
    Call<List<Recipe>> getListRecipes(@Query("ingredients") String ingredients);
}

