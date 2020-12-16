package com.notes.notebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Premium extends AppCompatActivity {

    BillingClient mBillingClient;
    Button buy;
    HashMap<String, SkuDetails> skuDetailsHashMap;
    SessionBilling session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        buy = findViewById(R.id.premium_btn);

        //premiumAmount= findViewById(R.id.premiumAmountTxt);
        session = new SessionBilling(this);
        buy.setEnabled(false);
        billing();

        if(session.isPremium()){

            buy.setText("You are a Premium User");
            buy.setEnabled(false);
        }

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable() && skuDetailsHashMap != null) {
                    BillingFlowParams mBillingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsHashMap.get("notebook_pro"))  //In-app product name
                            .build();
                    mBillingClient.launchBillingFlow(Premium.this, mBillingFlowParams);
                }else{
                    Toast.makeText(Premium.this, "Please check your INTERNET connection!",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    private void billing() {

        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {
                    for (Purchase purchase : purchases) {
                        handlePurchase(purchase);
                    }


                } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user cancelling the purchase flow.
                } else {
                    // Handle any other error codes.
                }
                // Toast.makeText(Premium.this, "After Buy " + billingResult.getResponseCode(),Toast.LENGTH_SHORT).show();

            }

            private void handlePurchase(Purchase purchase) {

                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
                    Toast.makeText(Premium.this, "purchased!! " ,Toast.LENGTH_SHORT).show();
                    if (!purchase.isAcknowledged()) {
                        AcknowledgePurchaseParams acknowledgePurchaseParams =
                                AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.getPurchaseToken())
                                        .build();
                        mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                            @Override
                            public void onAcknowledgePurchaseResponse(BillingResult
                                                                              billingResult) {

                                SessionBilling session= new SessionBilling(Premium.this);
                                session.setPremium(true);

                                //need to refresh activity
                                startActivity(getIntent());

                            }
                        });
                    }
                }}

        };

        mBillingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(purchasesUpdatedListener)
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                // Toast.makeText(Premium.this, "onBillingSetupFinished " ,Toast.LENGTH_SHORT).show();
                // Log.d(TAG, “onBillingSetupFinished);
                if (billingResponseCode == BillingClient.BillingResponseCode.OK)
                {
                    //getPurchasedItems();
                    getSKUDetails();

                    //buy.setEnabled(true);
                }
            }

            void getSKUDetails() {

                skuDetailsHashMap = new HashMap<>();
                List<String> skuList = new ArrayList<>();
                skuList.add("notebook_pro"); // Testing id : android.test.purchased
                SkuDetailsParams skuParams = SkuDetailsParams.newBuilder()
                        .setType(BillingClient.SkuType.INAPP)
                        .setSkusList(skuList)
                        .build();
                mBillingClient.querySkuDetailsAsync(skuParams,
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(BillingResult billingResult,
                                                             List<SkuDetails> skuDetailsList) {
                                // Process the result.
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    skuDetailsHashMap.put(skuDetails.getSku(), skuDetails);
                                    if(!session.isPremium()) {
                                        buy.setText("Buy Now " + skuDetailsHashMap.get(skuDetails.getSku()).getPrice());
                                        buy.setEnabled(true);
                                    }

                                }

                            }
                        });

            }

            void getPurchasedItems() {


            }



            @Override
            public void onBillingServiceDisconnected() {
                // startConnection();
                Toast.makeText(Premium.this, "Disconnected..! " ,Toast.LENGTH_SHORT).show();
                // Log.d(TAG, “onBillingServiceDisconnected“);
            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void closePremiumClick(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
