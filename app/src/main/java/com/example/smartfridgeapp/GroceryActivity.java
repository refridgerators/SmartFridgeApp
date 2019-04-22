package com.example.smartfridgeapp;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroceryActivity extends AppCompatActivity {
    private HashSet<String> owned, missing, need;
    private List<String> list;
    private RecyclerView rv;
    private GroceryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
        owned = new HashSet<>();
        for(ListFoodItemsQuery.Item i : MainActivity.mFoodItems){
            owned.add(i.name());
        }

        missing = new HashSet<>();
        Set<String> prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(this).getStringSet("grocery_prefs", new HashSet<String>());
        for(String s : prefs){
            missing.add(s.split(",")[0]);
        }
        need = clone(missing);
        need.removeAll(owned);
        list = new ArrayList<>();
        list.addAll(need);
        rv = findViewById(R.id.recylcer_view_grocery);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new GroceryAdapter(this, list);
        rv.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static<T> HashSet<T> clone(Set<T> original) {
        HashSet<T> copy = new HashSet<>(original);
        return copy;
    }

}

class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {

    List<String> nValues;
    private final Context context;
    private LayoutInflater mInflater;

    public GroceryAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public GroceryAdapter(Context context, List<String> items ) {
        this.nValues = items;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // resets the list with a new set of data
    public void setItems(List<String> items) {
        nValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grocery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(nValues.get(position));
        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return nValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;


        public ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item_name);

        }

        public void bindData(String item){
            this.item.setText(item);
        }

    }
}
