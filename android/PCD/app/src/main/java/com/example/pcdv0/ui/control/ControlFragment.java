package com.example.pcdv0.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pcdv0.R;
import com.example.pcdv0.databinding.FragmentControlBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlFragment extends Fragment {


    private FragmentControlBinding binding;
    private boolean rent = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ControlViewModel controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);

        binding = FragmentControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Fragment yControl = new ControlFragmentY();
        Fragment bluetoothFragment = new BluetoothFragment();

//        getActivity()
//            .getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id.frameControl, bluetoothFragment, "findThisFragment")
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit();




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("users").child(userId).child("rent");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String r = snapshot.getValue(String.class);
                System.out.println("rent? "+r.equals("renting"));
                if (r.equals("renting")) {
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameControl, bluetoothFragment, "findThisFragment")
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }

                else  {
                    Toast.makeText(getContext(), "you didn't rent any car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "db error", Toast.LENGTH_SHORT).show();
            }
        });

        System.out.println("rent ! "+rent);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}