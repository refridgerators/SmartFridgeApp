package com.example.smartfridgeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class GroceryListSearchAdapter extends RecyclerView.Adapter<GroceryListSearchAdapter.ViewHolder> {

    List<Item> mValues;
    Item mRecentlyDeletededItem;
    int mRecentlyDeletedItemPosition;
    private final Context context;
    private LayoutInflater mInflater;

    public GroceryListSearchAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public GroceryListSearchAdapter(List<Item> items, Context context) {
        mValues = items;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    // resets the list with a new set of data
    public void setItems(List<Item> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grocery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(mValues.get(position));
        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public Context getContext() {
        return context;
    }

    public void deleteItem(int position){
        mRecentlyDeletededItem = mValues.get(position);
        mRecentlyDeletedItemPosition = position;
        mValues.remove(position);
        String removefromSet = mRecentlyDeletededItem.getName() + "," + mRecentlyDeletededItem.getQuantity() + "," + mRecentlyDeletededItem.getUnit();
        GroceryListSearch.prefs.remove(removefromSet);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        TextView quantity;
        TextView unit;

        public ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item_name);
            quantity = view.findViewById(R.id.item_quanitity);
            unit = view.findViewById(R.id.item_unit);
        }

        public void bindData(Item item){
            this.item.setText(item.getName());
            this.quantity.setText(item.getQuantity() + "");
            this.unit.setText(item.getUnit());
        }

    }
}

class Item{
    String name;
    String unit;
    int quantity;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, int quantity, String unit) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
