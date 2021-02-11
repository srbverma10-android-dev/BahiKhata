package com.sourabhverma.bahikhata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen_Activity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int VISIBLE = 20;

    private BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state){

                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE ON");

                        Toast.makeText(Splash_Screen_Activity.this, "TURNED ON", Toast.LENGTH_SHORT).show();

                        discoverable();

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE TURNING ON");
                        break;
                }

            }

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){

                final  int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.ERROR);

                switch (state){

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive: SCAN MODE CONNECTABLE DISCOVERABLE");
                        Toast.makeText(Splash_Screen_Activity.this, "DISCOVERABLE", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(Splash_Screen_Activity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);

                        break;
                    case  BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: SCAN MODE CONNECTABLE");
                        Toast.makeText(Splash_Screen_Activity.this, "CONNECTABLE", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: SCANMODE NONE");

                        Toast.makeText(Splash_Screen_Activity.this, "NOT VISIBLE", Toast.LENGTH_SHORT).show();

                        break;
                }

            }


        }
    };

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen_);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.d(TAG, "onCreate: SS" );

        if (firebaseUser != null){

            checkPermission();
            enableBlueTooth();

        } else {

            Intent intent = new Intent(Splash_Screen_Activity.this, LogIn_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }


    }

    private void enableBlueTooth() {

        if (bluetoothAdapter == null){
            Log.d(TAG, "NO BLUETOOTH");

            Toast.makeText(Splash_Screen_Activity.this, "THERE IS SOME PROBLEM WITH THE BLUETOOTH",
                    Toast.LENGTH_SHORT).show();

        }

        if (!bluetoothAdapter.isEnabled()){

            Intent enabledBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabledBluetooth);

            IntentFilter BTintent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(receiver,BTintent);
        }

        if (bluetoothAdapter.isEnabled()){

            discoverable();

        }

    }

    private void discoverable() {

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);
        startActivityForResult(discoverableIntent,VISIBLE);


        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(receiver,intentFilter);

    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "checkPermission: ALL PERMISSION GRANTED");

            } else {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "onRequestPermissionsResult:  ALL PERMISSION GRANTED");

        } else {
            checkPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VISIBLE && resultCode == RESULT_FIRST_USER){

            Intent intent1 = new Intent(Splash_Screen_Activity.this, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);

        }

        if (requestCode == VISIBLE && resultCode == RESULT_CANCELED ){

            Toast.makeText(Splash_Screen_Activity.this, "ALLOW IT NEXT TIME FOR BETTER EXPERIENCE...",Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(Splash_Screen_Activity.this, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);

    }
}