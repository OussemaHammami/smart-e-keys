package com.example.pcdv0;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.bluettooth.ConnectThread;
import com.example.pcdv0.bluettooth.MyBluetoothService;
import com.example.pcdv0.databinding.ActivityControlBinding;

import java.io.IOException;
import java.util.Set;

@SuppressWarnings({"MissingPermission"})
public class ControlActivity extends AppCompatActivity {

    ActivityControlBinding binding;
    private BluetoothAdapter myBluetooth = null;
    BluetoothSocket bluetoothSocket = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        Intent intent = getIntent();
        String address = intent.getStringExtra("adress");  // the address of clicked device
        System.out.println("adress: " + address);

        //return to home page
        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ControlActivity.this, UserNavigationDrawerActivity.class);
                startActivity(intent);
            }
        });

        //connect to selected bluetooth
        binding.connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(BluetoothDevice bt : pairedDevices)
                {
                    if (bt.getAddress().toString().equals(address)){
                        ConnectThread connect = new ConnectThread(bt);
                        connect.start();
                        bluetoothSocket = connect.getMmSocket();   //the current socket
                        Toast.makeText(ControlActivity.this, "connecting to "+bt.getName(), Toast.LENGTH_SHORT).show();

                        if (bluetoothSocket.isConnected() ){
                            Toast.makeText(ControlActivity.this, "connected ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        //disconnect from bluetooth
        binding.disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bluetoothSocket.close();
                    Toast.makeText(ControlActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
            }
        });

        //unlock car ( servo moteur turn right )
        binding.unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("O");
            }
        });

        //lock car (servo moteur turn left)
        binding.lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("C");
            }
        });
    }

    //send a message from app to car
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        MyBluetoothService.ConnectedThread r = new MyBluetoothService.ConnectedThread(bluetoothSocket);

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            r.write(send);
        }
    }
}

