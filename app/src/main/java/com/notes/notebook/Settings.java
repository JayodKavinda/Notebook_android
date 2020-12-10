package com.notes.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;



public class Settings extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);




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
