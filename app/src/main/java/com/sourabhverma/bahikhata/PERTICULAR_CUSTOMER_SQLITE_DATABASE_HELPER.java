package com.sourabhverma.bahikhata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "PERTICULAR_CUSTOMER";

    public PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER(@Nullable Context context, String name) {
        super(context, name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, CREDIT TEXT, DEBIT TEXT, DATE_AND_TIME TEXT, TOTAL TEXT) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String credit, String debit, String date_and_time){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CREDIT",credit);
        contentValues.put("DEBIT",debit);
        contentValues.put("DATE_AND_TIME",date_and_time);
        contentValues.put("TOTAL","913");
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        return result != -1;

    }

    public Cursor getAllData(){

        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("select * from " + TABLE_NAME, null);

    }


}
