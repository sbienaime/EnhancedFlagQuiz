package com.deitel.flagquiz;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceManager;
public class AppPreferences {

    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;




    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();

    }





    public class Players {
        public String username;
        public int  Score;
        public Players(String username, String Score) {
            this.username = username;
            this.Score = Integer.parseInt(Score);
        }

    }


    public void StoreUsername( int User_Number,String username) {
        _prefsEditor.putString("user"+User_Number, username);
        _prefsEditor.commit();
    }

    public String RetrieveUserName( int User_Number) {

        String username=_sharedPrefs.getString("user"+User_Number, "Unknown");
        return username;
    }





    public String RetrieveScore(int Score_Number) {
        String score=_sharedPrefs.getString("score"+Score_Number, "0");
      return score;
    }



    public void StoreScore( int Score_Number,String score ) {
        _prefsEditor.putString("score"+Score_Number, score);
        _prefsEditor.commit();
    }




}
