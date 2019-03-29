package com.deitel.flagquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Arrays;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {
    public static SharedPreferences ScorePrefs;
    public static TextView Topscore;
    public static TextView Topscore2;
    public static TextView Topscore3;
    public static TextView Topscore4;
    public static TextView Topscore5;
    private AppPreferences _appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        _appPrefs = new AppPreferences(this);
        String someString = _appPrefs.getSmsBody();
           Topscore = (TextView)findViewById(R.id.h1);
        Topscore2 = (TextView)findViewById(R.id.h2);
        Topscore3 = (TextView)findViewById(R.id.h3);
        Topscore4 = (TextView)findViewById(R.id.h4);
        Topscore5 = (TextView)findViewById(R.id.h5);
        Topscore5.setVisibility(View.GONE);



           Arrays.sort(MainActivityFragment.PlayerScores);
        // Topscore.setText(MainActivityFragment.PlayersList.get(0).username+"" +MainActivityFragment.PlayersList.get(0).Score+"");


        if (MainActivityFragment.PlayerScores[0]!=0) {
            Topscore.setText(MainActivityFragment.Player4 + " " + MainActivityFragment.PlayerScores[0] + " ");
        }

        if (MainActivityFragment.Player1Score!=0) {
        Topscore4.setText(MainActivityFragment.Player1+" " + MainActivityFragment.Player1Score+" ");}
        if (MainActivityFragment.Player2Score!=0) {
        Topscore2.setText(MainActivityFragment.Player2+" " + MainActivityFragment.Player2Score+" ");}

        if (MainActivityFragment.PlayerScores[3]!=0) {
        Topscore3.setText(MainActivityFragment.Player3+" " + MainActivityFragment.Player3Score+" ");}
        if (MainActivityFragment.PlayerScores[4]!=0) {
        Topscore5.setText(MainActivityFragment.Player4+" " + MainActivityFragment.Player4Score+" ");}

    }
}
