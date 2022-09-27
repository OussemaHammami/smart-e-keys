package com.example.pcdv0.ui.cars;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pcdv0.CarDetailActivity;
import com.example.pcdv0.EditCarActivity;
import com.example.pcdv0.MapActivity;
import com.example.pcdv0.databinding.FragmentCarsAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CarsAdminFragment extends Fragment {


    private FragmentCarsAdminBinding binding ;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<Car> carArrayList;
    private CarsAdminAdapter carsAdminAdapter;
    private RelativeLayout deRL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCarsAdminBinding.inflate(inflater,container,false);
        View root=binding.getRoot();

        binding.addCarFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //database reference.
        databaseReference = firebaseDatabase.getReference("cars");


        carArrayList = new ArrayList<Car>();
        //initializing our adapter class.
        carsAdminAdapter = new CarsAdminAdapter(this.getContext(), carArrayList);
        //get from db
        getCars();


        binding.adminCarListView.setAdapter(carsAdminAdapter);
        binding.adminCarListView.setClickable(true);



        //on item click --> go to car detail
        binding.adminCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CarDetailActivity.class);

                intent.putExtra("name", carArrayList.get(i).getName());
                intent.putExtra("serial_number",carArrayList.get(i).getSerialNumber());
                intent.putExtra("traveled", carArrayList.get(i).getTraveled());
                intent.putExtra("place", carArrayList.get(i).getPlace());
                intent.putExtra("price", carArrayList.get(i).getPrice());
                intent.putExtra("state", carArrayList.get(i).getState());

                startActivity(intent);

            }
        });

        //on item long click --> call alert dialog (edit / delete)
        binding.adminCarListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("Action");


                //button delete
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                dialogInterface.dismiss();
                            }
                        });

                //button edit
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                Intent intent = new Intent(getContext(), EditCarActivity.class);
                                // on below line we are passing our car car
                                intent.putExtra("car", carArrayList.get(i));
                                System.out.println("ID: "+carArrayList.get(i).getSerialNumber());
                                startActivity(intent);
                            }
                        });
                alertDialog.show();

                return true;
            }
        });

        return root;
    }

    private void getCars() {
        // on below line clearing our list.
        carArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                binding.idPBLoading.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                carArrayList.add(snapshot.getValue(Car.class));
                // notifying our adapter that data has changed.
                carsAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                binding.idPBLoading.setVisibility(View.GONE);
                carsAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                carsAdminAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                carsAdminAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}