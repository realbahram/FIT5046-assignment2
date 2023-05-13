package com.example.goal.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goal.R;
import com.example.goal.entity.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {
    private MapView mapView;
    private FirebaseAuth authProfile;

    public MapFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String useId = firebaseUser.getUid();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");
        referenceProfile.child(useId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                if (customer != null) {
                    String name = customer.getName();
                    String email = customer.getEmail();
                    String address = customer.getAddress();
                    Log.d("Address",address);
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(address,1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // save in local database
                    //customerViewModel.insert(temp);
                    double latitude = 0;
                    double longitude = 0;
                    if (addresses.size() > 0) {
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        Log.d("Geocode", "Latitude: " + latitude + ", Longitude: " + longitude);
                    } else {
                        Log.d("Geocode", "Address not found");
                    }

                    final Point point = Point.fromLngLat(longitude, latitude);
                    mapView = rootView.findViewById(R.id.mapView);
                    CameraOptions cameraPosition = new CameraOptions.Builder()
                            .zoom(13.0)
                            .center(point)
                            .build();
                    mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
                    mapView.getMapboxMap().setCamera(cameraPosition);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something Gone wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}