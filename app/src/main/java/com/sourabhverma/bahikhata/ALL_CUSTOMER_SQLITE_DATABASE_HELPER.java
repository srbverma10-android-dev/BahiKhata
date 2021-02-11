package com.sourabhverma.bahikhata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ALL_CUSTOMER_SQLITE_DATABASE_HELPER extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ALL_CUSTOMER.db";
    public static final String TABLE_NAME = "ALL_CUTOMER_TABLE";


    public ALL_CUSTOMER_SQLITE_DATABASE_HELPER(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (USER_NAME TEXT, DEVICE_NAME TEXT, DEVICE_ADDRESS TEXT, PHONE_NUMBER TEXT, EMAIL_ADDRESS TEXT, DATE TEXT, PROFILE_IMAGE BLOB) " );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String user_name, String device_name, String device_address, String phone_number, String email_address, String date,  byte[] profile_Image){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_NAME",user_name);
        contentValues.put("DEVICE_NAME",device_name);
        contentValues.put("DEVICE_ADDRESS",device_address);
        contentValues.put("PHONE_NUMBER",phone_number);
        contentValues.put("EMAIL_ADDRESS",email_address);
        contentValues.put("DATE",date);
        contentValues.put("PROFILE_IMAGE",profile_Image);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        return result != -1;

    }

    public Cursor searchData(String address){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.query(TABLE_NAME,new String[]{"USER_NAME","DEVICE_NAME","DEVICE_ADDRESS","PHONE_NUMBER","EMAIL_ADDRESS","DATE","PROFILE_IMAGE"},"DEVICE_ADDRESS=?", new String[]{address},null,null,null);

        if (res != null){

            res.moveToFirst();
            return res;

        } else {
            return null;
        }

    }

//    public Cursor searchDataByName(String name){
//
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        Cursor res = sqLiteDatabase.query(TABLE_NAME,new String[]{"USER_NAME","DEVICE_NAME","DEVICE_ADDRESS","PHONE_NUMBER","EMAIL_ADDRESS","DATE","PROFILE_IMAGE"},"USER_NAME=?", new String[]{name},null,null,null);
//
//        if (res != null){
//
//            res.moveToFirst();
//            return res;
//
//        } else {
//            return null;
//        }
//
//    }


    public Cursor getAllData(){

        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("select * from " + TABLE_NAME, null);

    }


}
