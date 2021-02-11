package com.sourabhverma.bahikhata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView credit;
    private TextView debit;
    private TextView net_balance;
    public static String uid;


    public static final String TAG = "TAG";
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSION_REQUEST_RECEIVE_SMS = 0;
    private static final int MY_PERMISSION_REQUEST_READ_CALL_LOG = 3;

    private RecyclerView recyclerViewForMain;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<Bluetooth_Devices> results = new ArrayList<>();
    private Peoples_In_Shop Peoples_In_Shop;

    private ALL_TRANSACTION_SQLITE_DATABASE_HELPER all_transaction_sqlite_database_helper;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                assert device != null;
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();

                Log.d(TAG, "onReceive: " + deviceName);

                Bluetooth_Devices bluetoothDevice;

                bluetoothDevice = new Bluetooth_Devices(deviceName,deviceHardwareAddress);

                if (results.size() == 0){
                    results.add(bluetoothDevice);
                }

                for (int i = 0 ; i < results.size() ; i++ ){

                    if (!results.get(i).getDeviceAddress().equals(deviceHardwareAddress)){

                        if (i == results.size()-1){

                            results.add(bluetoothDevice);

                        }

                    }

                }

                Peoples_In_Shop.notifyDataSetChanged();

            }

        }
    };


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.d(TAG, "onCreate: Main");

        //Initialization
        TextView user_name = findViewById(R.id.user_name);
        TextView todays_date = findViewById(R.id.todays_date);
        //Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        ImageView profile_Image = findViewById(R.id.profileImage);
        assert firebaseUser != null;
        uid = firebaseUser.getUid();

        credit = findViewById(R.id.credit_main);
        debit = findViewById(R.id.debit_main);
        net_balance = findViewById(R.id.net_balance_main);

        all_transaction_sqlite_database_helper = new ALL_TRANSACTION_SQLITE_DATABASE_HELPER(MainActivity.this);

        //RECYCLER VIEW
        recyclerViewForMain = findViewById(R.id.recycler_view_main);
        recyclerViewForMain.setHasFixedSize(true);
        recyclerViewForMain.setNestedScrollingEnabled(false);

        user_name.setText(firebaseUser.getDisplayName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        String date = dateFormat.format(calendar.getTime());
        todays_date.setText(date);


        try {
            Bitmap bitmap = Cache_Image_Manager.getImage(MainActivity.this, uid);

            if (bitmap == null){
                try {

                    DownLoadImageTask downLoadImageTask = new DownLoadImageTask(profile_Image,MainActivity.this);
                    downLoadImageTask.execute(Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                profile_Image.setImageBitmap(bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void show_data_in_MAIN_TABLE() {

        int TOTAL_CREDIT = 0;
        int TOTAL_DEBIT = 0;
        int NET_BALANCE;

        Cursor res = all_transaction_sqlite_database_helper.getAllData();
        if (res.getCount() == 0){
            Toast.makeText(MainActivity.this,"NOTHING FOUND", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_RECEIVE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "THANK YOU FOR PERMITTING", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "PLEASE GIVE US PERMISSION FOR BETTER PERFORMANCE", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Peoples_In_Shop = new Peoples_In_Shop(getDataSet(),MainActivity.this);
        recyclerViewForMain.setAdapter(Peoples_In_Shop);
        recyclerViewForMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        show_data_in_MAIN_TABLE();

    }



    private ArrayList<Bluetooth_Devices> getDataSet(){

        show_devices_in_people_in_your_shop();

        return results;

    }

    private void show_devices_in_people_in_your_shop() {



        if (checkCoarseLocationPermission()) {
            bluetoothAdapter.startDiscovery();

            IntentFilter startDiscovery = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver,startDiscovery);

        } else {
            Log.d(TAG, "show_devices_in_people_in_your_shop:  in else");
        }

    }

    private boolean checkCoarseLocationPermission() {
        //checks all needed permissions
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        }else{
            return true;
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter.isDiscovering()){

            bluetoothAdapter.cancelDiscovery();

        }

        unregisterReceiver(broadcastReceiver);

    }

    public void Records_Main(View view) {

        Intent intent = new Intent(MainActivity.this, Records_Activity.class);
        startActivity(intent);

    }

    public void All_Credit_And_Debit(View view) {

        Intent intent = new Intent(MainActivity.this, All_Credit_And_Debit_Activity.class);
        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void floating_button(View view) {
        Intent intent = new Intent(MainActivity.this, Add_New_User_Activity.class);
        intent.putExtra("FROM", "MAIN_ACTIVITY");
        startActivity(intent);
    }

    public static class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {

        public DownLoadImageTask(ImageView image, Context context) {
            this.image = image;
            this.context = context;
        }

        @SuppressLint("StaticFieldLeak")
        private ImageView image;
        @SuppressLint("StaticFieldLeak")
        private Context context;

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;

            String string = strings[0];

            InputStream inputStream;

            try {
                URL imageURl = new URL(string);
                inputStream = (InputStream)imageURl.getContent();
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            image.setImageBitmap(bitmap);

            Cache_Image_Manager.setImage(context,uid,bitmap);


        }
    }



}