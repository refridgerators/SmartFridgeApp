package com.example.smartfridgeapp;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Ingredient {
    /**
            "id":4073
            "amount":35
            "unit":"g"
            "unitLong":"grams"
            "unitShort":"g"
            "aisle":"Milk, Eggs, Other Dairy"
            "name":"margarine"
            "original":"35 g margarine or butter"
            "originalString":"35 g margarine or butter"
            "originalName":"margarine or butter"
            "metaInformation":[]0 items
            "image":"https://spoonacular.com/cdn/ingredients_100x100/butter-sliced.jpg"
     **/
    /**
     "id":18079
     "aisle":"Pasta and Rice"
     "image":"breadcrumbs.jpg"
     "consitency":"solid"
     "name":"breadcrumbs"
     "original":"1/2 cup fresh breadcrumbs (I used gluten-free!)"
     "originalString":"1/2 cup fresh breadcrumbs (I used gluten-free!)"
     "originalName":"fresh breadcrumbs (I used gluten-free!)"
     "amount":0.5
     "unit":"cup"
     *
     */
    @SerializedName("id")
    private int id;
    @SerializedName("amount")
    private float amount;
    @SerializedName("unit")
    private String unit;
    @SerializedName("aisle")
    private String aisle;
    @SerializedName("name")
    private String name;
    @SerializedName("original")
    private String original;
    @SerializedName("originalString")
    private String originalString;
    @SerializedName("image")
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id &&
                Float.compare(that.amount, amount) == 0 &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(aisle, that.aisle) &&
                Objects.equals(name, that.name) &&
                Objects.equals(original, that.original) &&
                Objects.equals(originalString, that.originalString) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, unit, aisle, name, original, originalString, image);
    }
}
