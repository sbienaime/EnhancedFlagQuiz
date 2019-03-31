// MainActivityFragment.java
// Contains the Flag Quiz logic
package com.deitel.flagquiz;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransitionImpl;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import org.w3c.dom.Text;

public  class  MainActivityFragment extends Fragment{
    public static SharedPreferences prefs;
    public static String Player1;
    public static String Player2;
    public static String Player3;
    public static String Player4;
    public static String Player5;

    // String used when logging error messages
    private static final String TAG = "FlagQuiz Activity";
    int result;
    private static  int FLAGS_IN_QUIZ = 3;
    public static  int NumberOfPlayers;
    private static List<String> fileNameList; // flag file names
    private static List<String> quizCountriesList; // countries in current quiz
    private static Set<String> regionsSet; // world regions in current quiz
    public static String correctAnswer; // correct country for the current flag
    public  static int totalGuesses; // number of guesses made
    private static int correctAnswers; // number of correct guesses
    private static int guessRows; // number of rows displaying guess Buttons
    private static SecureRandom random; // used to randomize the quiz
    private static Handler handler; // used to delay loading next flag
    private static Animation shakeAnimation; // animation for incorrect guess
    public static Context context;
    static int deduction=0;
    static int AvailableAttempts;
    public static int Player1Score;
    public static int Player2Score;
    public static int Player3Score;
    public static int Player4Score;
    public static int Player5Score;
    public static int[] PlayerScores = new int[15];
    public static int j=1; // stops the quiz when i is = to number of players
    static int NumberOfButtons;//#number
    static int PointsPerQuestion;
    public static int AccumulatedPoints;
    static int CorrectOnFirstTry;
    private static ArrayList<Players>PlayersList =new ArrayList();
    public static TextView DisplayScore;//#cp1
    TextView IncreaseFirstTry;
    int CurrentPlayer;
    //public static int NumberofPlayers;
    String[] PlayerNames = new String[10];
    private AppPreferences _appPrefs;
    private static LinearLayout quizLinearLayout; // layout that contains the quiz
    private static TextView questionNumberTextView; // shows current question #
    private static  ImageView flagImageView; // displays a flag
    private static LinearLayout[] guessLinearLayouts; // rows of answer Buttons
    private static TextView answerTextView; // displays correct answer
    boolean  set =false;
    Context MainActivityFragmentContext;
    TextView CurrentPlayerDisplayer;
    public Context getMainActivityFragmentContext() {
        return MainActivityFragmentContext;
    }


    public interface OnFragmentInteractionListener {

    }

    public static void increaseScore() {

    }



 private class Players {
        public String username;
        public int  Score;
        public Players(String username, String Score) {
             this.username = username;
             this.Score = Integer.parseInt(Score);
        }

    }






   // configures the MainActivityFragment when its View is created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view =
         inflater.inflate(R.layout.fragment_main, container, false);

       //#preferences
       MainActivityFragmentContext = getContext();
       _appPrefs = new AppPreferences(MainActivityFragmentContext);


      CurrentPlayerDisplayer=(TextView)view.findViewById(R.id.h10);
      CurrentPlayerDisplayer.setText( _appPrefs.RetrieveUserName(5)+"'s turn");

