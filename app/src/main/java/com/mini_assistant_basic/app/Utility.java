package com.mini_assistant_basic.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;

public class Utility
 {
     public static void setHistoryData(Context context, ArrayList<String> array, String Key)
     {
         SharedPreferences prefs = context.getSharedPreferences("preferencename", 0);
         SharedPreferences.Editor editor = prefs.edit();
         editor.putInt(Key +"_size", array.size());
         for(int i=0;i<array.size();i++)
             editor.putString(Key + "_" + i, array.get(i));
         editor.apply();
     }

     public static ArrayList<String> getHistoryData(Context context, String Key)
     {
         SharedPreferences prefs = context.getSharedPreferences("preferencename", 0);
         int size = prefs.getInt(Key + "_size", 0);
         ArrayList<String> array = new ArrayList<>(size);
         for(int i=0;i<size;i++)
             array.add(prefs.getString(Key + "_" + i, null));
         return array;
     }
 }
