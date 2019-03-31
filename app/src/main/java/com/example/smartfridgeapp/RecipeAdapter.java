package com.example.smartfridgeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> dataList;
    private Context context;
    private LayoutInflater mInflater;
    // data is passed into the constructor
    RecipeAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipelist_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        holder.bindData(dataList.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // resets the list with a new set of data
    public void setItems(List<Recipe> recipes) {
        dataList = recipes;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        TextView txt_likes;
        TextView txt_nums;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.recipe_title);
            txt_nums = itemView.findViewById(R.id.food_num_text);
            txt_likes = itemView.findViewById(R.id.like_text);
            image = itemView.findViewById(R.id.recipe_photo);
        }

        void bindData(Recipe recipe) {
            txt_title.setText(recipe.getTitle());
            txt_nums.setText("Used: " + recipe.getUsedIngredientCount() + "Missing: " + recipe.getMissedIngredientCount());
            txt_likes.setText("Likes: " + recipe.getLikes());
            Picasso.get().load(recipe.getImage()).placeholder(R.drawable.default_sign_in_logo)
                    .error(R.drawable.ic_launcher_background).into(image);
        }
    }
}