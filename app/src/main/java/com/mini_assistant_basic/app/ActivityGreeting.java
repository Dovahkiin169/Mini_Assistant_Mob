package com.mini_assistant_basic.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;


public class ActivityGreeting extends AppCompatActivity
 {
    private static int SPLASH_TIME = 1400; //This is 1.4 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState)
     {


        setTheme(R.style.AppThemeForLogo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

             new Handler().postDelayed(new Runnable()
             {
                 @Override
                 public void run()
                 {
                     Intent mySuperIntent = new Intent(ActivityGreeting.this, MainActivity.class);
                     startActivity(mySuperIntent);
                     finish();
                 }
             }, SPLASH_TIME);
     }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
     {
        if(keyCode==KeyEvent.KEYCODE_BACK)
         {
            //do nothing. Because we don't want app to close.
         }
        return true;
     }
 }

