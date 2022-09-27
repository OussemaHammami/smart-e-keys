package com.example.pcdv0.ui.cars;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pcdv0.CarDetailActivity;
import com.example.pcdv0.Location;
import com.example.pcdv0.databinding.FragmentCarsClientBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CarsClientFragment extends Fragment {

    private FragmentCarsClientBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, geodatabaseReference;
    private CarsClientAdapter carsClientAdapter;
    private ArrayList<Car> carArrayList;
    private ArrayList<Location> locationArrayList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCarsClientBinding.inflate(inflater, container, false);
        View root=binding.getRoot();

         carArrayList = new ArrayList<Car>();
         locationArrayList = new ArrayList<Location>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        //database reference.
        databaseReference = firebaseDatabase.getReference("cars");
//        geodatabaseReference = firebaseDatabase.getReference("locations");
        //initializing our adapter class.
        carsClientAdapter = new CarsClientAdapter(this.getContext(), carArrayList);
        //get from db
        getCars();





        binding.clientCarListView.setAdapter(carsClientAdapter);
        binding.clientCarListView.setClickable(true);
        binding.clientCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CarDetailActivity.class);

                intent.putExtra("name", carArrayList.get(i).getName());
                intent.putExtra("serial_number",carArrayList.get(i).getSerialNumber());
                intent.putExtra("traveled", carArrayList.get(i).getTraveled());
                intent.putExtra("place", carArrayList.get(i).getPlace());
                intent.putExtra("price", carArrayList.get(i).getPrice());

                startActivity(intent);

            }
        });
        return root;
    }



    private void getCars() {
        // on below line clearing our list.
        carArrayList.clear();
        // on below line we are calling add child event listener method to read the data.

        Query query = databaseReference.orderByChild("state").equalTo("available");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                binding.idPBLoading.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                carArrayList.add(snapshot.getValue(Car.class));
                // notifying our adapter that data has changed.
                carsClientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                binding.idPBLoading.setVisibility(View.GONE);
                carsClientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                carsClientAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                carsClientAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}