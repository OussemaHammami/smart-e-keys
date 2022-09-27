package com.example.pcdv0.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pcdv0.User;
import com.example.pcdv0.UserDetailActivity;
import com.example.pcdv0.databinding.FragmentUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<User> userArrayList;
    private UsersAdapter usersAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsersViewModel usersViewModel =
                new ViewModelProvider(this).get(UsersViewModel.class);

        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //database reference.
        databaseReference = firebaseDatabase.getReference("users");


        userArrayList = new ArrayList<User>();
        //initializing our adapter class.
        usersAdapter = new UsersAdapter(this.getContext(),userArrayList);
        //get from db
        getUsers();


        binding.userListView.setAdapter(usersAdapter);
        binding.userListView.setClickable(true);

        binding.userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), UserDetailActivity.class);

                intent.putExtra("id", userArrayList.get(i).getId());
                intent.putExtra("name", userArrayList.get(i).getName());
                intent.putExtra("fname",userArrayList.get(i).getFamilyName());
                intent.putExtra("email", userArrayList.get(i).getEmail());
                intent.putExtra("tel", userArrayList.get(i).getTelNumber());
                intent.putExtra("state", userArrayList.get(i).getRent());
                intent.putExtra("type", userArrayList.get(i).getType());

                startActivity(intent);

            }
        });

        return root;
    }

    private void getUsers() {
        userArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                binding.idPBLoading.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                userArrayList.add(snapshot.getValue(User.class));
                // notifying our adapter that data has changed.
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                binding.idPBLoading.setVisibility(View.GONE);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                usersAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                usersAdapter.notifyDataSetChanged();
                binding.idPBLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}