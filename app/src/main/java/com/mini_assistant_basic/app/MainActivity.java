package com.mini_assistant_basic.app;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView Activate;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(this, this, "com.google.android.tts");

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.SET_ALARM},
                1);

        Activate = findViewById(R.id.activate);

        Activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 10);
            }
        });
    }

    public void set_Alarm(int hour, int minute) {

        if(hour==24)
            hour=00;
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm");
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(i);
        textToSpeech.speak("Your alarm is set", TextToSpeech.QUEUE_ADD, null);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            PhraseGetter(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));

        } else {
            Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private int PhraseGetter(ArrayList<String> results) {
        for (String str : results) {
            if (getWordFromPhrase(str) != -1) {
                return getWordFromPhrase(str);
            }
        }
        flag = false;
        return -1;
    }

    boolean flag = false;
    boolean alarmFlag = false;
    private Handler mHandler = new Handler();
    private int getWordFromPhrase(String result) {

        boolean NotOneWord = result.contains(" ");
        if(NotOneWord)
        {
            List<String> AllWords = new ArrayList<String>(Arrays.asList(result.split(" ")));
            Log.e("data",String.valueOf(AllWords));
            if(  ((AllWords.get(0).equals("time")) || (AllWords.get(0).equals("what") && AllWords.get(1).equals("time")) ||(AllWords.get(0).equals("what") && AllWords.get(1).equals("time") && ((AllWords.get(2).equals("it") && AllWords.get(3).equals("is")) || (AllWords.get(2).equals("is") && AllWords.get(3).equals("it"))))) && !flag )
            {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                textToSpeech.speak("Right now is: " + String.valueOf(formattedDate), TextToSpeech.QUEUE_ADD, null);
                Log.e ("time","Right now is: " + String.valueOf(formattedDate));
                flag = true;
                Toast.makeText(getApplicationContext(), "Right now is: " + String.valueOf(formattedDate), Toast.LENGTH_LONG).show();
            }
            else if( ((AllWords.get(0).equals("date")) || (AllWords.get(0).equals("what") && AllWords.get(1).equals("date"))  || (AllWords.get(0).equals("what") && AllWords.get(1).equals("day")) || (  ( (AllWords.get(0).equals("what") && AllWords.get(1).equals("day")) || (AllWords.get(0).equals("what") && AllWords.get(1).equals("date")) ) && ((AllWords.get(2).equals("it") && AllWords.get(3).equals("is")) || (AllWords.get(2).equals("is") && AllWords.get(3).equals("it")))) || (  ( (AllWords.get(0).equals("what") && AllWords.get(1).equals("day")) || (AllWords.get(0).equals("what") && AllWords.get(1).equals("date")) ) && ((AllWords.get(2).equals("is") && AllWords.get(3).equals("today")) || (AllWords.get(2).equals("today") && AllWords.get(3).equals("is"))))) && !flag )
            {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM, dd, yyyy ");
                String formattedDate = sdf.format(c.getTime());

                textToSpeech.speak("Today is: " + String.valueOf(formattedDate), TextToSpeech.QUEUE_ADD, null);
                Log.e ("Date","Today is: " + String.valueOf(formattedDate));
                flag = true;
                Toast.makeText(getApplicationContext(), "Today is: " + String.valueOf(formattedDate), Toast.LENGTH_LONG).show();
            }
            else if((     AllWords.get(0).equals("alarm") ||
                        ((AllWords.get(0).equals("set") && AllWords.get(1).equals("alarm")) || (AllWords.get(0).equals("set") && AllWords.get(1).equals("an")  && AllWords.get(2).equals("alarm"))) ||
                        (((AllWords.get(0).equals("set") && AllWords.get(1).equals("alarm")) || (AllWords.get(0).equals("set") && AllWords.get(1).equals("an")  && AllWords.get(2).equals("alarm")))  &&  ( (AllWords.get(3).equals("on")) || (AllWords.get(3).equals("at")) || (AllWords.get(3).equals("for")) )  && ( isNumeric(AllWords.get(4)) || ( (AllWords.get(5).equals("a.m.")) || (AllWords.get(5).equals("p.m.")) || (AllWords.get(5).equals("hours")) || (AllWords.get(5).equals("hour")) )))) && !flag)
            {
                if(AllWords.size()<5)
                {
                    textToSpeech.speak("On what time do you want to set alarm?", TextToSpeech.QUEUE_ADD, null);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                            startActivityForResult(intent, 10);
                        }
                    }, 2000);
                    alarmFlag = true;
                }
                else if(isNumeric(AllWords.get(4)))
                {
                    if(( AllWords.get(0).equals("alarm") ||
                            ((AllWords.get(0).equals("set") && AllWords.get(1).equals("alarm")) || (AllWords.get(0).equals("set") && AllWords.get(1).equals("an")  && AllWords.get(2).equals("alarm")))) && AllWords.size()<=3)
                    {
                        textToSpeech.speak("On what time do you want to set alarm?", TextToSpeech.QUEUE_ADD, null);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                                startActivityForResult(intent, 10);
                            }
                        }, 2000);
                        alarmFlag = true;
                    }
                    else if(AllWords.size()>=6 &&(isNumeric(AllWords.get(4)) &&(   (AllWords.get(5).equals("a.m.")) || (AllWords.get(5).equals("hour")) || (AllWords.get(5).equals("hours")))    ))
                    {
                        if(AllWords.size()==6)
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(4)), 00);
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(4)), Integer.parseInt(AllWords.get(6)));
                        }

                        flag = true;
                        alarmFlag = false;
                    }
                    else if(AllWords.size()>=6 &&(isNumeric(AllWords.get(4)) &&((AllWords.get(5).equals("p.m.")) || (AllWords.get(5).equals("hour")) || (AllWords.get(5).equals("hours")))))
                    {
                        if(AllWords.size()==6)
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(4))+12, 00);
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(4))+12, Integer.parseInt(AllWords.get(6)));
                        }
                        flag = true;
                        alarmFlag = false;
                    }
                    else if(( isNumeric(AllWords.get(4)) || AllWords.get(4).contains(":") )&& !flag)
                    {
                        if(AllWords.get(4).contains(":"))
                        {
                            String[] parts = AllWords.get(4).split(":");
                            set_Alarm(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(4)), Integer.parseInt(AllWords.get(6)));
                        }

                        flag = true;
                    }
                }
                else if (isNumeric(AllWords.get(3)))
                {
                    if(( AllWords.get(0).equals("alarm") ||
                            ((AllWords.get(0).equals("set") && AllWords.get(1).equals("alarm")) || (AllWords.get(0).equals("set") && AllWords.get(1).equals("an")  && AllWords.get(2).equals("alarm")))) && AllWords.size()<=3)
                    {
                        textToSpeech.speak("On what time do you want to set alarm?", TextToSpeech.QUEUE_ADD, null);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                                startActivityForResult(intent, 10);
                            }
                        }, 2000);
                        alarmFlag = true;
                    }
                    else if(AllWords.size()>=5 &&(isNumeric(AllWords.get(3)) &&(   (AllWords.get(4).equals("a.m.")) || (AllWords.get(4).equals("hour")) || (AllWords.get(4).equals("hours")))    ))
                    {
                        if(AllWords.size()==5)
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(3)), 00);
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(3)), Integer.parseInt(AllWords.get(5)));
                        }

                        flag = true;
                        alarmFlag = false;
                    }
                    else if(AllWords.size()>=5 &&(isNumeric(AllWords.get(3)) &&((AllWords.get(4).equals("p.m.")) || (AllWords.get(4).equals("hour")) || (AllWords.get(4).equals("hours")))))
                    {
                        if(AllWords.size()==5)
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(3))+12, 00);
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(3))+12, Integer.parseInt(AllWords.get(5)));
                        }
                        flag = true;
                        alarmFlag = false;
                    }
                    else if(( isNumeric(AllWords.get(3)) || AllWords.get(5).contains(":") )&& !flag)
                    {
                        if(AllWords.get(3).contains(":"))
                        {
                            String[] parts = AllWords.get(3).split(":");
                            set_Alarm(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                        }
                        else
                        {
                            set_Alarm(Integer.parseInt(AllWords.get(3)), Integer.parseInt(AllWords.get(5)));
                        }

                        flag = true;
                    }
                }


            }
            else if ( isNumeric(AllWords.get(0)) && (AllWords.get(1).equals("a.m.") || AllWords.get(1).equals("p.m.") || (AllWords.get(1).equals("hour")) || (AllWords.get(1).equals("hours"))) && (AllWords.size()>=3 && isNumeric(AllWords.get(2))) && !flag && alarmFlag)
            {
                if(AllWords.get(1).equals("a.m.") || (AllWords.get(1).equals("hours")) || (AllWords.get(1).equals("hour")))
                {
                    set_Alarm(Integer.parseInt(AllWords.get(0)), Integer.parseInt(AllWords.get(2)));
                }
                else
                {
                    set_Alarm(Integer.parseInt(AllWords.get(0)) + 12, Integer.parseInt(AllWords.get(2)));
                }

                flag = true;
                alarmFlag = false;
            }
            else if ( isNumeric(AllWords.get(0)) && (AllWords.get(1).equals("a.m.") || AllWords.get(1).equals("p.m.")) && (AllWords.size() == 2) && !flag && alarmFlag)
            {
                if(AllWords.get(1).equals("a.m."))
                {
                    set_Alarm(Integer.parseInt(AllWords.get(0)), 00);
                }
                else
                {
                    set_Alarm(Integer.parseInt(AllWords.get(0)) + 12, 00);
                }

                flag = true;
                alarmFlag = false;
            }
        }
        else
        {
            if(result.equals("time") && !flag)
            {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                textToSpeech.speak("Right now is: " + String.valueOf(formattedDate), TextToSpeech.QUEUE_ADD, null);
                Log.e ("time","Right now is: " + String.valueOf(formattedDate));
                Toast.makeText(getApplicationContext(), "Right now is: " + String.valueOf(formattedDate), Toast.LENGTH_LONG).show();
                flag = true;
            }
            else if(result.equals("date") && !flag )
            {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM, dd, yyyy ");
                String formattedDate = sdf.format(c.getTime());

                textToSpeech.speak("Today is: " + String.valueOf(formattedDate), TextToSpeech.QUEUE_ADD, null);
                Log.e ("Date","Today is: " + String.valueOf(formattedDate));
                flag = true;
                Toast.makeText(getApplicationContext(), "Today is: " + String.valueOf(formattedDate), Toast.LENGTH_LONG).show();
            }
            else if (result.equals("alarm") && !flag)
            {

                textToSpeech.speak("On what time do you want to set alarm?", TextToSpeech.QUEUE_ADD, null);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                        startActivityForResult(intent, 10);
                    }
                }, 2000);
                alarmFlag = true;

            }
            else if (isNumeric(result) && !flag && alarmFlag && result.length()<5 && Integer.parseInt(result)<2400)
            {
                String[] Res = splitAfterNChars(result,2);
                set_Alarm(Integer.parseInt(Res[0]), Integer.parseInt(Res[1]));
                flag = true;
                alarmFlag = false;
            }
        }
        return -1;
    }



    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String[] splitAfterNChars(String input, int splitLen){
        return input.split(String.format("(?<=\\G.{%1$d})", splitLen));
    }

    @Override
    public void onInit(int i) {

    }
}