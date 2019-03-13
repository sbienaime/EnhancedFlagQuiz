// MainActivityFragment.java
// Contains the Flag Quiz logic
package com.deitel.flagquiz;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
public  class  MainActivityFragment extends Fragment{
   // String used when logging error messages
   private static final String TAG = "FlagQuiz Activity";

   private static  int FLAGS_IN_QUIZ = 10;
   private static  int NumberOfPlayers;
   private static List<String> fileNameList; // flag file names
   private static List<String> quizCountriesList; // countries in current quiz
   private static Set<String> regionsSet; // world regions in current quiz
   private static String correctAnswer; // correct country for the current flag
   public  static int totalGuesses; // number of guesses made
   private static int correctAnswers; // number of correct guesses
   private static int guessRows; // number of rows displaying guess Buttons
   private static SecureRandom random; // used to randomize the quiz
   private static Handler handler; // used to delay loading next flag
   private static Animation shakeAnimation; // animation for incorrect guess
    public static Context context;
     static int deduction=0;
     static int AvailableAttempts;
      static int NumberOfButtons;
      static int PointsPerQuestion;
     static int AccumulatedPoints;
    static int CorrectOnFirstTry; //#cp1
    int CurrentPlayer;
    int NumberofPlayers;
    String[] PlayerNames = new String[10];

    private static LinearLayout quizLinearLayout; // layout that contains the quiz
   private static TextView questionNumberTextView; // shows current question #
   private static  ImageView flagImageView; // displays a flag
   private static LinearLayout[] guessLinearLayouts; // rows of answer Buttons
   private static TextView answerTextView; // displays correct answer
    boolean  set =false;
    public interface OnFragmentInteractionListener {

    }


   // configures the MainActivityFragment when its View is created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view =
         inflater.inflate(R.layout.fragment_main, container, false);

      fileNameList = new ArrayList<>();
      quizCountriesList = new ArrayList<>();
      random = new SecureRandom();
      handler = new Handler();

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






