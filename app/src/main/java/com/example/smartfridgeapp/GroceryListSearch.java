package com.example.smartfridgeapp;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GroceryListSearch extends AppCompatActivity{
    private RecyclerView rv;
    private Button btn;
    private AutoCompleteTextView mtv, mtv3;
    private EditText qtv;
    private GroceryListSearchAdapter adapter;
    private SharedPreferences.Editor editor;
    public static Set<String> prefs;
    private static final String[] FOODS = new String[] {
            "Asparagus" , "Broccoli" , "Carrots" , "Cauliflower" , "Celery" , "Corn" , "Cucumbers" , "Lettuce " , "Mushrooms" , "Onions" , "Peppers" , "Potatoes" , "Spinach" , "Squash" , "Zucchini" , "Tomatoes" , "Fresh fruits" , "Apples" , "Avocados" , "Bananas" , "Berries" , "Cherries" , "Grapefruit" , "Grapes" , "Kiwis" , "Lemons" , "Limes" , "Melon" , "Oranges" , "Peaches" , "Nectarines" , "Pears" , "Plums" , "Bagels" , "Chip dip" , "English muffins" , "Eggs" , "Fruit juice" , "Hummus" , "Tofu" , "Tortillas" , "Burritos" , "Fish sticks" , "Ice cream " , "Sorbet" , "Juice concentrate" , "Pizza" , "Pizza Rolls" , "Popsicles" , "Fries" , "Tater tots" , "Vegetables" , "Veggie burgers" , "BBQ sauce" , "Gravy" , "Honey" , "Hot sauce" , "Jam" , "Jelly" , "Preserves" , "Ketchup" , "Mustard" , "Mayonnaise" , "Pasta sauce" , "Relish" , "Salad dressing" , "Salsa" , "Soy sauce" , "Steak sauce" , "Syrup" , "Worcestershire sauce" , "Bouillon cubes" , "Cereal" , "Coffee" , "Instant potatoes" , "Lime juice" , "Mac & cheese" , "Olive oil" , "Pancake" , "Pasta" , "Peanut butter" , "Pickles" , "Rice" , "Tea" , "Vegetable oil" , "Vinegar" , "Applesauce" , "Baked beans" , "Chili" , "Fruit" , "Olives" , "Tinned meats" , "Tuna" , "Chicken" , "Tomatoes" , "Basil" , "Black pepper" , "Cilantro" , "Cinnamon" , "Garlic" , "Ginger" , "Mint" , "Oregano" , "Paprika" , "Parsley" , "Red pepper" , "Salt" , "Spice mix" , "Vanilla extract" , "Butter" , "Margarine" , "Cottage cheese" , "Half & half" , "Milk" , "Sour cream" , "Whipped cream" , "Yogurt" , "Cheese" , "Bleu cheese" , "Cheddar" , "Cottage cheese" , "Cream cheese" , "Feta" , "Goat cheese" , "Mozzarella" , "rovolone" , "Parmesan" , "Provolone" , "Ricotta" , "Sandwich slices" , "Swiss" , "Meat" , "Bacon" , "Sausage" , "Beef" , "Chicken" , "Ground beef" , "Turkey" , "Ham" , "Pork" , "Hot dogs" , "Lunchmeat" , "Turkey" , "Seafood" , "Catfish" , "Crab" , "Lobster" , "Mussels" , "Oysters" , "Salmon" , "Shrimp" , "Tilapia" , "Tuna" , "Beverages" , "Beer" , "Club soda" , "Tonic" , "Champagne" , "Gin" , "Juice" , "Mixers" , "Red wine" , "White wine" , "Rum" , "Sake" , "Soda pop" , "Sports drink" , "Whiskey" , "Vodka" , "Bagels" , "Croissants" , "Buns" , "Rolls" , "Cake" , "Cookies" , "Donuts" , "Pastries" , "Fresh bread" , "Sliced bread" , "Pie" , "Pita bread" , "Baking" , "Bread crumbs" , "Cake" , "Brownie mix" , "Cake icing" , "Chocolate chips" , "Cocoa" , "Flour" , "Shortening" , "Sugar" , "Sugar substitute"
    };
    private static final String[] UNITS = new String[] {
            "lbs", "oz", "gallons", "cups", "quarts", "pounds", "g", "kg", "mg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_search);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, FOODS);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, UNITS);
        mtv = findViewById(R.id.multiAutoCompleteTextView);
        mtv.setAdapter(arrayAdapter);
        mtv.setThreshold(1);
        qtv = findViewById(R.id.quantity_text);
        mtv3 = findViewById(R.id.multiAutoCompleteTextView3);
        mtv3.setAdapter(arrayAdapter2);
        mtv.setThreshold(1);
        btn = findViewById(R.id.grocery_list_search_btn);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        ArrayList<Item> items = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(this).getStringSet("grocery_prefs", new HashSet<String>());
        for(String s: prefs){
            List<String> sList = Arrays.asList(s.split(","));
            int quantity = Integer.parseInt(sList.get(1));
            items.add(new Item(sList.get(0), quantity, sList.get(2)));
        }

        //recycler view setup
        adapter = new GroceryListSearchAdapter(this);
        adapter.setItems(items);
        rv = findViewById(R.id.grocery_list_serach_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new s2dprefsCallBack(adapter));
        itemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.remove("grocery_prefs");
        editor.apply();
        editor.putStringSet("grocery_prefs", prefs);
        editor.apply();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void addtoList(View view){
        String listItem = mtv.getText().toString()+ "," + qtv.getText().toString() + "," + mtv3.getText().toString();
        adapter.mValues.add(new Item(mtv.getText().toString(), Integer.parseInt(qtv.getText().toString()) , mtv3.getText().toString()));
        prefs.add(listItem);
        adapter.notifyDataSetChanged();
    }
}
