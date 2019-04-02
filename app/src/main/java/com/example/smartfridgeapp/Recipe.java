package com.example.smartfridgeapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("usedIngredientCount")
    private int usedIngredientCount;
    @SerializedName("missedIngredientCount")
    private int missedIngredientCount;
    @SerializedName("likes")
    private int likes;
    @SerializedName("missedIngredients")
    private List<Ingredient> missedIngredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Ingredient> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(List<Ingredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }
    /*
    public ArrayList<Ingredient> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(ArrayList<Ingredient> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }*/
}
