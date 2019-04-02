package com.example.smartfridgeapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class RecipeDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecipeDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        int id = getIntent().getIntExtra("id", 0);
        Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeDetailService service = retrofit.create(RecipeDetailService.class);
        Call<RecipeDetail> call = service.getRecipeDetail(id);
        call.enqueue(new Callback<RecipeDetail>() {
            @Override
            public void onResponse(Call<RecipeDetail> call, Response<RecipeDetail> response) {
                //Toast.makeText(RecipeActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                generateRecipeDetail(response.body());

            }

            @Override
            public void onFailure(Call<RecipeDetail> call, Throwable t) {

                Toast.makeText(RecipeDetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateRecipeDetail(final RecipeDetail rp) {
        ImageView iv = findViewById(R.id.recipeView);
        Picasso.get().load(rp.getImage()).fit().centerCrop().into(iv);
        TextView ttv = findViewById(R.id.recipe_title_txt);
        ttv.setText(rp.getTitle());
        populateChecks(rp);
        recyclerView = findViewById(R.id.ingredient_recycler_view);
        adapter = new RecipeDetailAdapter(this);
        adapter.setItems(rp.getExtendedIngredients());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RecipeDetailActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        TextView rtv = findViewById(R.id.recipe_txt);
        rtv.setText(rp.getInstructions());
    }
    private void populateChecks(final RecipeDetail rp){

        ImageView iv = findViewById(R.id.glut_img); //gluten free
        if(rp.isGlutenFree()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv);
        }
        ImageView iv2 = findViewById(R.id.vegan_img); //gluten free
        if(rp.isVegan()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv2);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv2);
        }
        ImageView iv3 = findViewById(R.id.vegt_img); //gluten free
        if(rp.isVegetarian()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv3);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv3);
        }
        ImageView iv4 = findViewById(R.id.cheap_img); //gluten free
        if(rp.isCheap()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv4);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv4);
        }
        ImageView iv5 = findViewById(R.id.dairy_img); //gluten free
        if(rp.isDairyFree()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv5);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv5);
        }
        ImageView iv6 = findViewById(R.id.health_img); //gluten free
        if(rp.isVeryHealthy()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv6);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv6);
        }
        ImageView iv7 = findViewById(R.id.sus_img); //gluten free
        if(rp.isSustainable()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv7);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv7);
        }
        ImageView iv8 = findViewById(R.id.keto_img); //gluten free
        if(rp.isKetogenic()){
            Picasso.get().load(R.drawable.checkmark).fit().into(iv8);
        }else{
            Picasso.get().load(R.drawable.crossmark).fit().into(iv8);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
interface RecipeDetailService {
    @Headers({
            "X-RapidAPI-Key: 11f118d368mshd1e6592bdae368ep17e8c6jsn465913d15c49"
    })
    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeDetail(@Path("id") int id);
}

class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    RecipeDetailAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public RecipeDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipelist_check, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecipeDetailAdapter.ViewHolder holder, int position) {
        holder.bindData(ingredientList.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    // resets the list with a new set of data
    public void setItems(List<Ingredient> ingredients) {
        ingredientList = ingredients;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ingredient;

        ViewHolder(View itemView) {
            super(itemView);
            txt_ingredient = itemView.findViewById(R.id.ingredient_txt);

        }
        void bindData (Ingredient ingredient){
            txt_ingredient.setText(ingredient.getOriginalString());
            if(RecipeActivity.currentSet.contains(ingredient.getName())){
                txt_ingredient.setBackgroundColor(ContextCompat.getColor(context,R.color.red));
            }else{
                txt_ingredient.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
            }
        }
    }
}

