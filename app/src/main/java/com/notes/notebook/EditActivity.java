package com.notes.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    EditText noteTitle,noteDetalils;
    int color =10; // default color as 10 = white color ;
    Calendar calendar;
    String currentDate, currentTime;
    Button cancel;
    Dialog dialog;
    int isFavourite = 0;
    ImageButton colorBtn;  // color picker icon in edit Activity

    AppBarLayout appBarLayout;
AdView adView;

Window window;
    SessionBilling session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        /////AdView///////////////////////////////////////////

        // Initialize the Audience Network SDK
        session = new SessionBilling(this);
        if (!session.isPremium()) {
            AudienceNetworkAds.initialize(this);

            adView = new AdView(this, getResources().getString(R.string.Facebook_Banner_placement_edit), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container_edit);

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
                        EditActivity.this,
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

        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////

        dialog = new Dialog(this);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetalils = findViewById(R.id.noteDetails);
         colorBtn = findViewById(R.id.colorBtn);

         appBarLayout = findViewById(R.id.appBarEditActivity);

        calendar = Calendar.getInstance();
        currentDate =  calendar.get(Calendar.YEAR) + "/" + setZeroFirst((calendar.get(Calendar.MONTH)+1)) + "/" +  setZeroFirst(calendar.get(Calendar.DAY_OF_MONTH));
        currentTime =  String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+ ":"+pad( calendar.get(Calendar.MINUTE)) + ":"+ pad(calendar.get(Calendar.SECOND));  //pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE)) + " "+ timePad(calendar.get(Calendar.AM_PM));

        Log.d("Calender","Date and Time: "+ currentDate +"  and "+ currentTime);

    }

    private String  setZeroFirst(int i) {
        if(i<10)
            return "0"+i;
        else
            return String.valueOf(i);
    }

    private String pad(int i) {

        if(i<10)
            return "0"+i;
        return String.valueOf(i);
    }


    public void saveNote(View view){

        if(noteTitle.getText().length()!=0){
            NodeModel note = new NodeModel(noteTitle.getText().toString(),noteDetalils.getText().toString(),currentDate,currentTime,color,isFavourite);

            NoteDatabase db = new NoteDatabase(this);

            long id = db.addNote(note);

            Toast.makeText(this, "Save Note", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        else {
            Toast.makeText(this, "Title can not be empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void Back_click(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Button discard,save,cancelPopup;
        dialog.setContentView(R.layout.discard_popup);

        discard = dialog.findViewById(R.id.discardbtn);
        save = dialog.findViewById(R.id.notDiscard);
        cancelPopup = dialog.findViewById(R.id.discardCancelbtn);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.super.onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });

        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    public void goToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }



    public  void colorPicker(View view){

        final ImageButton color1,color2,color3,color4,color5,color6,color7,color9, color_gray,color_black,color_white;



        dialog.setContentView(R.layout.color_popup);

        color1 = dialog.findViewById(R.id.color1);
        color2 = dialog.findViewById(R.id.color2);
        color3 = dialog.findViewById(R.id.color3);
        color4 = dialog.findViewById(R.id.color4);
        color5 = dialog.findViewById(R.id.color5);
        color6 = dialog.findViewById(R.id.color6);
        color7 = dialog.findViewById(R.id.color7);
        color_gray = dialog.findViewById(R.id.color_gray);
        color9 = dialog.findViewById(R.id.color9);
        color_white = dialog.findViewById(R.id.color_white);


        cancel = dialog.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
        }
        });

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 1;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorOne));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorOne));
                dialog.dismiss();
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 2;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorTwo));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorTwo));
                dialog.dismiss();
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 3;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorThree));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorThree));
                dialog.dismiss();
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 4;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorFour));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorFour));
                dialog.dismiss();
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 5;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorFive));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorFive));
                dialog.dismiss();
            }
        });
        color6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 6;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorSix));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorSix));
                dialog.dismiss();
            }
        });
        color7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 7;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorSeven));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorSeven));
                dialog.dismiss();
            }
        });
        color_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 8;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorGray));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorGray));
                dialog.dismiss();
            }
        });
        color9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 9;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorNine));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorNine));
                dialog.dismiss();
            }
        });
        color_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 10;
                appBarLayout.setBackgroundColor(EditActivity.this.getResources().getColor(R.color.colorWhite));
                setStatusBarColor(EditActivity.this.getResources().getColor(R.color.colorWhite));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();




    }


    private void setStatusBarColor(int color) {

        if(Build.VERSION.SDK_INT> 21){
            window = this.getWindow();
            window.setStatusBarColor(color);
        }
    }

    public void favouriteClick(View v){
        ImageButton favouriteBtn = findViewById(R.id.favouriteBtn);

        if(isFavourite==0){
            favouriteBtn.setColorFilter(this.getResources().getColor(R.color.colorRed));
            isFavourite=1;
        }
        else{
            favouriteBtn.setColorFilter(this.getResources().getColor(R.color.colorGray));
            isFavourite=0;
        }


    }
}
