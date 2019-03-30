package com.deitel.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Output;
import android.support.design.shape.ShapePath;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Arrays;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {
    public static SharedPreferences ScorePrefs;
    public static TextView Topscore;
    public static TextView Topscore2;
    public static TextView Topscore3;
    public static TextView Topscore4;
    public static TextView Topscore5;
    private AppPreferences _appPrefs;
    private int  Number_of_players;
    public static ArrayList<Players> PlayersList =new ArrayList<>();


    public Context getPrefContext( Context context){

      return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        //Intent intent =new Intent();
        Number_of_players= getIntent().getIntExtra("Number_of_players", 1);
        Log.i("CHECKINGEXTRA", Number_of_players+"");


       //
      _appPrefs = new AppPreferences(this);
      //Code for resetting shared Preferences
        /*for (int i=0 ; i <10; i++){
            _appPrefs.StoreUsername(i, null);
            _appPrefs.StoreScore(i, null);

        }*/
        for (int i=0 ; i <10; i++){
           String Username= _appPrefs.RetrieveUserName(i);
           String Score=_appPrefs.RetrieveScore(i);

           PlayersList.add(new Players(Username,Score));
           Log.i("BEFORESORT",PlayersList.get(i).toString());

        }

        // Sort the Scores in Ascending Order
        Collections.sort(PlayersList, new Comparator<Players>() {//#compar
            @Override
            //Descending Sort
            public int compare(Players o1, Players o2) {
                return Integer.valueOf(o2.Score).compareTo(o1.Score);
            }
        });

        for (int i=0 ; i <10; i++) {

            _appPrefs.StoreScore(i,PlayersList.get(i).GetScore()+"");
            _appPrefs.StoreUsername(i,PlayersList.get(i).GetUserName()+"");
            Log.i("AFTERSORT",PlayersList.get(i).toString());
        }





        Topscore = (TextView)findViewById(R.id.h1);
        Topscore2 = (TextView)findViewById(R.id.h2);
        Topscore3 = (TextView)findViewById(R.id.h3);
        Topscore4 = (TextView)findViewById(R.id.h4);
        Topscore5 = (TextView)findViewById(R.id.h5);




          // Arrays.sort(MainActivityFragment.PlayerScores);
        // Topscore.setText(MainActivityFragment.PlayersList.get(0).username+"" +MainActivityFragment.PlayersList.get(0).Score+"");

        Log.i("PASSED-SHAREDPREFERENCE",  _appPrefs.RetrieveScore(1)+" ");

        Log.i("PASSEDUSERNAME",_appPrefs.RetrieveUserName(1)+" "+_appPrefs.RetrieveScore(1) );












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




    public class Players {
        public String username;
        public int  Score;


        public Players(String username, String Score) {
            this.username = username;
            this.Score = Integer.parseInt(Score);
        }

        public String toString(){


         String Output = username+" "+Score+" ";
         return Output;

        }

        public String GetUserName()
        {

         String Output=this.username;
         return Output;

        }


        public int GetScore(){

        return  this.Score;

        }

    }
















}
