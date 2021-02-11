package com.sourabhverma.bahikhata;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class Peoples_In_Shop extends RecyclerView.Adapter<Peoples_In_Shop.ViewHolder> {

    private ArrayList<Bluetooth_Devices> bluetooth_devices;
    private Context context;

    private byte[] profileImage;

    private String phoneNum,user_name;

    private boolean isCreated = false;

    public Peoples_In_Shop(ArrayList<Bluetooth_Devices> bluetooth_devices, Context context) {
        this.bluetooth_devices = bluetooth_devices;
        this.context = context;
    }

    @NonNull
    @Override
    public Peoples_In_Shop.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoples_in_shop, parent,false);
        return new Peoples_In_Shop.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Peoples_In_Shop.ViewHolder holder, final int position) {


        ALL_CUSTOMER_SQLITE_DATABASE_HELPER all_customer_sqlite_database_helper = new ALL_CUSTOMER_SQLITE_DATABASE_HELPER(context);

        final Cursor res = all_customer_sqlite_database_helper.searchData(bluetooth_devices.get(position).getDeviceAddress());

        if (res != null && res.moveToFirst()){

            isCreated = true;

            profileImage = res.getBlob(6);

            Bitmap bmp= BitmapFactory.decodeByteArray(profileImage,0,profileImage.length);
            holder.profile_image_PISI.setImageBitmap(bmp);

            phoneNum = res.getString(3);
            user_name = res.getString(0);

            holder.deviceName_PISI.setText(user_name);
            holder.address_PISI.setText(phoneNum);

        } else {

            isCreated = false;

            Toast.makeText(context,"IN ELSE PEOPLES IN SHOP",Toast.LENGTH_SHORT).show();

            holder.deviceName_PISI.setText(bluetooth_devices.get(position).getDeviceName());
            holder.address_PISI.setText(bluetooth_devices.get(position).getDeviceAddress());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isCreated){

                    Intent intent = new Intent(context, Credit_or_Debit_Activity.class);
                    intent.putExtra("DEVICE_NAME", user_name);
                    intent.putExtra("DEVICE_ADDRESS", bluetooth_devices.get(position).getDeviceAddress());
                    intent.putExtra("PHONE_NUMBER",phoneNum);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);

                    try {
                        //Write file
                        String filename = "bitmap.png";
                        FileOutputStream stream = context.openFileOutput(filename, Add_New_User_Activity.MODE_PRIVATE);
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

                    context.startActivity(intent);

                } else {

                    Intent intent = new Intent(context, Add_New_User_Activity.class);

                    intent.putExtra("device_name", bluetooth_devices.get(position).getDeviceName());
                    intent.putExtra("device_address", bluetooth_devices.get(position).getDeviceAddress());
                    intent.putExtra("FROM","PEOPLES_IN_SHOP");

                    context.startActivity(intent);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return bluetooth_devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile_image_PISI;

        private TextView deviceName_PISI, address_PISI;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image_PISI = itemView.findViewById(R.id.profileImage_PISI);
            deviceName_PISI = itemView.findViewById(R.id.device_name_PISI);
            address_PISI = itemView.findViewById(R.id.address_PISI);

        }
    }

}
