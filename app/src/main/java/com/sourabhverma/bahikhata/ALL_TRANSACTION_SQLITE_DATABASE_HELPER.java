package com.sourabhverma.bahikhata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ALL_TRANSACTION_SQLITE_DATABASE_HELPER extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "ALL_TRANSACTION_TABLE";

    public ALL_TRANSACTION_SQLITE_DATABASE_HELPER(@Nullable Context context) {
        super(context, "ALL_TRANSACTION.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, CREDIT TEXT, DEBIT TEXT, DATE_AND_TIME TEXT, CUSTOMER_NAME TEXT , CUSTOMER_DEVICE_ID TEXT) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String credit, String debit, String date_and_time,String customer_name, String customer_device_id){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CREDIT",credit);
        contentValues.put("DEBIT",debit);
        contentValues.put("DATE_AND_TIME",date_and_time);
        contentValues.put("CUSTOMER_NAME",customer_name);
        contentValues.put("CUSTOMER_DEVICE_ID",customer_device_id);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        return result != -1;

    }


    public Cursor getAllData(){

        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("select * from " + TABLE_NAME, null);

    }


}
