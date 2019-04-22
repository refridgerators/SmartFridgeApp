package com.example.smartfridgeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;

import java.util.ArrayList;
import java.util.List;

public class GraphqlAdapter extends RecyclerView.Adapter<GraphqlAdapter.ViewHolder> {

    public List<ListFoodItemsQuery.Item> getmData() {
        return mData;
    }

    private List<ListFoodItemsQuery.Item> mData = new ArrayList<>();;
    private LayoutInflater mInflater;


    // data is passed into the constructor
    GraphqlAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mainlist_row, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListFoodItemsQuery.Item> items) {
        mData = items;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_date;
        TextView txt_quantity;

        ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.food_text);
            txt_date = itemView.findViewById(R.id.date_text);
            txt_quantity = itemView.findViewById(R.id.quantity_text);
        }

        void bindData(ListFoodItemsQuery.Item item) {
            txt_name.setText(item.name());
            txt_date.setText(item.date());
            if(item.quantity() == null){
                txt_quantity.setText("1");
            }else {
                txt_quantity.setText(item.quantity() + " " + item.description());
            }

        }
    }
}