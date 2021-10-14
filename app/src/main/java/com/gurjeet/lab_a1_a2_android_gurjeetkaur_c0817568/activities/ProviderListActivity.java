package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.R;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.ProductViewModel;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Provider;

import java.util.ArrayList;

public class ProviderListActivity extends AppCompatActivity {
    RecyclerView rcvProvider;
    ImageView showProducts,createProvider;
    private ProductViewModel productViewModel;
    ArrayList<Provider> providerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_list);
        showProducts = findViewById(R.id.showProducts);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);

        productViewModel.getAllProviders().observe(this, providers -> {
            if (providers.size()==0){


            }
            providerList.clear();
            providerList.addAll(providers);
            rcvProvider = findViewById(R.id.rcvProvider);
            rcvProvider.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
            rcvProvider.setAdapter(new ProviderAdapter(this, providerList));
        });

        /**************show providers activity when click on providers**************/
        showProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /**************create providers starts from here**************/
        createProvider = findViewById(R.id.createProvider);
        createProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProviderListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(ProviderListActivity.this);
                View view = layoutInflater.inflate(R.layout.dialog_create_provider, null);
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                EditText providerNameET = view.findViewById(R.id.providerNameET);
                EditText providerEmailET = view.findViewById(R.id.providerEmailET);
                EditText providerPhoneET = view.findViewById(R.id.providerPhoneET);
                EditText providerLatET = view.findViewById(R.id.providerLatET);
                EditText providerLngET = view.findViewById(R.id.providerLngET);
                Button btnCreate = view.findViewById(R.id.createProviderBTN);

                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String providerName = providerNameET.getText().toString().trim();
                        String providerEmail = providerEmailET.getText().toString().trim();
                        String providerPhone = providerPhoneET.getText().toString().trim();
                        String providerLat = providerLatET.getText().toString().trim();
                        String providerLng = providerLngET.getText().toString().trim();
                        if (providerName.isEmpty() ||providerEmail.isEmpty() ||providerPhone.isEmpty() ||providerLat.isEmpty() ||providerLng.isEmpty()) {
                            alertBox("Please fill all the fields");
                            return;
                        }


                        productViewModel.insertProvider(new Provider(providerName,providerEmail,providerPhone,providerLat,providerLng));
                        alertDialog.dismiss();
                    }
                });
            }
        });

    }



    class ProviderAdapter extends
            RecyclerView.Adapter<ProviderAdapter.ViewHolder> {

        private Activity activity;
        private ArrayList<Provider> providerList;

        ProviderAdapter(Activity activity, ArrayList<Provider> providerList) {
            this.activity = activity;
            this.providerList = providerList;
        }

        @NonNull
        @Override
        public ProviderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_provider, parent, false);

            return new ProviderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProviderAdapter.ViewHolder holder, int position) {

            holder.providerNameTV.setText(providerList.get(position).getProviderName());
            holder.providerEmailTV.setText(providerList.get(position).getProviderEmail());
            holder.providerPhoneTV.setText(providerList.get(position).getProviderPhone());
            holder.providerNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ProductListActivity.class);
                    intent.putExtra(ProductListActivity.PROVIDER_ID, providerList.get(position).getProviderId());
                    intent.putExtra(ProductListActivity.PROVIDER_NAME, providerList.get(position).getProviderName());
                    intent.putExtra(ProductListActivity.PROVIDER_LAT, providerList.get(position).getProviderLatitude());
                    intent.putExtra(ProductListActivity.PROVIDER_LNG, providerList.get(position).getProviderLongitude());
                    startActivity(intent);
                }
            });

            //delete when long press the keyboard
            holder.providerNameTV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // confirmation dialog to ask user before delete contact
                    androidx.appcompat.app.AlertDialog.Builder builderL = new androidx.appcompat.app.AlertDialog.Builder(ProviderListActivity.this);
                    builderL.setTitle("Are you sure to delete this Provider?");
                    builderL.setPositiveButton("Yes", (dialog, which) -> {
                        productViewModel.deleteProvider(providerList.get(position).getProviderId());
                    });
                    builderL.setNegativeButton("No", (dialog, which) -> {});
                    androidx.appcompat.app.AlertDialog alertDialogL = builderL.create();

                    alertDialogL.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return providerList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView providerNameTV,providerEmailTV,providerPhoneTV;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                providerNameTV = itemView.findViewById(R.id.providerNameTV);
                providerEmailTV = itemView.findViewById(R.id.providerEmailTV);
                providerPhoneTV = itemView.findViewById(R.id.providerPhoneTV);
            }
        }


    }


    public void alertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProviderListActivity.this);
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