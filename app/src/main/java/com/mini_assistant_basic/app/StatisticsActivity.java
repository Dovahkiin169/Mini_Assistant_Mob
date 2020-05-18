package com.mini_assistant_basic.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static java.util.stream.Collectors.toMap;


public class StatisticsActivity extends AppCompatActivity
{
    ListView StatisticsView;
    ArrayList<String>  AllhistoryView = new ArrayList<String>();
    ArrayList<String> StatisticsArrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        StatisticsView = findViewById(R.id.statistics_view);
        AllhistoryView = Utility.getHistoryData(getApplicationContext(),getString(R.string.HistoryKey));
        getMostUsedPhrases();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent mySuperIntent = new Intent(StatisticsActivity.this, MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }
        return true;
    }

    private ArrayList<String> getMostUsedPhrases() {

        StatisticsArrayList.clear();
        Map<String, Integer> countMap = new HashMap<>();

        for (String item: AllhistoryView) {
            item = item.substring(17);
            if (countMap.containsKey(item))
                countMap.put(item, countMap.get(item) + 1);
            else
                countMap.put(item, 1);
        }


        Map<String, Integer> sorted = countMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        ArrayList<String> keyList = new ArrayList<String>(sorted.keySet());
        ArrayList<Integer> valueList = new ArrayList<Integer>(sorted.values());
        for(int i =0; i <countMap.size(); i++)
        {
            StatisticsArrayList.add(keyList.get(i) + " = " + valueList.get(i));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StatisticsArrayList );
        StatisticsView.setAdapter(arrayAdapter);
        return null;
    }
}

