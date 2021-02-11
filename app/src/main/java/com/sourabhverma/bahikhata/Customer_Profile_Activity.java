package com.sourabhverma.bahikhata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;

public class Customer_Profile_Activity extends AppCompatActivity {

    public static  final String TAG = "TAG";

    private String Name;
    private String Phone_Number;
    private String Address;

    private int TOTAL_CREDIT = 0,TOTAL_DEBIT = 0, NET_BALANCE = 0;

    private TextView net_balance;
    private TextView credit;
    private TextView debit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__profile_);

        ImageView profile_Image = findViewById(R.id.profile_image);
        TextView name = findViewById(R.id.name);
        TextView phone_num = findViewById(R.id.phone_number);
        net_balance = findViewById(R.id.Net_balance);
        credit = findViewById(R.id.Credit);
        debit = findViewById(R.id.Debit);

        Intent intent = getIntent();

        Name = intent.getStringExtra("NAME");
        Phone_Number = intent.getStringExtra("PHONE_NUM");
        Address = intent.getStringExtra("DEVICE_ADDRESS");

        Bitmap profile_Image_Bitmap = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            profile_Image_Bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        profile_Image.setImageBitmap(profile_Image_Bitmap);

        name.setText(Name);
        phone_num.setText(Phone_Number);

        show_data_in_table();

    }

    private void show_data_in_table() {

        if (Address != null){

            String database_name = Address + ".db";

            PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER perticular_customer_sqlite_database_helper = new PERTICULAR_CUSTOMER_SQLITE_DATABASE_HELPER(Customer_Profile_Activity.this, database_name);

            Cursor res = perticular_customer_sqlite_database_helper.getAllData();
            if (res.getCount() == 0){
                Toast.makeText(Customer_Profile_Activity.this,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
            }
            //1 == Credit
            //2 == Debit
            //0 == fault

            while (res.moveToNext()){

                if (res.getString(1) != null) {
                    Log.d(TAG, "show_data_in_table: FOR 1 " + Integer.parseInt(res.getString(1)));

                    TOTAL_CREDIT = TOTAL_CREDIT + Integer.parseInt(res.getString(1));

                }

                if (res.getString(2) != null) {

                    Log.d(TAG, "show_data_in_table: FOR 2 " + Integer.parseInt(res.getString(2)));

                    TOTAL_DEBIT = TOTAL_DEBIT + Integer.parseInt(res.getString(2));

                }

            }

            NET_BALANCE = TOTAL_CREDIT - TOTAL_DEBIT;

            credit.setText(String.valueOf(TOTAL_CREDIT));
            debit.setText(String.valueOf(TOTAL_DEBIT));
            net_balance.setText(String.valueOf(NET_BALANCE));

        }

    }

    public void Open_Whats_App(View view) {

        boolean installed = appInstalledOrNot();
        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (NET_BALANCE < 0) {
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+Phone_Number + "&text="+"Hello " + Name + ", Payment of "  + NET_BALANCE + " RS is pending. Please make payment as soon as possible. THANK YOU" ));
            } else {
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+Phone_Number + "&text="+"Hello " + Name + ", I have your " + NET_BALANCE + " RS , Please collect it from me. THANK YOU"));
            }
            startActivity(intent);
        }else {
            Toast.makeText(Customer_Profile_Activity.this, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean appInstalledOrNot(){
        PackageManager packageManager =getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }

}