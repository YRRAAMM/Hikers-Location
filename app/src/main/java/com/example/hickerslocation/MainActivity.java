package com.example.hickerslocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    TextView addressTextView;
    TextView accuracyTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView altitudeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to remove the action bar and app title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
               updateLocationInfo(location);
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // I don't know what the use of request code
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    @SuppressLint("SetTextI18n")
    private void updateLocationInfo(Location location) {

        addressTextView = findViewById(R.id.addressTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);

        accuracyTextView.setText("Accuracy: " + location.getAccuracy());
        latitudeTextView.setText("Latitude: " + location.getLatitude());
        longitudeTextView.setText("Longitude: " + location.getLongitude());
        altitudeTextView.setText("Altitude: " + location.getAltitude());

        String address = "Could not find address :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAdresses != null) {
                address = "Address:\n";
                if (listAdresses.get(0).getThoroughfare() != null)
                    address += listAdresses.get(0).getThoroughfare() + "\n";
                if (listAdresses.get(0).getLocality() != null)
                    address += listAdresses.get(0).getLocality() + "\n";
                if (listAdresses.get(0).getPostalCode() != null)
                    address += listAdresses.get(0).getPostalCode() + "\n";
                if (listAdresses.get(0).getAdminArea() != null)
                    address += listAdresses.get(0).getAdminArea() + "\n";
            }

            addressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // getLatitude, getLongitude, getAccuracy, getAltitude
        // address is a string
        // Geocoder
        // list<address> list = getcoder.getfromlocation(lat, long, 1);
        // if list!=null , list.get(0).size() > 0)
        // addressString =
        // latitude
        // longitude
        // accuracy
        // altitude
        // address



    }
}