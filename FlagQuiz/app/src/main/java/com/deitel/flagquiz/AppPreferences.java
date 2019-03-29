package com.deitel.flagquiz;
import android.content.SharedPreferences;
import android.content.Context;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceManager;
public class AppPreferences {

    public static final String Player_One_SCORE= "P1_S";

    public static final String KEY_PREFS_SMS_BODY = "sms_body";
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getSmsBody() {
        return _sharedPrefs.getString(KEY_PREFS_SMS_BODY, "");
    }





   /* public void  SaveScores( int  player , in){


        switch
        _prefsEditor.putString(KEY_PREFS_SMS_BODY, text);
        _prefsEditor.commit();




    }*/
   public void updatetextfields( int Num_of_Fields){

       _prefsEditor.putInt("Num_of_fields",Num_of_Fields);
       _prefsEditor.commit();




   }


    public void saveSmsBody(String text) {
        _prefsEditor.putString(KEY_PREFS_SMS_BODY, text);
        _prefsEditor.commit();
    }

}
