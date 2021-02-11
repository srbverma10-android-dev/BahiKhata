package com.sourabhverma.bahikhata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Credit_or_Debit_Activity extends AppCompatActivity {

    private static final String TAG = "TAG";

    private String device_name_string;
    private String phone_number_string;
    private String device_address_string;

    private EditText add_amount;


    private String date;

    private ImageView profile_Image;

    private ArrayList<TRANSACTION> results = new ArrayList<>();

    private RecyclerView recyclerView_for_credit_or_debit;

    private Bitmap Profile_Image_Bitmap;

    public Credit_or_Debit credit_or_debit;

    private PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER perticular_customer_sqlite_database_helper;

    private ALL_TRANSACTION_SQLITE_DATABASE_HELPER all_transaction_sqlite_database_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_or__debit_);

        TextView device_name = findViewById(R.id.customer_name);
        TextView phone_number = findViewById(R.id.customer_num);
        profile_Image = findViewById(R.id.profile_Image_credit_debit);
        add_amount = findViewById(R.id.add_amount);
        Calendar calendar = Calendar.getInstance();

        all_transaction_sqlite_database_helper = new ALL_TRANSACTION_SQLITE_DATABASE_HELPER(Credit_or_Debit_Activity.this);

        recyclerView_for_credit_or_debit = findViewById(R.id.recycler_view_credit_or_debit);
        recyclerView_for_credit_or_debit.setHasFixedSize(true);
        recyclerView_for_credit_or_debit.setNestedScrollingEnabled(false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView_for_credit_or_debit.setLayoutManager(mLinearLayoutManager);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());

        Intent intent = getIntent();
        device_name_string = intent.getStringExtra("DEVICE_NAME");
        phone_number_string = intent.getStringExtra("PHONE_NUMBER");
        device_address_string = intent.getStringExtra("DEVICE_ADDRESS");


        if (device_address_string != null) {

            Toast.makeText(Credit_or_Debit_Activity.this,"in if     " + device_address_string,Toast.LENGTH_SHORT).show();

            String database_name_string = device_address_string + ".db";
            perticular_customer_sqlite_database_helper = new PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER(Credit_or_Debit_Activity.this, database_name_string);
        } else {

            Toast.makeText(Credit_or_Debit_Activity.this,"SOME PROBLEM WITH DEVICE ADDRESS AND DATABASE CREATION", Toast.LENGTH_SHORT).show();

        }


        device_name.setText(device_name_string);
        phone_number.setText(phone_number_string);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Credit_or_Debit_Activity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void Credit_button(View view) {

        if (!add_amount.getText().toString().equals("")) {
            boolean is_Inserted = perticular_customer_sqlite_database_helper.insertData(add_amount.getText().toString(),null,date);
            if (is_Inserted){

                boolean is_Inserted2 = all_transaction_sqlite_database_helper.insertData(add_amount.getText().toString(),null,date,device_name_string,device_address_string);
                if (is_Inserted2){
                    Toast.makeText(Credit_or_Debit_Activity.this,"INSERTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Credit_or_Debit_Activity.this,"SOMETHING IS WRONG !!!", Toast.LENGTH_SHORT).show();
                }

                credit_or_debit = new Credit_or_Debit(getDataSet());
                recyclerView_for_credit_or_debit.setAdapter(credit_or_debit);

                Toast.makeText(Credit_or_Debit_Activity.this,"INSERTED", Toast.LENGTH_SHORT).show();
                add_amount.getText().clear();
            } else {
                Toast.makeText(Credit_or_Debit_Activity.this,"SOMETHING IS WRONG !!!", Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        credit_or_debit = new Credit_or_Debit(getDataSet());
        recyclerView_for_credit_or_debit.setAdapter(credit_or_debit);

        ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(Credit_or_Debit_Activity.this);

        Cursor res = all_customer_sqlite_database_helper.searchData(device_address_string);

        if (res != null && res.moveToFirst()){


            byte[] byteArray = res.getBlob(6);

            Profile_Image_Bitmap= BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);

            profile_Image.setImageBitmap(Profile_Image_Bitmap);


        }

    }

    private ArrayList<TRANSACTION> getDataSet() {

        show_devices_in_people_in_your_shop();

        return results;

    }

    private void show_devices_in_people_in_your_shop() {

        results.clear();

        Cursor res = perticular_customer_sqlite_database_helper.getAllData();
        if (res.getCount() == 0){
            Toast.makeText(Credit_or_Debit_Activity.this,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
        }
        //1 == Credit
        //2 == Debit
        //0 == fault

        while (res.moveToNext()){

            Log.d(TAG, "Another: " + res.getString(1)+res.getString(3));

            TRANSACTION transaction = new TRANSACTION(date,res.getString(1),res.getString(2));

            results.add(transaction);

        }

    }


    public void Debit_Button(View view) {

        if (!add_amount.getText().toString().equals("")) {
            boolean is_Inserted = perticular_customer_sqlite_database_helper.insertData(null,add_amount.getText().toString(),date);
            if (is_Inserted){

                boolean is_Inserted2 = all_transaction_sqlite_database_helper.insertData(null,add_amount.getText().toString(),date,device_name_string,device_address_string);
                if (is_Inserted2){
                    Toast.makeText(Credit_or_Debit_Activity.this,"INSERTED2", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Credit_or_Debit_Activity.this,"SOMETHING IS WRONG !!!", Toast.LENGTH_SHORT).show();
                }

                credit_or_debit = new Credit_or_Debit(getDataSet());
                recyclerView_for_credit_or_debit.setAdapter(credit_or_debit);

                Toast.makeText(Credit_or_Debit_Activity.this,"INSERTED", Toast.LENGTH_SHORT).show();
                add_amount.getText().clear();
            } else {
                Toast.makeText(Credit_or_Debit_Activity.this,"SOMETHING IS WRONG !!!", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void Goto_Customer_Profile(View view) {

        Intent intent = new Intent(Credit_or_Debit_Activity.this, Customer_Profile_Activity.class);
        intent.putExtra("NAME", device_name_string);
        intent.putExtra("PHONE_NUM",phone_number_string);
        intent.putExtra("DEVICE_ADDRESS",device_address_string);

        try {
            //Write file
            String filename = "bitmap.png";
            FileOutputStream stream = this.openFileOutput(filename, Add_New_User_Activity.MODE_PRIVATE);
            Profile_Image_Bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            Profile_Image_Bitmap.recycle();

            intent.putExtra("image", filename);

        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(intent);

    }
}