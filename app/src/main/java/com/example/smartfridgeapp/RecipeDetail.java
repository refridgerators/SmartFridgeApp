package com.example.smartfridgeapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail {
    /**"vegetarian":false
     "vegan":false
     "glutenFree":true
     "dairyFree":true
     "veryHealthy":false
     "cheap":false
     "veryPopular":false
     "sustainable":false
     "weightWatcherSmartPoints":21
     "gaps":"no"
     "lowFodmap":false
     "ketogenic":false
     "whole30":false
     "servings":10
     "sourceUrl":"http://www.epicurious.com/recipes/food/views/Char-Grilled-Beef-Tenderloin-with-Three-Herb-Chimichurri-235342"
     "spoonacularSourceUrl":"https://spoonacular.com/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992"
     "aggregateLikes":0
     "creditText":"Epicurious"
     "sourceName":"Epicurious"
     "extendedIngredients":[...]18 items
     "id":156992
     "title":"Char-Grilled Beef Tenderloin with Three-Herb Chimichurri"
     "readyInMinutes":45
     "image":"https://spoonacular.com/recipeImages/char-grilled-beef-tenderloin-with-three-herb-chimichurri-156992.jpg"
     "imageType":"jpg"
     "instructions":"PreparationFor spice rub: Combine all ingredients in small bowl. Do ahead: Can be made 2 days ahead. Store airtight at room temperature. For chimichurri sauce: Combine first 8 ingredients in blender; blend until almost smooth. Add 1/4 of parsley, 1/4 of cilantro, and 1/4 of mint; blend until incorporated. Add remaining herbs in 3 more additions, pureeing until almost smooth after each addition. Do ahead: Can be made 3 hours ahead. Cover; chill. For beef tenderloin: Let beef stand at room temperature 1 hour. Prepare barbecue (high heat). Pat beef dry with paper towels; brush with oil. Sprinkle all over with spice rub, using all of mixture (coating will be thick). Place beef on grill; sear 2 minutes on each side. Reduce heat to medium-high. Grill uncovered until instant-read thermometer inserted into thickest part of beef registers 130F for medium-rare, moving beef to cooler part of grill as needed to prevent burning, and turning occasionally, about 40 minutes. Transfer to platter; cover loosely with foil and let rest 15 minutes. Thinly slice beef crosswise. Serve with chimichurri sauce. *Available at specialty foods stores and from tienda.com."
     **/
    @SerializedName("vegetarian")
    private boolean vegetarian;
    @SerializedName("vegan")
    private boolean vegan;
    @SerializedName("glutenFree")
    private boolean glutenFree;
    @SerializedName("veryHealthy")
    private boolean veryHealthy;
    @SerializedName("dairyFree")
    private boolean dairyFree;
    @SerializedName("cheap")
    private boolean cheap;
    @SerializedName("sustainable")
    private boolean sustainable;
    @SerializedName("ketogenic")
    private boolean ketogenic;
    @SerializedName("servings")
    private int servings;
    @SerializedName("sourceUrl")
    private String sourceUrl;
    @SerializedName("spoonacularSourceUrl")
    private String spoonacularSourceUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("instructions")
    private String instructions;
    @SerializedName("extendedIngredients")
    private ArrayList<Ingredient> extendedIngredients;


    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isCheap() {
        return cheap;
    }

    public void setCheap(boolean cheap) {
        this.cheap = cheap;
    }

    public boolean isSustainable() {
        return sustainable;
    }

    public void setSustainable(boolean sustainable) {
        this.sustainable = sustainable;
    }

    public boolean isKetogenic() {
        return ketogenic;
    }

    public void setKetogenic(boolean ketogenic) {
        this.ketogenic = ketogenic;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public ArrayList<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(ArrayList<Ingredient> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
}
