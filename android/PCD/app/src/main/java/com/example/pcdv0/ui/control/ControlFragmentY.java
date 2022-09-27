package com.example.pcdv0.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.pcdv0.databinding.FragmentControlYBinding;


public class ControlFragmentY extends Fragment {

    FragmentControlYBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentControlYBinding.inflate(inflater,container,false);
        View root=binding.getRoot();

        return root;
    }
}