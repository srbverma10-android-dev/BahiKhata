package com.sourabhverma.bahikhata;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Records extends RecyclerView.Adapter<Records.ViewHolder> {

    private ArrayList<ALL_DATA_FOR_RECORDS> transactions;
    private Context context;

    public Records(ArrayList<ALL_DATA_FOR_RECORDS> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public Records.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.records, parent,false);
        return new Records.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull Records.ViewHolder holder, int position) {

        ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(context);

        if (transactions.get(position).getDebit() != null) {
            holder.amountTextView.setText(transactions.get(position).getDebit());

            holder.credit_or_debit_imageView.setImageResource(R.drawable.d);

        } else {
            holder.amountTextView.setText(transactions.get(position).getCredit());

            holder.credit_or_debit_imageView.setImageResource(R.drawable.c);
        }

        holder.dateTextView.setText(transactions.get(position).getDate_And_Time());

        holder.nameTextView.setText(transactions.get(position).getCustomer_Name());

        final Cursor res = all_customer_sqlite_database_helper.searchData(transactions.get(position).getCustomer_Device_ID());

        Toast.makeText(context,transactions.get(position).getCustomer_Device_ID(),Toast.LENGTH_SHORT).show();

        if (res != null && res.moveToFirst()){

            byte[] profileImage = res.getBlob(6);

            Bitmap bmp= BitmapFactory.decodeByteArray(profileImage,0, profileImage.length);
            holder.profile_image_PISI.setImageBitmap(bmp);

        } else {
            Toast.makeText(context,"ERROR 404: NOT FOUND",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile_image_PISI,credit_or_debit_imageView;

        private TextView amountTextView, dateTextView,nameTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image_PISI = itemView.findViewById(R.id.profileImage_records);
            credit_or_debit_imageView = itemView.findViewById(R.id.credit_or_debit_records);
            amountTextView = itemView.findViewById(R.id.amount_records);
            dateTextView = itemView.findViewById(R.id.date_records);
            nameTextView = itemView.findViewById(R.id.name_records);


        }
    }


}
