package com.sourabhverma.bahikhata;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class All_Credit_And_Debit_Activity extends AppCompatActivity {

    private EditText customer_name_edit_text;

    private RecyclerView recyclerView;

    private All_Credit_And_Debit all_credit_and_debit;
    private ArrayList<CUSTOMER> results = new ArrayList<>();
    private ArrayList<CUSTOMER> results2 = new ArrayList<>();

    private ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__credit__and__debit_);

        customer_name_edit_text = findViewById(R.id.search_edit_text_all);
        ImageButton search_button = findViewById(R.id.search_button_all);

        recyclerView = findViewById(R.id.recycler_view_all_credit_and_Debit);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(All_Credit_And_Debit_Activity.this);

        all_credit_and_debit = new All_Credit_And_Debit(getDataSet(),All_Credit_And_Debit_Activity.this);
        recyclerView.setAdapter(all_credit_and_debit);
        recyclerView.setLayoutManager(new LinearLayoutManager(All_Credit_And_Debit_Activity.this));

    }

    private ArrayList<CUSTOMER> getDataSet() {

        show_devices_in_people_in_your_shop();

        return results;

    }

    private void show_devices_in_people_in_your_shop() {

        results.clear();

        Cursor res = all_customer_sqlite_database_helper.getAllData();
        if (res.getCount() == 0){
            Toast.makeText(All_Credit_And_Debit_Activity.this,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
        }
        //1 == Credit
        //2 == Debit
        //0 == fault

        while (res.moveToNext()){

            CUSTOMER customer = new CUSTOMER(res.getString(0),res.getString(3),res.getString(2));

            results.add(customer);

        }


    }



    public void serach_button_all_credit_and_debit_activity(View view) {

        Toast.makeText(All_Credit_And_Debit_Activity.this,"TAPPED", Toast.LENGTH_SHORT).show();

        results2.clear();

        for (int i = 0 ; i<results.size() ; i++){

            CUSTOMER customer = results.get(i);

            String searched_name = customer_name_edit_text.getText().toString();

            if (customer.getNAME().equals(searched_name)){

                results2.add(customer);

            }

        }

        all_credit_and_debit = new All_Credit_And_Debit(results2,All_Credit_And_Debit_Activity.this);
        recyclerView.setAdapter(all_credit_and_debit);
        recyclerView.setLayoutManager(new LinearLayoutManager(All_Credit_And_Debit_Activity.this));

    }



}