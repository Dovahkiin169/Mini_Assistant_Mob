package com.mini_assistant_basic.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;

public class CommandsActivity extends AppCompatActivity {
    ListView CommandsView;
    ArrayList<String> CommandsArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands_activity);
        Collections.addAll(CommandsArrayList, "Time", "What time", "What time is it","What time it is","",
                                                         "Date", "What day", "What date", "What date it is", "What day is it", "What day today is" , "What date today is","",
                                                         "Alarm", "Set alarm", "Set an alarm", "Set alarm at 4 p.m", "Set an alarm on 12 a.m", "Set alarm for 12 hours 13 minutes","",
                                                         "Browser", "Open browser", "Search for Facebook","Search for Weather", "",
                                                         "Music", "Play Music", "",
                                                         "Camera", "Open Camera", "" );
        CommandsView = findViewById(R.id.commands_view);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CommandsArrayList );
        CommandsView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent mySuperIntent = new Intent(CommandsActivity.this, MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }
        return true;
    }
}
