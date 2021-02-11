package com.sourabhverma.bahikhata;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Records_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<ALL_DATA_FOR_RECORDS> results = new ArrayList<>();

    private ALL_TRANSACTION_SQLITE_DATABASE_HELPER all_transaction_sqlite_database_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_);

        recyclerView = findViewById(R.id.recycler_view_records);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();

        all_transaction_sqlite_database_helper = new ALL_TRANSACTION_SQLITE_DATABASE_HELPER(Records_Activity.this);

        Records records = new Records(getDataSet(), Records_Activity.this);
        recyclerView.setAdapter(records);
    }

    private ArrayList<ALL_DATA_FOR_RECORDS> getDataSet() {
        show_devices_in_people_in_your_shop();
        return results;
    }

    private void show_devices_in_people_in_your_shop() {

        results.clear();

        Cursor res = all_transaction_sqlite_database_helper.getAllData();
        if (res.getCount() == 0){
            Toast.makeText(Records_Activity.this,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
        }

        while (res.moveToNext()){
            ALL_DATA_FOR_RECORDS all_data_for_records = new ALL_DATA_FOR_RECORDS(res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5));
            results.add(all_data_for_records);
        }

    }
}