   public void updateNumberOfPlayers(SharedPreferences sharedPreferences) {
      String choices =
              sharedPreferences.getString(MainActivity.PLAYERS, null);
      NumberOfPlayers = Integer.parseInt(choices) / 2;
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



   // set up and start the next quiz
   public void resetQuiz() {
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


    private  void loadBonus() {
        AvailableAttempts=guessRows*2;
        // get file name of the next flag and remove it from the list
        String nextImage = quizCountriesList.remove(0);
        correctAnswer = nextImage; // update the correct answer
        answerTextView.setText(""); // clear answerTextView

        // display current question number
        questionNumberTextView.setText(" Bonus Question");

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
        fileNameList.add(fileNameList.remove(correct));

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




   // after the user guesses a correct flag, load the next flag




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
      fileNameList.add(fileNameList.remove(correct));

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
      return name.substring(name.indexOf('-') + 1).replace('_', ' ');
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
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = getCountryName(correctAnswer);
            ++totalGuesses; // increment number of guesses the user has made

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

                      CorrectOnFirstTry++;
                      Log.i( "FirstTry" ,CorrectOnFirstTry + "");
                      deduction=0;
                      AccumulatedPoints = AccumulatedPoints + PointsPerQuestion;
                      Log.i("CompletePoints",AccumulatedPoints+"");
                      handler.postDelayed(
                              new Runnable() {
                                  @Override
                                  public void run() {


                                      answerTextView.setText(R.string.BonusPoints);
                                      answerTextView.setTextColor(
                                              getResources().getColor(R.color.correct_answer,
                                                      getContext().getTheme()));

                                  }
                              }, 3000); // 2000 milliseconds for 2-second delay


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

                                    }
                                    else
                                    {
                                        AccumulatedPoints = AccumulatedPoints + points_Given ;

                                        Log.i("PartialPoints",AccumulatedPoints+"");
                                    }
                                    deduction=0;
                                    answerTextView.setText(points_Given+" POINTS :( ");
                                    answerTextView.setTextColor(
                                            getResources().getColor(R.color.correct_answer,
                                                    getContext().getTheme()));
                                                points_Given=0;
                                }
                            }, 3000); // 2000 milliseconds for 2-second delay







                }
                disableButtons(); // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == FLAGS_IN_QUIZ) {
                    CorrectOnFirstTry=0;
                    // DialogFragment to display quiz stats and start new quiz

                   //#lastchange
                    DialogFragment quizResults = Dialog_Fragment.newInstance(1, totalGuesses);
                    quizResults.setCancelable(false);
                    quizResults.show(getFragmentManager(), "restarting_quiz");
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {

                                    resetQuiz();//// create method that delays reset
                                }
                            }, 5000); // 2000 milliseconds for 2-second delay



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

   /*//#btn called when a guess Button is touched


   private OnClickListener guessButtonListener = new OnClickListener() {
      @Override
      public void onClick(View v) {




         Button guessButton = ((Button) v);
         String guess = guessButton.getText().toString();
         String answer = getCountryName(correctAnswer);
         ++totalGuesses; // increment number of guesses the user has made

        // set the preferences to




         if (guess.equals(answer) && AvailableAttempts== NumberOfButtons) { // if the guess is correct
            ++correctAnswers; // increment the number of correct answers
             answerTextView.setText(R.string.BonusPoints);
             answerTextView.setTextColor(
                     getResources().getColor(R.color.correct_answer,
                             getContext().getTheme()));

            /* if( AvailableAttempts == NumberOfButtons){
                 CorrectOnFirstTry++;
                 AccumulatedPoints = PointsPerQuestion + 10;


                 handler.postDelayed(
                         new Runnable() {
                             @Override
                             public void run() {


                             }
                         }, 2000); // 2000 milliseconds for 2-second delay


                 // display correct answer in green text*/




             /*}
// Check if this is the users first attempt and if they still have attempts
              if (AvailableAttempts < NumberOfButtons  && AvailableAttempts > 1)
             {   int PointsAlotted  =  PointsPerQuestion - 10 * (NumberOfButtons-AvailableAttempts);



                 handler.postDelayed(
                         new Runnable() {
                             @Override
                             public void run() {


                             }
                         }, 2000); // 2000 milliseconds for 2-second delay

                 /// Add to their total points
                      AccumulatedPoints += PointsAlotted;
                 // display correct answer in green text
                 answerTextView.setText(R.string.PointsAllocated);
                 answerTextView.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));


                 handler.postDelayed(
                         new Runnable() {
                             @Override
                             public void run() {


             }
         }, 2000); // 2000 milliseconds for 2-second delay

             }


             if (AvailableAttempts  == 1  )

                 {


                     AvailableAttempts=NumberOfButtons;
                     answerTextView.setText( " 0 POINTS  :( !!!");
                     answerTextView.setTextColor(
                             getResources().getColor(R.color.correct_answer,
                                     getContext().getTheme()));

                     handler.postDelayed(
                             new Runnable() {
                                 @Override
                                 public void run() {




                                 }
                             }, 2000); // 2000 milliseconds for 2-second delay



                 }










                 // display correct answer in green text
            answerTextView.setText(answer + "!");
            answerTextView.setTextColor(
               getResources().getColor(R.color.correct_answer,
                  getContext().getTheme()));

            disableButtons(); // disable all guess Buttons

            // if the user has correctly identified FLAGS_IN_QUIZ flags
            if (correctAnswers == FLAGS_IN_QUIZ) {

                // display correct answer in green text
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));





                    /*DialogFragment n = */
               // DialogFragment to display quiz stats and start new quiz
              /* DialogFragment quizResults =
                  new DialogFragment() {
                     // create an AlertDialog and return it
                     @Override
                     public Dialog onCreateDialog(Bundle bundle) {
                        AlertDialog.Builder builder =
                           new AlertDialog.Builder(getActivity());
                        builder.setMessage(
                           getString(R.string.results,
                              totalGuesses,
                              (1000 / (double) totalGuesses)));

                        // "Reset Quiz" Button
                        builder.setPositiveButton(R.string.reset_quiz,
                           new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog,
                                 int id) {
                                 resetQuiz();
                              }
                           }
                        );

                        return builder.create(); // return the AlertDialog
                     }
                  };
               // use FragmentManager to display the DialogFragment
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));

                 DialogFragment quizResults = Dialog_Fragment.newInstance(1, totalGuesses);
                 quizResults.setCancelable(false);
               quizResults.show(getFragmentManager(), "restarting_quiz");

                if ( CurrentPlayer <= NumberofPlayers )
                {

                    // Do not reset flags list
                    SoftresetQuiz();

                }
                else {
                    resetQuiz();
                }
            }*/

              /*{ // answer is correct but quiz is not over
               // load the next flag after a 2-second delay
               handler.postDelayed(
                  new Runnable() {
                     @Override
                     public void run() {
                        animate(true); // animate the flag off the screen
                     }
                  }, 2000); // 2000 milliseconds for 2-second delay
            }
         }*/
// #endofcomment
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
