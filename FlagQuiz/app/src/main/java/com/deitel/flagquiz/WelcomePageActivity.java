package com.deitel.flagquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);








        }

    public void SinglePlayer(View buttonView){



        Intent preferencesIntent = new Intent(this, MainActivity.class);
        startActivity(preferencesIntent);





    }



    public void Multiplayer(View buttonView){



        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);





    }
}
