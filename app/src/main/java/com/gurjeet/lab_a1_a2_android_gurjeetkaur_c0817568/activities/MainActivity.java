package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.activities;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.R;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Provider;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Product;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainActivity extends AppCompatActivity {

    ImageView createProduct, goBack;
    RecyclerView rcvProducts;
    TextView sortAZ, sortZA, sortDate;
    SearchView searchView;
    ImageView showProviders;

    ArrayList<Product> productList = new ArrayList<>();
    List<Provider> selectedProvider = new ArrayList<>();
    List<String> catSpinnerArr= new ArrayList<>();

    private ProductViewModel productAppViewModel;
    private ProductAdapter productAdapter;
    private int prodId;
    private String searchKey = "";

    public static final String PROVIDER_ID = "provider_id";
    public static final String PROVIDER_NAME = "provider_name";
    public static final String PROVIDER_LAT = "provider_latitude";
    public static final String PROVIDER_LNG = "provider_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //All variables assigning
        sortAZ = findViewById(R.id.sortAZ);
        sortZA = findViewById(R.id.sortZA);
        sortDate = findViewById(R.id.sortDate);
        searchView = findViewById(R.id.searchView);
        rcvProducts = findViewById(R.id.rcvProducts);
        showProviders = findViewById(R.id.showProviders);

        //to not change the size of recycler view when change in adapter content
        rcvProducts.setHasFixedSize(true);





        //setting asc,desc,date wise sort option for each variable
        sortAZ.setOnClickListener(v -> getProductLists(true, false, false));
        sortZA.setOnClickListener(v -> getProductLists(false, true, false));
        sortDate.setOnClickListener(v -> getProductLists(false, false, true));

        productAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);



        /**************show providers activity when click on providers**************/
        showProviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProviderListActivity.class);
                startActivity(intent);
            }
        });

    }


    /************Starts products list with sort option***************************/
    public void getProductLists(boolean isAsc, boolean isDesc, boolean byDate) {
        productAppViewModel.getAllProducts(isAsc, isDesc, searchKey, byDate).observe(this, products -> {
            productList.clear();
            productList.addAll(products);
            productAdapter.notifyDataSetChanged();
        });
    }
    /************Ends products list with sort option***************************/


    /************Start onResume during search***************************/
    @Override
    protected void onResume() {//restart the activity with onResume method
        super.onResume();

        productAdapter = new ProductAdapter(this, productList);
        rcvProducts.setAdapter(productAdapter);

        getProductLists(true, false, false);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchKey = newText;
                getProductLists(true, false, false);
                return false;
            }
        });

    }
    /************Ends onResume during search***************************/



    /************Starts ProductAdapter part***************************/

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private Activity activity;
        private ArrayList<Product> productList;

        ProductAdapter(Activity activity, ArrayList<Product> productList) {
            this.activity = activity;
            this.productList = productList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_products, parent, false);

            return new ProductAdapter.ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.ProductAdapter.ViewHolder holder, int position) {

            holder.productTitle.setText(productList.get(position).getProductName());
            holder.providerName.setText(getIntent().getStringExtra(MainActivity.PROVIDER_NAME));
            holder.productCreateDate.setText(productList.get(position).getProductDate());
            holder.bind(productList.get(position));
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            TextView productTitle,productCreateDate,providerName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //setting title,provider name and date for list
                productTitle = itemView.findViewById(R.id.productTitle);
                providerName = itemView.findViewById(R.id.providerName);
                productCreateDate = itemView.findViewById(R.id.productCreateDate);
            }

            // Bind to show in productDetailActivity class
            public void bind(Product product) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product_id", product.getProductId());
                        intent.putExtra("productProviderId", product.getProductProviderId());
                        intent.putExtra("product_name", product.getProductName());
                        intent.putExtra("product_detail", product.getProductDetail());
                        intent.putExtra("product_price", product.getProductPrice());
                        //set provider latitude and longitude to show map in next activity
                        intent.putExtra("prov_name", getIntent().getStringExtra(MainActivity.PROVIDER_NAME));
                        intent.putExtra("prov_lat", getIntent().getDoubleExtra(MainActivity.PROVIDER_LAT,43.653));
                        intent.putExtra("prov_lng", getIntent().getDoubleExtra(MainActivity.PROVIDER_LNG,79.383));


                        startActivity(intent);
                    }
                });
            }
        }
    }
    /************Ends ProductAdapter part***************************/

}