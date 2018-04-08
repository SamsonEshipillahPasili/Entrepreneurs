package com.entrepreneurs.entrepreneurs.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.fragments.PersonalDepositHistory.OnListFragmentInteractionListener;
import com.entrepreneurs.entrepreneurs.entities.PaymentInformation;

import java.util.List;

public class PersonalDepositHistoryRecyclerViewAdapter extends
        RecyclerView.Adapter<PersonalDepositHistoryRecyclerViewAdapter.ViewHolder> {

    private final List<PaymentInformation> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PersonalDepositHistoryRecyclerViewAdapter(List<PaymentInformation> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.amountView.setText("Ksh." + mValues.get(position).getAmount());
        holder.dateView.setText(mValues.get(position).getDate());
        holder.transationIdView.setText(mValues.get(position).getTransactionID());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                //mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView amountView;
        public final TextView dateView;
        public final TextView transationIdView;
        public PaymentInformation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            amountView = view.findViewById(R.id.payment_amount);
            dateView = view.findViewById(R.id.payment_date);
            transationIdView = view.findViewById(R.id.payment_transaction_id);
        }
    }
}
