package com.sourabhverma.bahikhata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Credit_or_Debit extends RecyclerView.Adapter<Credit_or_Debit.ViewHolder>  {

    private ArrayList<TRANSACTION> transactions;

    public Credit_or_Debit(ArrayList<TRANSACTION> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public Credit_or_Debit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_or_debit, parent,false);
        return new Credit_or_Debit.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull Credit_or_Debit.ViewHolder holder, int position) {

//        if (transactions.get(position).getType_of_transaction() == 1){
//
//            holder.cardView1.setVisibility(View.VISIBLE);
//            holder.cardView2.setVisibility(View.GONE);
//
//            holder.amount1.setText(transactions.get(position).getAmount());
//            holder.date1.setText(transactions.get(position).getDate_and_time());
//
//        } else if (transactions.get(position).getType_of_transaction() == 2){
//
//            holder.cardView2.setVisibility(View.VISIBLE);
//            holder.cardView1.setVisibility(View.GONE);
//
//            holder.amount2.setText(transactions.get(position).getAmount());
//            holder.date2.setText(transactions.get(position).getDate_and_time());
//
//        }

        String amount_string;
        if (transactions.get(position).getCredit() != null){

            holder.cardView1.setVisibility(View.VISIBLE);
            holder.cardView2.setVisibility(View.GONE);

            amount_string = transactions.get(position).getCredit() + " RS";

            holder.amount1.setText(amount_string);
            holder.date1.setText(transactions.get(position).getDate_and_time());

        } else {

            holder.cardView2.setVisibility(View.VISIBLE);
            holder.cardView1.setVisibility(View.GONE);

            amount_string = transactions.get(position).getDebit() + " RS";

            holder.amount2.setText(amount_string);
            holder.date2.setText(transactions.get(position).getDate_and_time());

        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date1, amount1, date2, amount2;

        private CardView cardView1, cardView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date1 = itemView.findViewById(R.id.date_1);
            date2 = itemView.findViewById(R.id.date_2);

            amount1 = itemView.findViewById(R.id.amount_1);
            amount2 = itemView.findViewById(R.id.amount_2);

            cardView1 = itemView.findViewById(R.id.card_view_1);
            cardView2 = itemView.findViewById(R.id.card_view_2);

        }
    }

}
