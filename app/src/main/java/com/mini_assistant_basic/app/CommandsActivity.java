package com.mini_assistant_basic.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class CommandsActivity extends Activity {
    ListView CommandsView;
    ArrayList<String> CommandsArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands_activity);
        Collections.addAll(CommandsArrayList, "Browser", "Camera", "Time","Date","Music","Alarm");
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
