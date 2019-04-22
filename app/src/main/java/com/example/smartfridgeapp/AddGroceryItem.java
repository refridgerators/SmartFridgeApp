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
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import type.CreateFoodItemInput;

public class AddGroceryItem extends Activity {
    TextView name;
    TextView quantity;
    TextView unit;
    Button additem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_item);

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

        additem= (Button) findViewById(R.id.additem);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //name= (TextView) findViewById(R.id.name);
                //quantity= (TextView) findViewById(R.id.quantity);
                //unit= (TextView) findViewById(R.id.unit);
                String strname= name.getText().toString();
                int intquant=1;
                if(!quantity.getText().toString().equals("")){
                    intquant = Integer.parseInt(quantity.getText().toString());
                }
                String strunit= unit.getText().toString();
                if(strunit.equals("")){
                    strunit= "units";
                }
                updateAdd(strname,intquant,strunit);
                //finish();
            }
        });

    }

    /**
     * Mutation create grocery item
     */

    public void updateAdd(String name, int quantity, String unit){
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date();
        CreateFoodItemInput input = CreateFoodItemInput.builder().name(name.toLowerCase()).date(dateFormat.format(date))
                .quantity(quantity).description(unit).build();

        CreateFoodItemMutation addFoodItem = CreateFoodItemMutation.builder().input(input).build();

        ClientFactory.appSyncClient().mutate(addFoodItem).enqueue(additemCallback);
    }

    private GraphQLCall.Callback<CreateFoodItemMutation.Data> additemCallback = new GraphQLCall.Callback<CreateFoodItemMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateFoodItemMutation.Data> response) {


           // Log.i(TAG, "response " + response.data().toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //onResume() calls querylist to update list
                    finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
           // Log.e(TAG, e.toString());
        }
    };
}
