package com.example.pcdv0.ui.map;

import static com.example.pcdv0.LoginActivity.typeUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pcdv0.R;
import com.example.pcdv0.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        //initialaize view
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //initialize fragment
        Fragment adminFragment = new MapAdminFragment();
        Fragment clientFragment = new MapClientFragment();


        //open fragment
        if(typeUser == "admin"){
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameMap, adminFragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }
        else if (typeUser != "admin"){
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameMap, clientFragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }


        return root;


            //View view = inflater.inflate(R.layout.frame_map, container, false);

//            //initialize map fragment
//            SupportMapFragment supportMapFragment = (SupportMapFragment)
//                    getChildFragmentManager().findFragmentById(R.id.google_map);
//
//            //async map
//        assert supportMapFragment != null;
//        supportMapFragment.getMapAsync((new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(@NonNull GoogleMap googleMap) {
//                    // when map is loaded
//                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                        @Override
//                        public void onMapClick(@NonNull LatLng latLng) {
//                            //when clicked on map
//                            //initialize marker
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            //set position of marker
//                            markerOptions.position(latLng);
//                            //set title of marker
//                            markerOptions.title(latLng.latitude+ " : " + latLng.longitude);
//                            //remove all marker
//                            googleMap.clear();
//                            //zoom
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                    latLng, 10)
//                            );
//
//                            //add marker on map
//                            googleMap.addMarker(markerOptions);
//                        }
//                    });
//                }
//            }
//            ));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}