package com.example.pcdv0.ui.cars;

import static com.example.pcdv0.LoginActivity.typeUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pcdv0.R;
import com.example.pcdv0.databinding.FragmentCarsBinding;

@SuppressWarnings("deprecation")
public class CarsFragment extends Fragment {

    private FragmentCarsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarsViewModel carsViewModel =
                new ViewModelProvider(this).get(CarsViewModel.class);

        binding = FragmentCarsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Fragment fclient = new CarsClientFragment();
        Fragment fadmin = new CarsAdminFragment();

        if (typeUser == "admin") {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.CarsContainer, fadmin, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

        }
        else if (typeUser != "admin"){
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.CarsContainer, fclient, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

        }



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}