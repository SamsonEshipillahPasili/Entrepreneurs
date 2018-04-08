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

public class MembersProgress extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MembersProgress() {
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
        View view =  inflater.inflate(R.layout.fragment_members_progress, container, false);
        // update members data
        this.updateMembersData(view);
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.setActivityTitle("Member's Progress");
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

    private void updateMembersData(View view){
        // update member 1's data
        Member member1 = ((Group)Session.getSession().getAttribute(Constants.GROUP_KEY))
                .getMemberByName(Constants.MEMBER_1);
        ((TextView) view.findViewById(R.id.member_1_name)).setText(member1.getName());
        ((TextView) view.findViewById(R.id.member_1_amount)).setText("Ksh." + member1.getAmount());
        ((TextView) view.findViewById(R.id.member_1_target)).setText("Ksh." + member1.getTarget());
        ((TextView) view.findViewById(R.id.member_1_percentage)).setText(member1.getPercentage() + "%");
        ProgressBar progressBar1 = view.findViewById(R.id.member_1_progress_bar);
        progressBar1.setMax(Integer.parseInt(member1.getTarget()));
        progressBar1.setProgress(Integer.parseInt(member1.getAmount()));

        // update member 2's data
        Member member2 = ((Group)Session.getSession().getAttribute(Constants.GROUP_KEY))
                .getMemberByName(Constants.MEMBER_2);
        ((TextView) view.findViewById(R.id.member_2_name)).setText(member2.getName());
        ((TextView) view.findViewById(R.id.member_2_amount)).setText("Ksh." + member2.getAmount());
        ((TextView) view.findViewById(R.id.member_2_target)).setText("Ksh." + member2.getTarget());
        ((TextView) view.findViewById(R.id.member_2_percentage)).setText(member2.getPercentage() + "%");
        ProgressBar progressBar2 = view.findViewById(R.id.member_2_progress_bar);
        progressBar2.setMax(Integer.parseInt(member2.getTarget()));
        progressBar2.setProgress(Integer.parseInt(member2.getAmount()));

        // update member 3's data
        Member member3 = ((Group)Session.getSession().getAttribute(Constants.GROUP_KEY))
                .getMemberByName(Constants.MEMBER_3);
        ((TextView) view.findViewById(R.id.member_3_name)).setText(member3.getName());
        ((TextView) view.findViewById(R.id.member_3_amount)).setText("Ksh." + member3.getAmount());
        ((TextView) view.findViewById(R.id.member_3_target)).setText("Ksh." + member3.getTarget());
        ((TextView) view.findViewById(R.id.member_3_percentage)).setText(member3.getPercentage() + "%");
        ProgressBar progressBar3 = view.findViewById(R.id.member_3_progress_bar);
        progressBar3.setMax(Integer.parseInt(member3.getTarget()));
        progressBar3.setProgress(Integer.parseInt(member3.getAmount()));
    }
}
