package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;


import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.R;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.ProductViewModel;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ProductDetailActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 1;
    public static final int READ_EXTERNAL_STORAGE_CODE = 2;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Geocoder geocoder;
    LatLng latLangProduct = null;

    private ProductViewModel productAppViewModel;
    ArrayList<Product> productList = new ArrayList<>();

    //variables set
    ImageButton btnPlay, btnRecord;
    TextView locationDetailsTV,saveTV;
    ImageView btnBack,  mapIcon;
    EditText titleET, detailET,priceET,provET,provLatET,provLngET;


    private double provLat,provLng;
    private int provID = 0;
    private int productId = 0;
    String pathSave = "", recordFile = null;
    Boolean isRecording = false, isPlaying = false, imageSet = false;

    ActivityResultLauncher<Intent> myActivityResultLauncher;


    private static final int FASTEST_INTERVAL = 1000; // 1 seconds
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int SMALLEST_DISPLACEMENT = 200; // 200 meters

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //variable assigned
        titleET = findViewById(R.id.titleET);
        detailET = findViewById(R.id.detailET);
        priceET= findViewById(R.id.priceET);
        btnBack = findViewById(R.id.backBtn);
        saveTV = findViewById(R.id.saveTV);

        locationDetailsTV = findViewById(R.id.locationDetailsTV);
        mapIcon = findViewById(R.id.mapIcon);


        // when back button click then go back
        btnBack.setOnClickListener(v -> {
            finish();
        });


        provID = getIntent().getIntExtra(ProductListActivity.PROVIDER_ID, 0);
        provLat = getIntent().getDoubleExtra(ProductListActivity.PROVIDER_LAT,43.6532);
        provLng = getIntent().getDoubleExtra(ProductListActivity.PROVIDER_LNG , -79.383);
        productId = getIntent().getIntExtra("product_id", -1) ;

        // set location initializer
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //display product,detail from previous page using intent
        if(productId  > 0){
            latLangProduct = new LatLng(provLat, provLng);
            titleET.setText(getIntent().getStringExtra("product_name"));
            detailET.setText(getIntent().getStringExtra("product_detail"));
            priceET.setText(getIntent().getStringExtra("product_price"));

        }


        /**************Save button click for both add and update**************/
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleET.getText().toString().trim();
                String detail = detailET.getText().toString().trim();
                 String price = detailET.getText().toString().trim();
                //setting date format
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aa");
                String strDate = dateFormat.format(date);


                if (title.isEmpty() || detail.isEmpty()) {
                    alertBox("Enter both product title and description!");
                }else {

                    if(productId > 0){
                        //update product
                        productAppViewModel.getProductById(productId).observe(ProductDetailActivity.this, product -> {
                            product.setProductProviderId(product.getProductProviderId());
                            product.setProductName(title);
                            product.setProductDetail(detail);
                            product.setProductPrice(price);
                             productAppViewModel.update(product);
                        });

                    }else{
                        //add product
                        //date settings
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        //insert into table
                        productAppViewModel.insertProduct(new Product(provID, title, detail, price,formattedDate));
                    }
                    finish();
                }
            }
        });
        /**************Ends save button click for both add and update**************/

        productAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(ProductViewModel.class);


        // Reference:https://stackoverflow.com/questions/62613424/java-solution-for-startactivityforresultintent-int-in-fragment-has-been-depre
        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            //will use it later
                        }
                    }
                });



        // added permissions
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0)
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        else
            startUpdateLocation();


        /**************location text click to show map activity***********/
        locationDetailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MapsActivity.class);
                intent.putExtra("product_latitude", provLat);
                intent.putExtra("product_longitude", provLng);
                startActivity(intent);
            }
        });

        /**************mapIcon click to show map activity**************/
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MapsActivity.class);
                intent.putExtra("product_latitude", provLat);
                intent.putExtra("product_longitude", provLng);
                startActivity(intent);
            }
        });



    }
    /********************ON CREATE FUNCTIONS ENDS HERE*******************/



    //when activity resume is complete
    @Override
    protected void onPostResume() {
        super.onPostResume();

        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(ProductDetailActivity.this, "Google Services Not Available", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
//            findLocation();
        }
    }



    /************** Start location related methods **************************/
    @SuppressLint("MissingPermission")
    private void findLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locationDetailsTV.setText(String.format("Accuracy: %s,Altitude: %s", location.getAccuracy(), location.getAltitude()));
                }
            }
        });

        startUpdateLocation();
    }

    List<Address> addresses;
    @SuppressLint("MissingPermission")
    private void startUpdateLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    try {
                        //Reference:https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
                        if(latLangProduct == null){
                            latLangProduct = new LatLng(location.getLatitude(), location.getLongitude());
                        }

                        addresses = geocoder.getFromLocation(latLangProduct.latitude, latLangProduct.longitude, 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = "";
                        if (addresses != null && addresses.size() > 0) {
                            if (addresses.get(0).getThoroughfare() != null)
                                address += addresses.get(0).getThoroughfare() + ", "; // street
                            if (addresses.get(0).getPostalCode() != null)
                                address += addresses.get(0).getPostalCode() + ", "; // postal code
                            if (addresses.get(0).getLocality() != null)
                                address += addresses.get(0).getLocality() + ", "; // city
                            if (addresses.get(0).getAdminArea() != null)
                                address += addresses.get(0).getAdminArea(); // province

                        }

                        // adding address in textview
                        locationDetailsTV.setText(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    /************** Ends location related methods **************************/


    /************** Start permissions  methods **************************/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private List<String> permissionsToRequest(List<String> permissions) {
        ArrayList<String> results = new ArrayList<>();
        for (String perm : permissions) {
            if (!hasPermission(perm))
                results.add(perm);
        }

        return results;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (String perm : permissions) {
                if (!hasPermission(perm))
                    permissionsRejected.add(perm);
            }

            if (permissionsRejected.size() > 0) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(ProductDetailActivity.this)
                            .setMessage("The permission is mandatory")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE);
                                }
                            }).setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }else{
                startUpdateLocation();
            }
        }
    }
    /************** Ends permissions  methods **************************/


    // alert box function

    public void alertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
        builder.setTitle("Message!");
        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}