package com.sourabhverma.bahikhata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Add_New_User_Activity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText name, phone_number, email_address, device_address_edit_text, device_name_edit_text;

    private ImageView add_new_photo;

    private byte[] byteArray;

    private String date, from;

    private String device_name_string, device_address_string;

    private ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__user_);

        Intent intent = getIntent();

        from = intent.getStringExtra("FROM");

        device_address_edit_text = findViewById(R.id.device_address_edit_text);
        device_name_edit_text = findViewById(R.id.device_name_edit_text);

        name = findViewById(R.id.add_person_name);
        phone_number = findViewById(R.id.add_person_phone_num);
        email_address = findViewById(R.id.add_person_email_id);
        TextView device_name = findViewById(R.id.device_name);
        TextView device_address = findViewById(R.id.device_address);
        add_new_photo = findViewById(R.id.add_new_photo);

        Calendar calendar = Calendar.getInstance();

        if (from.equals("PEOPLES_IN_SHOP")){

            device_name_string = intent.getStringExtra("device_name");
            device_address_string = intent.getStringExtra("device_address");

            device_name.setVisibility(View.VISIBLE);
            device_name.setText(device_name_string);

            device_address.setText(device_address_string);
            device_address.setVisibility(View.VISIBLE);

        } else if (from.equals("MAIN_ACTIVITY")){
            device_address_edit_text.setVisibility(View.VISIBLE);
            device_name_edit_text.setVisibility(View.VISIBLE);
        }


        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = dateFormat.format(calendar.getTime());

        all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(Add_New_User_Activity.this);

    }

    public void chooseFile(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    public void addButton(View view) {


        if (from.equals("MAIN_ACTIVITY")){

            device_name_string = device_name_edit_text.getText().toString();
            device_address_string = device_address_edit_text.getText().toString();

        }


        Log.d(TAG, "addButton: " + device_address_string + device_name_string + name.getText().toString() + phone_number.getText().toString() + email_address.getText().toString());

        if (!phone_number.getText().toString().equals("")
                && !name.getText().toString().equals("")
                && byteArray != null) {

            boolean is_Inserted = all_customer_sqlite_database_helper.insertData(name.getText().toString(),device_name_string,device_address_string,phone_number.getText().toString(),email_address.getText().toString(),date,byteArray);
            if (is_Inserted){

                Intent intent = new Intent(Add_New_User_Activity.this, Credit_or_Debit_Activity.class);
                intent.putExtra("DEVICE_NAME", name.getText().toString());
                intent.putExtra("DEVICE_ADDRESS", device_address_string);
                intent.putExtra("PHONE_NUMBER",phone_number.getText().toString());

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = this.openFileOutput(filename, Add_New_User_Activity.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bitmap.recycle();

                    intent.putExtra("image", filename);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            } else {
                Toast.makeText(Add_New_User_Activity.this,"SOMETHING IS WRONG !!!", Toast.LENGTH_SHORT).show();
            }

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            Uri newImageUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), newImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            add_new_photo.setImageURI(newImageUri);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byteArray = stream.toByteArray();

        }

    }

}