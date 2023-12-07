package com.rdotcom.shortlefttaximath;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rdotcom.shortlefttaximath.PaymentDetails;

import java.util.ArrayList;

public class PaymentsListAdapter  extends RecyclerView.Adapter<PaymentsListAdapter.ViewHolder> {

    ArrayList<PaymentDetails> listData;


    public PaymentsListAdapter(ArrayList<PaymentDetails> listData){
        this.listData = listData;
    }

    @NonNull
    @Override
    public PaymentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.payment_list_item, parent, false);
        PaymentsListAdapter.ViewHolder viewHolder = new PaymentsListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentsListAdapter.ViewHolder holder, int position) {

        final PaymentDetails myListData = listData.get(position);
        holder.paymentDetails.setText(listData.get(position).paymentDetails);
        holder.changeDetails.setText(listData.get(position).changeDetails);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView paymentDetails;
        public TextView changeDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paymentDetails = itemView.findViewById(R.id.txtPayment);
            changeDetails = itemView.findViewById(R.id.txtChange);
        }
    }
}
