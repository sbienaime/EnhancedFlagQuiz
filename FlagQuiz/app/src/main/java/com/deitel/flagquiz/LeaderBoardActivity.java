package com.deitel.flagquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Output;
import android.net.Uri;
import android.support.design.shape.ShapePath;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Arrays;

import org.w3c.dom.Text;
import android.support.v4.app.DialogFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.LinkedList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity implements Dialog_Fragment.OnFragmentInteractionListener {
    public static SharedPreferences ScorePrefs;
    public static TextView Topscore;
    public static TextView Topscore2;
    public static TextView Topscore3;
    public static TextView Topscore4;
    public static TextView Topscore5;
    private AppPreferences _appPrefs;
    private int  Number_of_players;
    public static ArrayList<Players> PlayersList =new ArrayList<>();



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        // gets the the value passed by the calling activity
        Number_of_players= getIntent().getIntExtra("Number_of_players", 1);

        // gets the value passed by the calling activity , the j value +1 allows us to access the usernames and scores in the sharedPreferences
        int j = getIntent().getIntExtra("j_value", 5);
        Log.i("CHECKINGEXTRA", Number_of_players+"");

        _appPrefs=new AppPreferences(this);

        DialogFragment quizResults = Dialog_Fragment.newInstance(_appPrefs.RetrieveUserName(j+4),MainActivityFragment.CorrectOnFirstTry,MainActivityFragment.AccumulatedPoints);
        quizResults.setCancelable(false);
        quizResults.show(getSupportFragmentManager(),"Loading");

       //
      _appPrefs = new AppPreferences(this);


      // Code below used  for resetting shared Preferences-- For programmer use only , sometimes bad data gets into sharedPreferences while testing
        /*for (int i=0 ; i <10; i++){
            _appPrefs.StoreUsername(i, null);
            _appPrefs.StoreScore(i, null);
        }*/
        // Code above used for resetting shared Preferences-- For programmer use only , sometimes bad data gets into sharedPreferences while testing
        for (int i=0 ; i <10; i++){
           String Username= _appPrefs.RetrieveUserName(i);
           String Score=_appPrefs.RetrieveScore(i);

           PlayersList.add(new Players(Username,Score));
           Log.i("BEFORESORT",PlayersList.get(i).toString());// More debugging , verifying outputs

        }

        // Sorts the Scores in Descending Order , and therefore puts the usernames in the right order also since they are stored in one object
        Collections.sort(PlayersList, new Comparator<Players>() {//#compar
            @Override
            //Descending Sort
            public int compare(Players o1, Players o2) {
                return Integer.valueOf(o2.Score).compareTo(o1.Score);
            }
        });



        // This loop puts the Scores and Usernames back in shared preferences in the correct order
        for (int i=0 ; i <10; i++) {

            _appPrefs.StoreScore(i,PlayersList.get(i).GetScore()+"");
            _appPrefs.StoreUsername(i,PlayersList.get(i).GetUserName()+"");
            Log.i("AFTERSORT",PlayersList.get(i).toString());
        }




        //these variables are associated with the text views in the layout/ sorry for the bad id names
        Topscore = (TextView)findViewById(R.id.h1);
        Topscore2 = (TextView)findViewById(R.id.h2);
        Topscore3 = (TextView)findViewById(R.id.h3);
        Topscore4 = (TextView)findViewById(R.id.h4);
        Topscore5 = (TextView)findViewById(R.id.h5);



        Log.i("PASSED-SHAREDPREFERENCE",  _appPrefs.RetrieveScore(1)+" ");// More debugging , verifying outputs

        Log.i("PASSEDUSERNAME",_appPrefs.RetrieveUserName(1)+" "+_appPrefs.RetrieveScore(1) );


        //Checking if the shared preference value is not not before displaying it
        //Displaying  the values in positions 0-4 for of shared preferences
        if ( _appPrefs.RetrieveScore(0)!=null) {
            Topscore.setText(_appPrefs.RetrieveUserName(0) + " " +_appPrefs.RetrieveScore(0)); }

        if ( _appPrefs.RetrieveScore(1)!=null) {
             Topscore2.setText(_appPrefs.RetrieveUserName(1)+" " +_appPrefs.RetrieveScore(1));}

        if ( _appPrefs.RetrieveScore(2)!=null) {
            Topscore3.setText(_appPrefs.RetrieveUserName(2)+" " +_appPrefs.RetrieveScore(2));}

        if ( _appPrefs.RetrieveScore(3)!=null) {
            Topscore4.setText(_appPrefs.RetrieveUserName(3)+" " +_appPrefs.RetrieveScore(3));}

        if ( _appPrefs.RetrieveScore(4)!=null) {
            Topscore5.setText(_appPrefs.RetrieveUserName(4)+" " +_appPrefs.RetrieveScore(4));}



    }

//Onclick listener method that sends us back to the beginning of the game after we are done reviewing the scores
 public void Restart(View buttonView){
     MainActivity.preferencesChanged =true;
     MainActivityFragment.j=1;
     Intent Intent = new Intent(this, WelcomePageActivity.class);
     startActivity(Intent);

 }
   /// subclass that allows us to store usenames and scores in one object
    // these objects are passed to an ArrayList and then  sorted to find the top scores before displaying to the leaderboard
    public class Players {
        public String username;
        public int  Score;

        //I was getting a Number format exception for some reason so I decided to pass the score as a string , then parse it back to and int for sorting purposes

        public Players(String username, String Score) {
            this.username = username;
            this.Score = Integer.parseInt(Score);
        }

        public String toString(){


         String Output = username+" "+Score+" ";
         return Output;

        }
        //This method allows direct access to the username which allows me to format my strings better
        public String GetUserName()
        {

         String Output=this.username;
         return Output;

        }

       //This method allows direct access to the score which allows me to format my strings better
        public int GetScore(){

        return  this.Score;

        }

    }
















}
