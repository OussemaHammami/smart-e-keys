package com.example.pcdv0.bluettooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.GetLocationActivity;
import com.example.pcdv0.databinding.ActivityBluetoothBinding;

import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings({"MissingPermission"})
public class BluetoothActivity extends AppCompatActivity {
    private BluetoothAdapter myBluetooth = null;


    ActivityBluetoothBinding binding;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);


            BluetoothDevice btt = null;

            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            if(myBluetooth == null)
            {
                //Show a mensag. that thedevice has no bluetooth adapter
                Toast.makeText(BluetoothActivity.this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
                //finish apk
                //finish();
            }
            else
            {
                if (myBluetooth.isEnabled())
                { }
                else
                {
                    //Ask to the user turn the bluetooth on
                    Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    startActivityForResult(turnBTon,1);
                }
            }

            binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    pairedDevicesList(); //method that will be called
                }
            });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList(); //method that will be called
            }
        });

    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(BluetoothActivity.this, "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(BluetoothActivity.this,android.R.layout.simple_list_item_1, list);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the device MAC address, the last 17 chars in the View
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                // Make an intent to start next activity.
                Intent intent = new Intent(BluetoothActivity.this, GetLocationActivity.class);
                //Change the activity.
                intent.putExtra("adress", address);
                startActivity(intent);
            }
        }); //Method called when the device from the list is clicked


    }
}
