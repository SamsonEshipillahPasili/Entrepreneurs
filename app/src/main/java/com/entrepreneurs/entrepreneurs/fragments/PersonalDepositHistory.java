package com.entrepreneurs.entrepreneurs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.entities.Group;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.entities.PaymentInformation;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PersonalDepositHistory extends Fragment {
    private List<PaymentInformation> paymentInformationList;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Member focusedMember;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonalDepositHistory() {
        this.paymentInformationList = ((Member)Session
                .getSession()
                .getAttribute(Constants.CURRENT_MEMBER_KEY)).getPayments();
        this.paymentInformationList = this.reversePaymentsOrder((ArrayList<PaymentInformation>) this.paymentInformationList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.getArguments() != null){
            String memberName = this.getArguments().getString(Constants.MEMBER_HISTORY_KEY);
            this.paymentInformationList = ((Group)Session.getSession().getAttribute(Constants.GROUP_KEY))
                    .getMemberByName(memberName).getPayments();
            this.paymentInformationList = this.reversePaymentsOrder((ArrayList<PaymentInformation>) this.paymentInformationList);
            this.focusedMember = ((Group)Session.getSession().getAttribute(Constants.GROUP_KEY))
                    .getMemberByName(memberName);
        }else{
            this.focusedMember = ((Member)Session
                    .getSession()
                    .getAttribute(Constants.CURRENT_MEMBER_KEY));
        }

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView
                    .setAdapter(new PersonalDepositHistoryRecyclerViewAdapter(this.paymentInformationList, mListener));
        }
        mListener.setActivityTitle(this.focusedMember.getName() + "'s History");
        return view;
    }

    private List<PaymentInformation> reversePaymentsOrder(ArrayList<PaymentInformation> param){
        ArrayList<PaymentInformation> tempList = (ArrayList<PaymentInformation>) param.clone();
        Collections.reverse(tempList);
        return tempList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void setActivityTitle(String string);
    }
}
