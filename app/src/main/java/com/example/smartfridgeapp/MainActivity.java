package com.example.smartfridgeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
    private ArrayList<ListFoodItemsQuery.Item> mFoodItems;
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .cognitoUserPoolsAuthProvider(new CognitoUserPoolsAuthProvider() {
                    @Override
                    public String getLatestAuthToken() {
                        try {
                            return AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                        } catch (Exception e){
                            Log.e("APPSYNC_ERROR", e.getLocalizedMessage());
                            return e.getLocalizedMessage();
                        }
                    }
                }).build();*/
        ClientFactory.init(this);
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
                    //update the list view data set here
                    //mAdapter.setItems(mPets);
                    //mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };
}
