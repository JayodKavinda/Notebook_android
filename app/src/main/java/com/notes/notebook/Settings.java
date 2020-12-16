package com.notes.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;


public class Settings extends AppCompatActivity {


    ImageButton backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        backBtn = findViewById(R.id.backBtn);



    }




    public void setBackground(View v){
        Intent intent = new Intent(this, Backgounds.class);
        startActivity(intent);
    }

    public void goPremium(View v){
        Intent intent = new Intent(this, Premium.class);
        startActivity(intent);
    }



    public void Back_click(View v){
        onBackPressed();
    }
}
