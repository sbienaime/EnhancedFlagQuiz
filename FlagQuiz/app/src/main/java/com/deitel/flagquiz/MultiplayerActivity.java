package com.deitel.flagquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MultiplayerActivity extends AppCompatActivity {



    public static EditText Username1 ;
    public static EditText Username2 ;
    public static EditText Username3 ;
    public static EditText Username4 ;
    public static EditText Username5 ;


    public static int[] PlayerScores = new int[15];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        Username1 = (EditText)findViewById(R.id.player1edt);
        Username2 = (EditText)findViewById(R.id.player2edt);
        Username3 = (EditText)findViewById(R.id.player3edt);
        Username4 = (EditText)findViewById(R.id.player4edt);
        Username5= (EditText)findViewById(R.id.player4edt);


    }


     static public void setUserNames() {

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
