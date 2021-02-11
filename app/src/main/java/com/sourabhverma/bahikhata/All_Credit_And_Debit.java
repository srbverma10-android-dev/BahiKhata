package com.sourabhverma.bahikhata;

import android.content.Context;
import android.content.Intent;
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

public class All_Credit_And_Debit extends RecyclerView.Adapter<All_Credit_And_Debit.ViewHolder> {

    public All_Credit_And_Debit(ArrayList<CUSTOMER> transaction, Context context) {
        this.transaction = transaction;
        this.context = context;
    }

    private ArrayList<CUSTOMER> transaction;
    private Context context;

    @NonNull
    @Override
    public All_Credit_And_Debit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.records, parent,false);
        return new All_Credit_And_Debit.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Credit_And_Debit.ViewHolder holder, final int position) {

        int TOTAL_CREDIT = 0;
        int TOTAL_DEBIT = 0;
        int NET_BALANCE;

        ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(context);

        holder.nameTextView.setText(transaction.get(position).getNAME());

        holder.dateTextView.setText(transaction.get(position).getPHONE_NUMBER());

        if (transaction.get(position).getDEVICE_ADDRESS() != null){

            String database_name = transaction.get(position).getDEVICE_ADDRESS() + ".db";

            PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER perticular_customer_sqlite_database_helper = new PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER(context, database_name);

            Cursor res = perticular_customer_sqlite_database_helper.getAllData();
            if (res.getCount() == 0){
                Toast.makeText(context,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
            }
            //1 == Credit
            //2 == Debit
            //0 == fault

            while (res.moveToNext()){

                if (res.getString(1) != null) {
                    TOTAL_CREDIT = TOTAL_CREDIT + Integer.parseInt(res.getString(1));
                }
                if (res.getString(2) != null) {
                    TOTAL_DEBIT = TOTAL_DEBIT + Integer.parseInt(res.getString(2));
                }

            }

            NET_BALANCE = TOTAL_CREDIT - TOTAL_DEBIT;

            String to_show_in_textView = NET_BALANCE + " RS";

            holder.amountTextView.setText(to_show_in_textView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Credit_or_Debit_Activity.class);
                intent.putExtra("DEVICE_NAME", transaction.get(position).getNAME());
                intent.putExtra("DEVICE_ADDRESS", transaction.get(position).getDEVICE_ADDRESS());
                intent.putExtra("PHONE_NUMBER",transaction.get(position).getPHONE_NUMBER());

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });

        Cursor res = all_customer_sqlite_database_helper.searchData(transaction.get(position).getDEVICE_ADDRESS());

        if (res != null && res.moveToFirst()){

            byte[] profileImage = res.getBlob(6);

            Bitmap bmp= BitmapFactory.decodeByteArray(profileImage,0, profileImage.length);
            holder.profile_image_PISI.setImageBitmap(bmp);

        } else {
            Toast.makeText(context, "ERROR 404 : NOT FOUND" , Toast.LENGTH_SHORT).show();
        }

        holder.credit_or_debit_imageView.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return transaction.size();
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