      fileNameList = new ArrayList<>();
      quizCountriesList = new ArrayList<>();
      random = new SecureRandom();
      handler = new Handler();
       IncreaseFirstTry =  (TextView) view.findViewById(R.id.FirstAttemptsDisplayer);
       DisplayScore =  (TextView) view.findViewById(R.id.ScoreDisplayer);
      // load the shake animation that's used for incorrect answers
      shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
         R.anim.incorrect_shake);
      shakeAnimation.setRepeatCount(3); // animation repeats 3 times

      // get references to GUI components
      quizLinearLayout =
         (LinearLayout) view.findViewById(R.id.quizLinearLayout);
      questionNumberTextView =
         (TextView) view.findViewById(R.id.questionNumberTextView);
      flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
      guessLinearLayouts = new LinearLayout[4];
      guessLinearLayouts[0] =
         (LinearLayout) view.findViewById(R.id.row1LinearLayout);
      guessLinearLayouts[1] =
         (LinearLayout) view.findViewById(R.id.row2LinearLayout);
      guessLinearLayouts[2] =
         (LinearLayout) view.findViewById(R.id.row3LinearLayout);
      guessLinearLayouts[3] =
         (LinearLayout) view.findViewById(R.id.row4LinearLayout);
      answerTextView = (TextView) view.findViewById(R.id.answerTextView);

      // configure listeners for the guess Buttons
      for (LinearLayout row : guessLinearLayouts) {
         for (int column = 0; column < row.getChildCount(); column++) {
            Button button = (Button) row.getChildAt(column);
            button.setOnClickListener(guessButtonListener);
         }
      }

      // set questionNumberTextView's text
      questionNumberTextView.setText(
         getString(R.string.question, 1, FLAGS_IN_QUIZ));
      return view; // return the fragment's view for display
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

      ///* --------------- THESE METHODS USE THE VALUES STORED IN "preference.xml" -----\\\
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






   public static  void updateNumberOfPlayers(SharedPreferences sharedPreferences) {
      String players =
              sharedPreferences.getString(MainActivity.PLAYERS, null);
      NumberOfPlayers = Integer.parseInt(players);
   }
   // update guessRows based on value in SharedPreferences
   public void updateGuessRows(SharedPreferences sharedPreferences) {
      // get the number of guess buttons that should be displayed
      String choices =
         sharedPreferences.getString(MainActivity.CHOICES, null);
      guessRows = Integer.parseInt(choices) / 2;

      AvailableAttempts=guessRows*2;
       NumberOfButtons= guessRows*2;
       //#g123
      // hide all quess button LinearLayouts
      for (LinearLayout layout : guessLinearLayouts)
         layout.setVisibility(View.GONE);

      // display appropriate guess button LinearLayouts
      for (int row = 0; row < guessRows; row++)
         guessLinearLayouts[row].setVisibility(View.VISIBLE);
   }

   // update world regions for quiz based on values in SharedPreferences
   public void updateRegions(SharedPreferences sharedPreferences) {
      regionsSet =
         sharedPreferences.getStringSet(MainActivity.REGIONS, null);
   }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   ///* --------------- THESE METHODS USE THE VALUES STORED IN "preference.xml" ------\\\
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    /// Soft Reset to be used in multiplayer  mode
    public void  SoftresetQuiz() {
        // use AssetManager to get image file names for enabled regions
        AssetManager assets = getActivity().getAssets();
        //fileNameList.clear(); // empty list of image file names
        // reset j back to 1
        DisplayScore.setText("");
        IncreaseFirstTry.setText("");
        try {
            // loop through each region
            for (String region : regionsSet) {
                // get a list of all flag image files in this region
                String[] paths = assets.list(region);

                for (String path : paths)
                    fileNameList.add(path.replace(".png", ""));
            }
        }
        catch (IOException exception) {
            Log.e(TAG, "Error loading image file names", exception);
        }

        correctAnswers = 0; // reset the number of correct answers made
        totalGuesses = 0; // reset the total number of guesses the user made
        quizCountriesList.clear(); // clear prior list of quiz countries
        AccumulatedPoints=0;
        CorrectOnFirstTry=0;
        //public static TextView DisplayScore;//#cp1
        //TextView IncreaseFirstTry;
        //int CurrentPlayer;

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        // add FLAGS_IN_QUIZ random file names to the quizCountriesList
        while (flagCounter <= FLAGS_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfFlags);

            // get the random file name
            String filename = fileNameList.get(randomIndex);

            // if the region is enabled and it hasn't already been chosen
            if (!quizCountriesList.contains(filename)) {
                quizCountriesList.add(filename); // add the file to the list
                ++flagCounter;
            }
        }

        loadNextFlag(); // start the quiz by loading the first flag
    }



   // set up and start the next quiz
   public void resetQuiz() {
        AccumulatedPoints=0;
        j=1;
        CorrectOnFirstTry=0;//#shared pref
       DisplayScore.setText("0");
       IncreaseFirstTry.setText("0");
      // use AssetManager to get image file names for enabled regions
      AssetManager assets = getActivity().getAssets();
      fileNameList.clear(); // empty list of image file names

      try {
         // loop through each region
         for (String region : regionsSet) {
            // get a list of all flag image files in this region
            String[] paths = assets.list(region);

            for (String path : paths)
               fileNameList.add(path.replace(".png", ""));
         }
      }
      catch (IOException exception) {
         Log.e(TAG, "Error loading image file names", exception);
      }

      correctAnswers = 0; // reset the number of correct answers made
      totalGuesses = 0; // reset the total number of guesses the user made
      quizCountriesList.clear(); // clear prior list of quiz countries

      int flagCounter = 1;
      int numberOfFlags = fileNameList.size();

      // add FLAGS_IN_QUIZ random file names to the quizCountriesList
      while (flagCounter <= FLAGS_IN_QUIZ) {
         int randomIndex = random.nextInt(numberOfFlags);

         // get the random file name
         String filename = fileNameList.get(randomIndex);

         // if the region is enabled and it hasn't already been chosen
         if (!quizCountriesList.contains(filename)) {
            quizCountriesList.add(filename); // add the file to the list
            ++flagCounter;
         }
      }

      loadNextFlag(); // start the quiz by loading the first flag
   }


   private  void loadNextFlag() {
        AvailableAttempts=guessRows*2;
      // get file name of the next flag and remove it from the list
      String nextImage = quizCountriesList.remove(0);
      correctAnswer = nextImage; // update the correct answer
      answerTextView.setText(""); // clear answerTextView

      // display current question number
      questionNumberTextView.setText(getString(
         R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ));

      // extract the region from the next image's name
      String region = nextImage.substring(0, nextImage.indexOf('-'));

      // use AssetManager to load next image from assets folder
      AssetManager assets = getActivity().getAssets();

      // get an InputStream to the asset representing the next flag
      // and try to use the InputStream
      try (InputStream stream =
              assets.open(region + "/" + nextImage + ".png")) {
         // load the asset as a Drawable and display on the flagImageView
         Drawable flag = Drawable.createFromStream(stream, nextImage);
         flagImageView.setImageDrawable(flag);

         animate(false); // animate the flag onto the screen
      }
      catch (IOException exception) {
         Log.e(TAG, "Error loading " + nextImage, exception);
      }

      Collections.shuffle(fileNameList); // shuffle file names

      // put the correct answer at the end of fileNameList
      int correct = fileNameList.indexOf(correctAnswer);
      fileNameList.add(fileNameList.remove(correct));//##filenamelist

      // add 2, 4, 6 or 8 guess Buttons based on the value of guessRows
      for (int row = 0; row < guessRows; row++) {
         // place Buttons in currentTableRow
         for (int column = 0;
              column < guessLinearLayouts[row].getChildCount();
              column++) {
            // get reference to Button to configure
            Button newGuessButton =
               (Button) guessLinearLayouts[row].getChildAt(column);
            newGuessButton.setEnabled(true);

            // get country name and set it as newGuessButton's text
            String filename = fileNameList.get((row * 2) + column);
            newGuessButton.setText(getCountryName(filename));
         }
      }

      // randomly replace one Button with the correct answer
      int row = random.nextInt(guessRows); // pick random row
      int column = random.nextInt(2); // pick random column
      LinearLayout randomRow = guessLinearLayouts[row]; // get the row
      String countryName = getCountryName(correctAnswer);
      ((Button) randomRow.getChildAt(column)).setText(countryName);
   }

   // parses the country flag file name and returns the country name
   private static String getCountryName(String name) {
      String  CountryAndCity = name.substring(name.indexOf('-') + 1).replace('_', ' ');
      String  Country = CountryAndCity.substring(0,CountryAndCity.indexOf('-')).replace('_', ' ');
      return Country ;
   }

   // animates the entire quizLinearLayout on or off screen
   private void animate(boolean animateOut) {
      // prevent animation into the the UI for the first flag
      if (correctAnswers == 0)
         return;

      // calculate center x and center y
      int centerX = (quizLinearLayout.getLeft() +
         quizLinearLayout.getRight()) / 2; // calculate center x
      int centerY = (quizLinearLayout.getTop() +
         quizLinearLayout.getBottom()) / 2; // calculate center y

      // calculate animation radius
      int radius = Math.max(quizLinearLayout.getWidth(),
         quizLinearLayout.getHeight());

      Animator animator;

      // if the quizLinearLayout should animate out rather than in
      if (animateOut) {
         // create circular reveal animation
         animator = ViewAnimationUtils.createCircularReveal(
            quizLinearLayout, centerX, centerY, radius, 0);
         animator.addListener(
            new AnimatorListenerAdapter() {
               // called when the animation finishes
               @Override
               public void onAnimationEnd(Animator animation) {
                  loadNextFlag();
               }
            }
         );
      }
      else { // if the quizLinearLayout should animate in
         animator = ViewAnimationUtils.createCircularReveal(
            quizLinearLayout, centerX, centerY, 0, radius);
      }

      animator.setDuration(500); // set animation duration to 500 ms
      animator.start(); // start the animation
   }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///* --------------- THE OnClick LISTENER FOR ALL BUTTONS -------------\\\\\\\
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    private OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {//#buttoncode
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = getCountryName(correctAnswer);
            ++totalGuesses; // increment number of guesses the user has made
            Log.i("NumberOfPlayers", NumberOfPlayers+"");
            PointsPerQuestion=NumberOfButtons*10;
            int pointsGiven;

            if (guess.equals(answer)) { // if the guess is correct
                ++correctAnswers; // increment the number of correct answers



                // display correct answer in green text
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));


