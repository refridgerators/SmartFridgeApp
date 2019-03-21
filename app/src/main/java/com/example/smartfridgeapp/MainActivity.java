package com.example.smartfridgeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.amazonaws.amplify.generated.graphql.ListFoodItemsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {
    //private AWSAppSyncClient mAWSAppSyncClient;
    RecyclerView mRecyclerView;
    GraphqlAdapter mAdapter;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ArrayList<ListFoodItemsQuery.Item> mFoodItems;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                switch(id)
                {
                    case R.id.nav_logout:
                        AWSMobileClient.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, AuthenticatorActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    default:
                        return true;
                }

            }
        });

        mRecyclerView = findViewById(R.id.main_recycler_view);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new GraphqlAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        ClientFactory.init(this);
    }
    @Override
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
}
