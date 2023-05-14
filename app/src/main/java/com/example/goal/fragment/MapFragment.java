package com.example.goal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goal.R;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

/**
 * A fragment that displays a map and shows the location of the logged user.
 */
public class MapFragment extends Fragment {
    private MapView mapView;
    /**
     * Default constructor for the MapFragment.
     */
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int customerId = sharedPreferences.getInt("customerId", 0);
        String customerAdd = sharedPreferences.getString("customeraddress", "");
        // Inflate the layout for this fragment


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        Log.d("Addresstest",customerAdd);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(customerAdd,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                .zoom(14.0)
                .center(point)
                .build();
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        mapView.getMapboxMap().setCamera(cameraPosition);


        return rootView;
    }
}