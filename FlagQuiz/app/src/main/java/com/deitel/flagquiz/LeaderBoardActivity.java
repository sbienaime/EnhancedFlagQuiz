package com.deitel.flagquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LeaderBoardActivity extends AppCompatActivity {
    public static TextView Topscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
     Topscore = (TextView)findViewById(R.id.h1);
     Topscore.setText(MainActivityFragment.PlayersList.get(0).username+"" +MainActivityFragment.PlayersList.get(0).Score+"");
    }
}
