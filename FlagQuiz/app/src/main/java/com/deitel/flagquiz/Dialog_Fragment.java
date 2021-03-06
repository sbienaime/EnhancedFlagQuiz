package com.deitel.flagquiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dialog_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dialog_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
  public class Dialog_Fragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static int guesses;




    private OnFragmentInteractionListener mListener;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////------------------CONSTRUCTOR FROM STACKOVERFLOW-------------////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /// this constructor helps solve the run time error caused by not using the DialogFragment class from a non static context
    // The players information is passed to the dialog fragment in order to give them a summary of their gam play
    // This information is passed through the constructor
    public static Dialog_Fragment newInstance(String player,int Firsts, int score  ) {
        Dialog_Fragment frag = new Dialog_Fragment();
        Bundle args = new Bundle();
        args.putString("player", player);
        args.putInt("firsts", Firsts);
        args.putInt("score", score);
        frag.setArguments(args);
        return frag;

    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String Player = getArguments().getString("player");
        int Score =getArguments().getInt("score");
        int firsts =getArguments().getInt("firsts");

        Log.i("THEVALUEOFTITLE", Player);// Debugging

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage(Player+" You got "+firsts+" questions right on your first try. "+" Your Score is "+ Score +".");


        // "Continue Quiz " Button
        builder.setPositiveButton(R.string.Continue,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                    }


                }
        );

        return builder.create(); // return the AlertDialog


    }



    public Dialog_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.`
     * @return A new instance of fragment Dialog_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Dialog_Fragment newInstance(String param1, String param2) {
        Dialog_Fragment fragment = new Dialog_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    public void onFragmentInteraction(Uri uri){

    }
}
