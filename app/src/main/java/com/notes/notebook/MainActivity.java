package com.notes.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    private NodeAdapter nodeAdapter;
    private List<NodeModel> modelList;

     //to store main LL and set backgroud images for it

    RadioButton radioButtonView, radioButtonSort, radioButtonSortOrder;
    RadioGroup radioGroupView, radioGroupSort,radioGroupSortOrder;
    Dialog dialog;
    SessionBilling session;

    public static final String SHARED_PREP = "shared_pref";
    public static final String VIEW_TYPE = "view_type";
    public static final String SORT_TYPE = "sort_type";
    public static final String GRID_TYPE = "grid";
    public static final String LIST_TYPE = "list";

    public static final String DESC =  "id DESC";
    public static final String ASCE =  "id";
    public String sortType;
    public String viewType;
    String backgroundDetalis;  //this is for keeping getIntent extra detials from Background Activity

    private AdView adView;
    private InterstitialAd interstitialAd;
    private final String TAG ="tag";


    private ReviewManager reviewManager;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /////AdView///////////////////////////////////////////


        // Initialize the Audience Network SDK
        session = new SessionBilling(this);
        if (!session.isPremium()) {
            AudienceNetworkAds.initialize(this);

            adView = new AdView(this, getResources().getString(R.string.Facebook_Banner_placement), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

            // Add the ad view to your activity layout
            adContainer.addView(adView);

            // Request an ad
            // adView.loadAd();

            AdListener adListener = new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                /*
                Toast.makeText(
                        MainActivity.this,
                        "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG)
                        .show();

                        */
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Ad loaded callback
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }


            };

            // Request an ad
            adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


            interstitialAd = new InterstitialAd(this, getResources().getString(R.string.Facebook_Interstitial_placement));

            interstitialAdSet();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        FloatingActionButton fab = findViewById(R.id.fab);


        loadData();

        initReviewManager();
        dialog = new Dialog(this);

        NoteDatabase db= new NoteDatabase(this);
        modelList=db.getNotes(getSortType());
        recyclerView = findViewById(R.id.listOfNotes);


        if(viewType.equals(GRID_TYPE)){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));}

        else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));}



        nodeAdapter = new NodeAdapter(modelList,this);

        recyclerView.setAdapter(nodeAdapter);

        getBackground();



    }

    private void initReviewManager() {
        reviewManager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    reviewInfo = task.getResult();
                } else {
                    // There was some problem, continue regardless of the result.
                    //Toast.makeText(Settings.this , "There was a problem in review info ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void RateUs(){

        if(reviewInfo != null){
            Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    if (task.isSuccessful()) {

                       // Log.i("rated::", "Rated US!!!");

                        SharedPreferences.Editor editor = getSharedPreferences("isRateAppear",MODE_PRIVATE).edit();
                        editor.putBoolean("isRateAppear",true);
                        editor.apply();

                    }



                }
            });
            finishAffinity();
        }else{
            finishAffinity();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void getBackground(){
        SharedPreferences sharedPreferencesBackground = getSharedPreferences("SharedNew",MODE_PRIVATE);
        backgroundDetalis = sharedPreferencesBackground.getString(Backgounds.BACKGROUND_TYPE,Backgounds.BACKGROUND_1);   ///defacut = 1
        ImageView mainImageView  = findViewById(R.id.mainImageView);
        mainImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(backgroundDetalis.equals(Backgounds.BACKGROUND_1)){
            mainImageView.setImageResource(R.drawable.ic_night_moon);
        }
       else if (backgroundDetalis.equals(Backgounds.BACKGROUND_2)){
           mainImageView.setImageResource(R.drawable.ic_triangles);
       }
        else if (backgroundDetalis.equals(Backgounds.BACKGROUND_3))
            mainImageView.setImageResource(R.drawable.ic_forest);

        else
            mainImageView.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
    }

    public String getSortType(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREP,MODE_PRIVATE); // for perment storeage of view(layout) and sort
        final String defaultString = "id DESC";
        sortType= sharedPreferences.getString(SORT_TYPE,DESC);

        return sortType;
    }

    public void layout_view(){  //layout view fuction call inside of OnMenuItemClick (bottom of page)

        Button cancel, Ok;
        RadioButton gridBtn,listBtn;
        dialog.setContentView(R.layout.view_popup);

        gridBtn = dialog.findViewById(R.id.gridBtn);
        listBtn = dialog.findViewById(R.id.listbtn);

        if(viewType.equals(GRID_TYPE))   ////Selecting radio btn according to last save position when popup is appering
            gridBtn.setChecked(true);

        else
            listBtn.setChecked(true);


        cancel = dialog.findViewById(R.id.cancelBtn);
        Ok = dialog.findViewById(R.id.okBtn);
        radioGroupView =dialog.findViewById(R.id.radio_group);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioInt = radioGroupView.getCheckedRadioButtonId();
                radioButtonView = dialog.findViewById(radioInt);
                if(radioButtonView.getText().equals("Grid")){
                    saveData(GRID_TYPE); dialog.dismiss();

                }else{
                    saveData(LIST_TYPE);
                    dialog.dismiss();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void layout_sort(){  //layout sort fuction call inside of OnMenuItemClick (bottom of page)

        Button ok,cancel;

        dialog.setContentView(R.layout.sort_popup);

        ok = dialog.findViewById(R.id.okSort);
        cancel = dialog.findViewById(R.id.cancelSort);


        sortRadioButtonSelect();

        radioGroupSort =dialog.findViewById(R.id.radio_group_sort);
        radioGroupSortOrder =dialog.findViewById(R.id.radio_group_sort_order);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioInt = radioGroupSort.getCheckedRadioButtonId();
                int radioIntOrder = radioGroupSortOrder.getCheckedRadioButtonId();

                radioButtonSort = dialog.findViewById(radioInt);
                radioButtonSortOrder = dialog.findViewById(radioIntOrder);

                ///Sorting According Title,Data
                if(radioButtonSort.getText().equals("Title")){  //Select Sort by title
                    sortType="title";

                }
                else if(radioButtonSort.getText().equals("Data modified")){  //Select sort by Data modifed
                    sortType="date,time";
                }
                else {  // select sort by data created
                    sortType = "id";
                }

                //Sorting according order Asc OR Desc

                if(radioButtonSortOrder.getText().equals("Descending")) {  //descending order

                        if(sortType.equals("date,time"))
                            sortType = "date DESC,time DESC";
                        else
                            sortType = sortType +" DESC";
                }


               //else asceding order
                //save to memory
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREP,MODE_PRIVATE); // for perment storeage of view(layout) and sort
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SORT_TYPE,sortType);
                editor.apply();

                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void sortRadioButtonSelect(){
        RadioButton btnAcs,btnDes,btnTitle,btnDataModified,btnDataCreated;

        btnTitle=dialog.findViewById(R.id.btnTitle);
        btnDataModified=dialog.findViewById(R.id.btnDatamodified);
        btnDataCreated=dialog.findViewById(R.id.btnDataCreated);
        btnAcs=dialog.findViewById(R.id.btnAsce);
        btnDes=dialog.findViewById(R.id.btnDesc);

        if(sortType.equals("id")) {////Selecting radio btn according to last save position when popup is appering
            btnDataCreated.setChecked(true);
            btnAcs.setChecked(true);
        }  else if(sortType.equals("id DESC")){
            btnDataCreated.setChecked(true);
            btnDes.setChecked(true);
        }
        else if(sortType.equals("title")){
            btnTitle.setChecked(true);
            btnAcs.setChecked(true);
        }else if(sortType.equals("title DESC")){
            btnTitle.setChecked(true);
            btnDes.setChecked(true);
        }else if(sortType.equals("date,time")){
            btnDataModified.setChecked(true);
            btnAcs.setChecked(true);
        }else {
            btnDataModified.setChecked(true);
            btnDes.setChecked(true);
        }





    }

    public void saveData(String v){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREP,MODE_PRIVATE); // for perment storeage of view(layout) and sort
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VIEW_TYPE,v);
        editor.apply();

        if(v.equals(GRID_TYPE))
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREP,MODE_PRIVATE); // for perment storeage of view(layout) and sort

        viewType= sharedPreferences.getString(VIEW_TYPE,LIST_TYPE);

    }

    public void Add_Note(View view){
        Intent intent = new Intent(this,EditActivity.class);
        startActivity(intent);

    }







    public void showPopupMainMenu(View view){
        PopupMenu menu= new PopupMenu(this,view);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.popup_main_menu);
        menu.show();

    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sortby:
                //Sort By menu
                layout_sort();
                return true;

            case R.id.viewby:
                //View by menu
                layout_view();
                return true;

        }
        return false;
    }

    public void settings(View view){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences = getSharedPreferences("isRateAppear", MODE_PRIVATE);
        boolean rateAppear = sharedPreferences.getBoolean("isRateAppear", false);

        if(!rateAppear){

            if(Build.VERSION.SDK_INT>= 21){
                RateUs();
            }else{
                SharedPreferences.Editor editor = getSharedPreferences("isRateAppear",MODE_PRIVATE).edit();
                editor.putBoolean("isRateAppear",true);
                editor.apply();
                finishAffinity();

            }

            //Log.i("rated::", "Rated!!!");

        }else {
           // Log.i("rated::", "Not Rated!!!");
            Button exit, cancelExit;

            dialog.setContentView(R.layout.exit_popup);
            exit = dialog.findViewById(R.id.exitBtn);
            cancelExit = dialog.findViewById(R.id.cancelExit);

            cancelExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //finish();

                    if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                        interstitialAd.show();
                    }
                    finishAffinity();
                    // System.exit(0);
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }

    private void interstitialAdSet() {

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

}


