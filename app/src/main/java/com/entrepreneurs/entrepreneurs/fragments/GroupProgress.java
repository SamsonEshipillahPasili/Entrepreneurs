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
import com.entrepreneurs.entrepreneurs.entities.Group;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;

public class GroupProgress extends Fragment {

    private OnFragmentInteractionListener mListener;

    public GroupProgress() {
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
        View view = inflater.inflate(R.layout.fragment_group_progress, container, false);

        // update member 1's data
        Group group = (Group) Session.getSession().getAttribute(Constants.GROUP_KEY);

        ((TextView) view.findViewById(R.id.group_name)).setText(group.getName());
        ((TextView) view.findViewById(R.id.group_amount)).setText("Ksh." + group.getAmount());
        ((TextView) view.findViewById(R.id.group_target)).setText("Ksh." + group.getTarget());
        ((TextView) view.findViewById(R.id.group_percentage)).setText(group.getPercentage() + "%");
        ProgressBar progressBar = view.findViewById(R.id.group_progress_bar);
        progressBar.setMax(Integer.parseInt(group.getTarget()));
        progressBar.setProgress(Integer.parseInt(group.getAmount()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setActivityTitle("Group Progress");
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