// #123
                  if (AvailableAttempts==NumberOfButtons) {
                      Intent intent = new Intent(getContext(), BonusActivity.class);
                    // int code://#last
                      startActivityForResult(intent, 1);
                      onActivityResult(1,1, intent);

                      String hello ="pos";
                      Log.i("PASSED BY INTENT",result+"");
                      Log.i("STORED In Pref",hello);
                      // was used to keep quiz to 10 questions , most likely unecessary due to updates
                     //  ****correctAnswers--;
                      CorrectOnFirstTry++;
                      Log.i( "FirstTry" ,CorrectOnFirstTry + "");
                      deduction=0;
                      AccumulatedPoints = AccumulatedPoints + PointsPerQuestion;
                      Log.i("AccumulatePoints",AccumulatedPoints+"");

                    //  loadBonusFlag();


                      handler.postDelayed(
                              new Runnable() {
                                  @Override
                                  public void run() {


                                      DisplayScore.setText(AccumulatedPoints+"");
                                      IncreaseFirstTry.setText(CorrectOnFirstTry+"");
                                      answerTextView.setTextColor(
                                              getResources().getColor(R.color.correct_answer,
                                                      getContext().getTheme()));

                                  }
                              }, 1000); // 2000 milliseconds for 2-second delay


                  }

                if (AvailableAttempts<NumberOfButtons ) {


                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Log.i( "POintsPerQuestion" ,PointsPerQuestion + "");

                                    int points_Given= PointsPerQuestion - deduction*10;
                                    // stops user from getting points if they have used all attempts
                                    if (points_Given==10){

                                       points_Given=0;

                                        answerTextView.setText(points_Given+" POINTS :( ");
                                        answerTextView.setTextColor(
                                                getResources().getColor(R.color.correct_answer,
                                                        getContext().getTheme()));

                                    }
                                    else
                                    {
                                        AccumulatedPoints = AccumulatedPoints + points_Given ;
                                        DisplayScore.setText(AccumulatedPoints+"");
                                        IncreaseFirstTry.setText(CorrectOnFirstTry+"");
                                        answerTextView.setText(points_Given+" POINTS ");
                                        answerTextView.setTextColor(
                                                getResources().getColor(R.color.correct_answer,
                                                        getContext().getTheme()));

                                        Log.i("AcummulatedPoints ",AccumulatedPoints+"");
                                    }//rest points given and deductions after  correct answer has been given
                                    deduction=0;
                                    points_Given=0;
                                }
                            }, 3000); // 2000 milliseconds for 2-second delay







                }
                disableButtons(); // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == FLAGS_IN_QUIZ) {


                   String CurrentPlayerScore = Integer.toString(AccumulatedPoints);
                    _appPrefs.StoreScore(j+4, CurrentPlayerScore);

                    if ( j == NumberOfPlayers) {



                        Collections.sort(PlayersList, new Comparator<Players>() {//#compar
                            @Override
                            //Descending Sort
                            public int compare(Players o1, Players o2) {
                                return Integer.valueOf(o2.Score).compareTo(o1.Score);
                            }
                        });
                           for (int c= 1; c < NumberOfPlayers ; c++){
                               //#_appPrefs



                           }

                        CorrectOnFirstTry = 0;
                        // DialogFragment to display quiz stats and start new quiz

                        //#lastchange

                         // 2000 milliseconds for 2-second delay

                        Log.i("Won", "Player X won ");

                        Intent intent = new Intent(getContext(), LeaderBoardActivity.class);
                        intent.putExtra("Number_of_players",NumberOfPlayers);
                        intent.putExtra("j_value",j);
                        startActivity(intent);

                                handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        resetQuiz();//// create method that delays reset
                                    }
                                }, 2000);

                    }

                    else {
                        DialogFragment quizResults = Dialog_Fragment.newInstance(_appPrefs.RetrieveUserName(j+4),CorrectOnFirstTry,AccumulatedPoints );
                        quizResults.setCancelable(false);
                        quizResults.show(getFragmentManager(), "Loading");


                        j++;
                        CorrectOnFirstTry=0;
                        AccumulatedPoints=0;
                        CurrentPlayerDisplayer.setText(_appPrefs.RetrieveUserName(j+4)+"'s turn");
                        // keeps track of how many players have played
                        SoftresetQuiz();

                        }

                }
                else { // answer is correct but quiz is not over
                    // load the next flag after a 2-second delay
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    animate(true); // animate the flag off the screen
                                }
                            }, 3500); // 2000 milliseconds for 2-second delay
                }
            } //#1234
            else { // answer was incorrect
                flagImageView.startAnimation(shakeAnimation); // play shake
                ++deduction;
                AvailableAttempts--;



                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                guessButton.setEnabled(false); // disable incorrect answer



                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                answerTextView.setText("-10 POINTS !!!!!");
                                answerTextView.setTextColor(
                                        getResources().getColor(R.color.incorrect_answer,
                                                getContext().getTheme()));

                                handler.postDelayed(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                answerTextView.setText(" ");
                                            }
                                        }, 500); // 2000 milliseconds for 2-second dela


                            }
                        }, 500); // 2000 milliseconds for 2-second dela


            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                 result = data.getIntExtra("pos1",100);
                // do something with the result

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // some stuff that will happen if there's no result
            }
        }
    }


   // utility method that disables all answer Buttons
   private void disableButtons() {
      for (int row = 0; row < guessRows; row++) {
         LinearLayout guessRow = guessLinearLayouts[row];
         for (int i = 0; i < guessRow.getChildCount(); i++)
            guessRow.getChildAt(i).setEnabled(false);
      }
   }



}


/*************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
