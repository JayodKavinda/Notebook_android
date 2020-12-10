package com.notes.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Calendar;

public class EditNote extends AppCompatActivity {

    EditText title;
    EditText details;
    ImageButton colorBtn,favBtn;
    NoteDatabase db;
    NodeModel note;

    Calendar calendar;
    String currentDate, currentTime;
    Dialog dialog; Button cancel;
    int color;
AdView adView;
    int isFavourite; // storing editNote activity favorite button mood 0 for false 1 for interger


    SessionBilling session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        /////AdView///////////////////////////////////////////

        // Initialize the Audience Network SDK
        session = new SessionBilling(this);
        if (!session.isPremium()) {
            AudienceNetworkAds.initialize(this);

            adView = new AdView(this, getResources().getString(R.string.Facebook_Banner_placement_editNote), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container_editnote);

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
                        EditNote.this,
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

        title = findViewById(R.id.noteTitle2);
        details = findViewById(R.id.noteDetails2);
        colorBtn = findViewById(R.id.colorBtnEdit);
        favBtn = findViewById(R.id.fav_icon_edit);

        dialog = new Dialog(this);

        calendar = Calendar.getInstance();
        currentDate =  calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        currentTime =  String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))+ ":"+pad( calendar.get(Calendar.MINUTE))+ ":"+ pad(calendar.get(Calendar.SECOND));;


        Intent intent =getIntent();
        long id = intent.getLongExtra("ID", 0);

        db = new NoteDatabase(this);
         note = db.getNote(id);

        title.setText(note.getHead());
        details.setText(note.getDesc());
        color = note.getColor();
        isFavourite =note.getFav();
        setColorPicker(color);
        setFavIcon(note.getFav());
    }

    private void setFavIcon(int fav) {
        if(fav == 1)
            favBtn.setColorFilter(this.getResources().getColor(R.color.colorRed));
        else
            favBtn.setColorFilter(this.getResources().getColor(R.color.colorGray));
    }

    void setColorPicker(int color){
        switch (color){
            case 1:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorOne));
                break;
            case 2:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorTwo));
                break;
            case 3:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorThree));
                break;
            case 4:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorFour));
                break;
            case 5:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorFive));
                break;

            case 6:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorSix));
                break;
            case 7:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorSeven));
                break;
            case 8:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorGray));
                break;
            case 9:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorBlack));
                break;
            case 10:
                colorBtn.setColorFilter(this.getResources().getColor(R.color.colorBlack));
                break;
             default:
                 colorBtn.setColorFilter(this.getResources().getColor(R.color.colorBlack));

        }

    }



    private String pad(int i) {

        if(i<10)
            return "0"+i;
        return String.valueOf(i);
    }

    public void saveNote(View view){

        if(title.getText().length()!=0){

            note.setHead(title.getText().toString());
            note.setDesc(details.getText().toString());
            note.setTime(currentTime);
            note.setDate(currentDate);
            note.setColor(color);
            note.setFav(isFavourite);

            int id = db.editNote(note);
            Toast.makeText(this, "Update Note", Toast.LENGTH_SHORT).show();

           /* if(id==note.getID()){
                Toast.makeText(this, "Update Note", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();  */


            goToMain();
        }
        else {
            Toast.makeText(this, "Title can not be empty", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        //should appeare discard dialog box
        Button discard,save,cancelPopup;
        dialog.setContentView(R.layout.discard_popup);

        discard = dialog.findViewById(R.id.discardbtn);
        save = dialog.findViewById(R.id.notDiscard);
        cancelPopup = dialog.findViewById(R.id.discardCancelbtn);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNote.super.onBackPressed();
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

    public  void colorPickerEdit(View view){

        final ImageButton color1,color2,color3,color4,color5,color6,color7,color_gray,color_black,color_white;



        dialog.setContentView(R.layout.color_popup);

        color1 = dialog.findViewById(R.id.color1);
        color2 = dialog.findViewById(R.id.color2);
        color3 = dialog.findViewById(R.id.color3);
        color4 = dialog.findViewById(R.id.color4);
        color5 = dialog.findViewById(R.id.color5);
        color6 = dialog.findViewById(R.id.color6);
        color7 = dialog.findViewById(R.id.color7);
        color_gray = dialog.findViewById(R.id.color_gray);
        color_black = dialog.findViewById(R.id.color_black);
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
                colorBtn.setColorFilter(getColor(R.color.colorOne));
                dialog.dismiss();
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 2;
                colorBtn.setColorFilter(getColor(R.color.colorTwo));
                dialog.dismiss();
            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 3;
                colorBtn.setColorFilter(getColor(R.color.colorThree));
                dialog.dismiss();
            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 4;
                colorBtn.setColorFilter(getColor(R.color.colorFour));
                dialog.dismiss();
            }
        });
        color5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 5;
                colorBtn.setColorFilter(getColor(R.color.colorFive));
                dialog.dismiss();
            }
        });
        color6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 6;
                colorBtn.setColorFilter(getColor(R.color.colorSix));
                dialog.dismiss();
            }
        });
        color7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 7;
                colorBtn.setColorFilter(getColor(R.color.colorSeven));
                dialog.dismiss();
            }
        });
        color_gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 8;
                colorBtn.setColorFilter(getColor(R.color.colorGray));
                dialog.dismiss();
            }
        });
        color_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 9;
                colorBtn.setColorFilter(getColor(R.color.colorBlack));
                dialog.dismiss();
            }
        });
        color_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = 10;
                colorBtn.setColorFilter(getColor(R.color.colorBlack));
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();




    }

    public void favouriteClickEdit(View v){
        ImageButton favouriteBtn = findViewById(R.id.favouriteBtn);

        if(isFavourite==0){
            favBtn.setColorFilter(this.getResources().getColor(R.color.colorRed));
            isFavourite=1;
        }
        else{
            favBtn.setColorFilter(this.getResources().getColor(R.color.colorGray));
            isFavourite=0;
        }


    }

    public void Back_click(View v){
        onBackPressed();
    }
}
