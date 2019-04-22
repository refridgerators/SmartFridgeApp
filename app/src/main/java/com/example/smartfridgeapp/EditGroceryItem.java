package com.example.smartfridgeapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateFoodItemMutation;
import com.amazonaws.amplify.generated.graphql.DeleteFoodItemMutation;
import com.amazonaws.amplify.generated.graphql.UpdateFoodItemMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import type.CreateFoodItemInput;
import type.DeleteFoodItemInput;
import type.UpdateFoodItemInput;

import static com.amazonaws.mobile.client.internal.oauth2.OAuth2Client.TAG;

public class EditGroceryItem extends Activity {
    TextView name;
    TextView quantity;
    TextView unit;
    Button edititem;
    Button deleteitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grocery_item);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width= dm.widthPixels;
        int height= dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));
        WindowManager.LayoutParams params= getWindow().getAttributes();
        params.gravity= Gravity.CENTER;

        params.x=0;
        params.y= -20;
        getWindow().setAttributes(params);
        Intent i= getIntent();
        name= (TextView) findViewById(R.id.name);
        quantity= (TextView) findViewById(R.id.quantity);
        unit= (TextView) findViewById(R.id.unit);
        if(i.getBooleanExtra("edit",false)==true){
            name.setText(i.getStringExtra("name"));
            String set= "" + i.getIntExtra("quantity",0);
            quantity.setText(set);
            unit.setText(i.getStringExtra("unit"));

        }

        edititem= (Button) findViewById(R.id.edit);
        edititem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= getIntent();
                int intquant=1;
                if(!quantity.getText().toString().equals("")){
                    intquant = Integer.parseInt(quantity.getText().toString());
                }
                updateQuantity(i.getStringExtra("id"),intquant);
                //finish();
            }
        });

        deleteitem= (Button) findViewById(R.id.delete);
        deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= getIntent();
                updateDelete(i.getStringExtra("id"));
            }
        });

    }

    public void updateQuantity(String id, int quantity){

        UpdateFoodItemInput input = UpdateFoodItemInput.builder().id(id).quantity(quantity).build();

        UpdateFoodItemMutation updateFoodItem = UpdateFoodItemMutation.builder().input(input).build();

        ClientFactory.appSyncClient().mutate(updateFoodItem).enqueue(updateitemCallback);
    }

    private GraphQLCall.Callback<UpdateFoodItemMutation.Data> updateitemCallback = new GraphQLCall.Callback<UpdateFoodItemMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<UpdateFoodItemMutation.Data> response) {


            Log.i(TAG, "response " + response.data().toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //onResume();
                    finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    public void updateDelete(String id){

        DeleteFoodItemInput input = DeleteFoodItemInput.builder().id(id).build();

        DeleteFoodItemMutation addFoodItem = DeleteFoodItemMutation.builder().input(input).build();

        ClientFactory.appSyncClient().mutate(addFoodItem).enqueue(deleteitemCallback);
    }

    private GraphQLCall.Callback<DeleteFoodItemMutation.Data> deleteitemCallback = new GraphQLCall.Callback<DeleteFoodItemMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<DeleteFoodItemMutation.Data> response) {


            Log.i(TAG, "response " + response.data().toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };


}
