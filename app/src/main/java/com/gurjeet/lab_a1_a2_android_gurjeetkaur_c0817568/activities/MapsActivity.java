package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.R;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng latLangNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String address = "Address not found"; // default address message
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        latLangNote = new LatLng(getIntent().getDoubleExtra("product_latitude",0) ,  getIntent().getDoubleExtra("product_longitude",0));

        try {
            //gets the address list
            List<Address> addressList = geocoder.getFromLocation(latLangNote.latitude, latLangNote.longitude, 1);
            if (addressList != null && addressList.size() > 0) { // if the addressList gets a result
                address = ""; // empty the address message
                // street name
                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + ", "; // street name
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + ", "; // postal code name
                if (addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + ", "; // city name
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea(); // province name

                mMap.addMarker(new MarkerOptions()
                        .position(latLangNote)
                        .title("Your note Location here")
                        .snippet(address)
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLangNote, 15));
            }
        } catch (Exception e) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLangNote)
                    .title("Your were here")
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLangNote, 15));
            e.printStackTrace();
        }
    }
}