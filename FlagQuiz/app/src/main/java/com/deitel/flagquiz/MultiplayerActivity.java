package com.deitel.flagquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;


public class MultiplayerActivity extends AppCompatActivity {


    private boolean preferencesChanged = true; // did preferences change?
    public static EditText Username1 ;
    public static EditText Username2 ;
    public static EditText Username3 ;
    public static EditText Username4 ;
    public static EditText Username5 ;
    int NumberOfPlayers1;
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    public static final String PLAYERS ="pref_numberOfPlayers";

    public static int[] PlayerScores = new int[15];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(
                        preferencesChangeListener);


        Username1 = (EditText)findViewById(R.id.player1edt);
        Username2 = (EditText)findViewById(R.id.player2edt);
        Username3 = (EditText)findViewById(R.id.player3edt);
        Username4 = (EditText)findViewById(R.id.player4edt);
        Username5= (EditText)findViewById(R.id.player5edt);



    }
    // listener for changes to the app's SharedPreferences
    private OnSharedPreferenceChangeListener preferencesChangeListener =
            new OnSharedPreferenceChangeListener() {
                // called when the user changes the app's preferences
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true; // user changed app setting
                    Log.i("NUMBEROFPLAYERSMULTI", NumberOfPlayers1+" ");
                    if (key.equals(PLAYERS)){
                        String players =
                                sharedPreferences.getString(PLAYERS, null);
                        NumberOfPlayers1 = Integer.parseInt(players);

                    }


                }
            };


    public static SharedPreferences setPrefences(SharedPreferences sharedPreferences ){


      return sharedPreferences ;

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (preferencesChanged) {
            // now that the default preferences have been set,
            // initialize MainActivityFragment and start the quiz
            String players =
                    PreferenceManager.getDefaultSharedPreferences(this).getString(PLAYERS, null);
            NumberOfPlayers1 = Integer.parseInt(players);
            setVisibility();
            preferencesChanged = false;

        }
    }



    public void setVisibility(){
     // MultiplayerActivity hello = new MultiplayerActivity();
     //MainActivity.getPreferences(R.xml.preferences);
     //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
     //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MultiplayerActivit, R.xml.preferences, false);
     //String  n =preferences.getString(MainActivity.PLAYERS, null);
     //SharedPreferences pref= new Sha;
    // pref.getString(MainActivity.PLAYERS);
//      int numPlayers = Integer.parseInt(MainActivity.PLAYERS);

        switch(NumberOfPlayers1){

            case 0:
                Username2.setVisibility(View.INVISIBLE);
                Username3.setVisibility(View.INVISIBLE);
                Username4.setVisibility(View.INVISIBLE);
                Username5.setVisibility(View.INVISIBLE);
                break;

            case 1: Username2.setVisibility(View.INVISIBLE);
                Username3.setVisibility(View.INVISIBLE);
                Username4.setVisibility(View.INVISIBLE);
                Username5.setVisibility(View.INVISIBLE);
                break;
            case 2: Username3.setVisibility(View.INVISIBLE);
                Username4.setVisibility(View.INVISIBLE);
                Username5.setVisibility(View.INVISIBLE);

                break;
            case 3:  Username4.setVisibility(View.INVISIBLE);
                Username5.setVisibility(View.INVISIBLE);

                break;
            case 4:  Username5.setVisibility(View.INVISIBLE);
                break;


        }


    }


    public static void setUserNames() {

         if(Username1.getText().toString()!=null && !Username1.getText().toString().isEmpty()){

             MainActivityFragment.Player1= Username1.getText().toString();


         }



         if(Username2.getText().toString()!=null && !Username2.getText().toString().isEmpty()){
             MainActivityFragment.Player2= Username2.getText().toString();


         }

         if(Username3.getText().toString()!=null && !Username3.getText().toString().isEmpty()){

             MainActivityFragment.Player3= Username3.getText().toString();

         }

         if(Username4.getText().toString()!=null && !Username4.getText().toString().isEmpty()){

             MainActivityFragment.Player4= Username4.getText().toString();

         }


         if(Username5.getText().toString()!=null && !Username5.getText().toString().isEmpty()){

             MainActivityFragment.Player5= Username5.getText().toString();


         }



    
         

    }

    public void play(View buttonView){



        Intent preferencesIntent = new Intent(this, MainActivity.class);
        startActivity(preferencesIntent);





    }






}
