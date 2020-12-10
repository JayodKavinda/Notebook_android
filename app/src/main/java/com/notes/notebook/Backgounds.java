package com.notes.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;



public class Backgounds extends AppCompatActivity {


    public static final String BACKGROUND_TYPE = "background_type";
    public static final String BACKGROUND_1 = "background_1";
    public static final String BACKGROUND_2 = "background_2";
    public static final String BACKGROUND_3 = "background_3";
   // public static final String BACKGROUND_4 = "background_4";
   // public static final String BACKGROUND_5 = "background_5";
    public static final String NO_BACKGROUND = "no_background";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgounds);

        ////////Adview ////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////////////////
    }




    public void no_background(View v){
        saveBackground(NO_BACKGROUND);
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void setBackground1(View v){

        saveBackground(BACKGROUND_1);
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void setBackground2(View v){

        saveBackground(BACKGROUND_2);
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void setBackground3(View v){

        saveBackground(BACKGROUND_3);
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }






    public void saveBackground(String v){
        SharedPreferences sharedPreferencesBackground = getSharedPreferences("SharedNew",MODE_PRIVATE); // for perment storeage of view(layout) and sort
        SharedPreferences.Editor editor = sharedPreferencesBackground.edit();
        editor.putString(BACKGROUND_TYPE,v);
        editor.apply();
    }

    public void Back_click(View v){
        onBackPressed();
    }
}
