package com.entrepreneurs.entrepreneurs.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;

public class PersonalProgress extends Fragment {
    private OnFragmentInteractionListener mListener;

    public PersonalProgress() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_progress, container, false);
        // get the current member
        Member member = (Member) Session.getSession().getAttribute(Constants.CURRENT_MEMBER_KEY);
        ((TextView)view.findViewById(R.id.personal_progress_name)).setText(member.getName());
        ((TextView)view.findViewById(R.id.personal_progress_amount)).setText("Ksh." + member.getAmount());
        ((TextView)view.findViewById(R.id.personal_progress_target)).setText("Ksh." + member.getTarget());
        ((TextView)view.findViewById(R.id.personal_progress_percentage)).setText(member.getPercentage() + "%");
        ProgressBar progressBar = view.findViewById(R.id.personal_progress_progress_bar);
        progressBar.setMax(Integer.parseInt(member.getTarget()));
        progressBar.setProgress(Integer.parseInt(member.getAmount()));
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setActivityTitle("My Progress");
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
       void setActivityTitle(String title);
    }
}
