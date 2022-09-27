package com.example.pcdv0;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.bluettooth.ConnectThread;
import com.example.pcdv0.bluettooth.MyBluetoothService;
import com.example.pcdv0.databinding.ActivityGetLocationBinding;

import java.io.IOException;
import java.util.Set;

@SuppressWarnings({"MissingPermission"})
public class GetLocationActivity extends AppCompatActivity {

    ActivityGetLocationBinding binding;
    private BluetoothAdapter myBluetooth = null;
    BluetoothSocket bluetoothSocket = null;
    static String locate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        Intent intent = getIntent();
        String address = intent.getStringExtra("adress");  // the address of clicked device
        System.out.println("adress: " + address);

        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetLocationActivity.this, UserNavigationDrawerActivity.class);
                startActivity(intent);
            }
        });

        binding.connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothDevice bt : pairedDevices)
                {
                    if (bt.getAddress().toString().equals(address)){
                        ConnectThread connect = new ConnectThread(bt);
                        connect.start();
                        bluetoothSocket = connect.getMmSocket();   //the current socket
                        Toast.makeText(GetLocationActivity.this, "connected to "+bt.getName(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        binding.disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetoothSocket.close();
                    Toast.makeText(GetLocationActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        });

        binding.getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendM("A");
            }
        });




        binding.moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetLocationActivity.this,MapActivity.class);
                intent.putExtra("message", locate);
                startActivity(intent);
            }
        });
    }

    private void sendM(String message) {
        // Check that we're actually connected before trying anything
        MyBluetoothService.ConnectedThread r = new MyBluetoothService.ConnectedThread(bluetoothSocket);
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            r.write(send);
        }
        r.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (r.tempMsg != null) {
                    System.out.println("r.tempMsg "+r.tempMsg);
                    binding.etLocation.setText(r.tempMsg);
                    locate = r.tempMsg;
                }
            }
        }, 1000);   //5 seconds




    }




}