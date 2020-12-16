package com.notes.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.appbar.AppBarLayout;

import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    TextView titleNote,detailNoteHeading;
    TextView DetailsNote;
    AppBarLayout appBarLayout;
    NoteDatabase db;
    NodeModel note;

    Window window;  //for set status bar color

    Dialog dialog;

AdView adView;
    SessionBilling session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

////////////////////////////ad View//////////////////////////////////////////////////////////////////////
        // Initialize the Audience Network SDK
        session = new SessionBilling(this);
        if (!session.isPremium()) {
            AudienceNetworkAds.initialize(this);

            adView = new AdView(this, getResources().getString(R.string.Facebook_Banner_placement_details), AdSize.BANNER_HEIGHT_50);

            // Find the Ad Container
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container_details);

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
                        DetailsActivity.this,
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////



        dialog = new Dialog(this);
        titleNote = findViewById(R.id.noteTitleDetails);
        DetailsNote = findViewById(R.id.noteDetailsNew);
        detailNoteHeading = findViewById(R.id.detail_note_text);

        appBarLayout = findViewById(R.id.appBarDetails);

        Intent intent = getIntent();
        long id = intent.getLongExtra("ID", 0);
        db = new NoteDatabase(this);
         note = db.getNote(id);


        setStatusBarColor(note.getColor());

        appBarLayout.setBackgroundColor(getNoteColor(note.getColor()));



        titleNote.setTextSize(18);
        titleNote.setText(note.getHead());
        detailNoteHeading.setText(note.getHead());
        DetailsNote.setTextSize(17);
        DetailsNote.setText((note.getDesc()));


//////////////////////adview


    }

    public int getNoteColor(int color){
        switch (color){
            case 1:
                return this.getResources().getColor(R.color.colorOne);
            case 2:
                return this.getResources().getColor(R.color.colorTwo);

            case 3:
                return this.getResources().getColor(R.color.colorThree);

            case 4:
                return this.getResources().getColor(R.color.colorFour);

            case 5:
                return this.getResources().getColor(R.color.colorFive);

            case 6:
                return this.getResources().getColor(R.color.colorSix);

            case 7:
                return this.getResources().getColor(R.color.colorSeven);

            case 8:
                return this.getResources().getColor(R.color.colorGray);

            case 9:
                return this.getResources().getColor(R.color.colorNine);

            case 10:
                return this.getResources().getColor(R.color.colorWhite);


            default:
                return this.getResources().getColor(R.color.colorWhite);

        }

    }

    private void setStatusBarColor(int color) {

        if(Build.VERSION.SDK_INT> 21){
            window = this.getWindow();
            window.setStatusBarColor(getNoteColor(color));
        }
    }

    public void deleteNote(View view){

        Button YesDelete,noDelete;

        dialog.setContentView(R.layout.delete_popup);

        YesDelete = dialog.findViewById(R.id.YesDeleteBtn);
        noDelete = dialog.findViewById(R.id.noDeletBnt);

        noDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        YesDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Delete note form DB
                db.deleteNote(note.getID());
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(DetailsActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();



    }

    public void editNote(View view){
         Intent intent = new Intent(this,EditNote.class);
         intent.putExtra("ID",note.getID());
         startActivity(intent);
    }

    public void Back_click(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
