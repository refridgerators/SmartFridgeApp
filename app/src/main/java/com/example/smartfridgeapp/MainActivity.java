package com.example.smartfridgeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateFoodItemMutation;
import com.amazonaws.amplify.generated.graphql.DeleteFoodItemMutation;
import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;
import com.amazonaws.amplify.generated.graphql.UpdateFoodItemMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateFoodItemInput;
import type.DeleteFoodItemInput;
import type.UpdateFoodItemInput;

public class MainActivity extends AppCompatActivity {
    //private AWSAppSyncClient mAWSAppSyncClient;
    RecyclerView mRecyclerView;
    GraphqlAdapter mAdapter;
    TextView st;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public static ArrayList<ListFoodItemsQuery.Item> mFoodItems;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("display_name", AWSMobileClient.getInstance().getUsername());
        editor.commit();
        /* Set up refresher*/
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         new Handler().postDelayed(new Runnable() {
                                                             @Override public void run() {
                                                                 queryFoodList();
                                                                 mSwipeRefreshLayout.setRefreshing(false);
                                                             }
                                                         }, 1000);
                                                     }
                                                 });
        /* Set up nav menu*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent i;
                switch(id)
                {
                    case R.id.add_grocery_item:
                        i = new Intent(MainActivity.this, AddGroceryItem.class);
                        i.putExtra("edit",true);
                        startActivity(i);
                        return true;
                    case R.id.nav_grocery_list:
                        startActivity(new Intent(MainActivity.this, GroceryActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_recipes:
                        i = new Intent(MainActivity.this, RecipeActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_account:
                        i = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_preferences:
                        i = new Intent(MainActivity.this, GroceryListSearch.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        return true;
                    case R.id.nav_logout:
                        AWSMobileClient.getInstance().signOut();
                        i = new Intent(MainActivity.this, AuthenticatorActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        return true;
                    default:
                        return true;
                }

            }
        });

        mRecyclerView = findViewById(R.id.main_recycler_view);
        st = findViewById(R.id.main_stats);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new GraphqlAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        ClientFactory.init(this);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(getBaseContext(), " " + position, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, EditGroceryItem.class);
                        i.putExtra("edit",true);
                        i.putExtra("id",mAdapter.getmData().get(position).id());
                        i.putExtra("name",mAdapter.getmData().get(position).name());
                        i.putExtra("quantity",mAdapter.getmData().get(position).quantity());
                        i.putExtra("unit",mAdapter.getmData().get(position).description());
                        startActivity(i);
                        //mAdapter.getmData().get(1).id(); get id
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Query list data when we return to the screen
        queryFoodList();
    }


    /**Query for list of food items here
      **/
    public void queryFoodList(){
        ClientFactory.appSyncClient().query(ListFoodItemsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(fooditemsCallback);
    }
    private GraphQLCall.Callback<ListFoodItemsQuery.Data> fooditemsCallback = new GraphQLCall.Callback<ListFoodItemsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListFoodItemsQuery.Data> response) {

            mFoodItems = new ArrayList<>(response.data().listFoodItems().items());

            Log.i(TAG, "Retrieved list items: " + mFoodItems.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    st.setText("Number of Items: " + mFoodItems.size());
                    mAdapter.setItems(mFoodItems);
                    mAdapter.notifyDataSetChanged();

                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }

    };

    /**
     * Mutation create grocery item
     */

    public void updateAdd(String name, int quantity, String unit){
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date();
        CreateFoodItemInput input = CreateFoodItemInput.builder().name(name).date(dateFormat.format(date))
                .quantity(quantity).description(unit).build();

        CreateFoodItemMutation addFoodItem = CreateFoodItemMutation.builder().input(input).build();

        ClientFactory.appSyncClient().mutate(addFoodItem).enqueue(additemCallback);
    }

    private GraphQLCall.Callback<CreateFoodItemMutation.Data> additemCallback = new GraphQLCall.Callback<CreateFoodItemMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateFoodItemMutation.Data> response) {


            Log.i(TAG, "response " + response.data().toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //onResume() calls querylist to update list
                    onResume();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    /**
     * Mutation delete grocery item
     */

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
                    onResume();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    /**
     * Mutation delete grocery item
     */

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
                    onResume();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };
}
