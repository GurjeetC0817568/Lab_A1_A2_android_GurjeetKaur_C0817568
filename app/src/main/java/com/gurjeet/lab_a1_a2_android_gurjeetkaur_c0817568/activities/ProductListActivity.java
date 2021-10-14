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


public class ProductListActivity extends AppCompatActivity {

    ImageView createProduct, goBack;
    RecyclerView rcvProducts;
    TextView sortAZ, sortZA, sortDate;
    SearchView searchView;

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
        setContentView(R.layout.activity_product_list);

        //All variables assigning
        sortAZ = findViewById(R.id.sortAZ);
        sortZA = findViewById(R.id.sortZA);
        sortDate = findViewById(R.id.sortDate);
        createProduct = findViewById(R.id.createProduct);
        searchView = findViewById(R.id.searchView);
        rcvProducts = findViewById(R.id.rcvProducts);
        goBack = findViewById(R.id.imgBack);

        //to not change the size of recycler view when change in adapter content
        rcvProducts.setHasFixedSize(true);
        prodId = getIntent().getIntExtra(ProductListActivity.PROVIDER_ID, 0);


        createProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), ProductDetailActivity.class);
            intent.putExtra(ProductListActivity.PROVIDER_ID, getIntent().getIntExtra(ProductListActivity.PROVIDER_ID, 0));
            startActivity(intent);
        });


        //setting asc,desc,date wise sort option for each variable
        sortAZ.setOnClickListener(v -> getProductLists(true, false, false));
        sortZA.setOnClickListener(v -> getProductLists(false, true, false));
        sortDate.setOnClickListener(v -> getProductLists(false, false, true));

        productAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);

        // back button click to go back
        goBack.setOnClickListener(v -> {
            finish();
        });


    }


    /************Starts products list with sort option***************************/
    public void getProductLists(boolean isAsc, boolean isDesc, boolean byDate) {
        productAppViewModel.getProductsByProvider(prodId, isAsc, isDesc, searchKey, byDate).observe(this, products -> {
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
        //   ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //   itemTouchHelper.attachToRecyclerView(rcvProducts);
    }
    /************Ends onResume during search***************************/

    /************Starts Left Right Swipe Part***************************/
   ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                //Delete task when left swipe
                case ItemTouchHelper.LEFT:
                    AlertDialog.Builder builderl = new AlertDialog.Builder(ProductListActivity.this);
                    builderl.setTitle("You sure to delete this product?");

                    //when click yes then delete
                    builderl.setPositiveButton("Yes", (dialog, which) -> {
                        productAppViewModel.delete(productList.get(position));
                    });
                    //when click No then do nothing
                    builderl.setNegativeButton("No", (dialog, which) -> productAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builderl.create();
                    alertDialog.show();
                    break;
                //move provider part when right swipe
                case ItemTouchHelper.RIGHT:
                    productAppViewModel.getAllProviders().observe(ProductListActivity.this, providers -> {
                        if (providers.size() == 1){
                            //move function will not work if there is only one provider
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                            builder.setTitle("Warning! can not possible!");
                            builder.setMessage("There is one provider only");
                            builder.setCancelable(false);
                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();

                        }else{
                            //if more than 1 providers then update the product's providerId in product table
                            selectedProvider = providers;
                            for (Provider provider :providers){
                                catSpinnerArr.add(provider.getProviderName());
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                            LayoutInflater layoutInflater = LayoutInflater.from(ProductListActivity.this);
                            View view = layoutInflater.inflate(R.layout.dialog_move_product_provider, null);
                            builder.setView(view);
                            final AlertDialog alertDialogr = builder.create();
                            alertDialogr.show();

                            //Reference:https://stackoverflow.com/questions/6485158/custom-style-setdropdownviewresource-android-spinner/22178862
                            //Reference: https://stackoverflow.com/questions/40261501/how-to-set-same-appearance-for-spinner-in-xml-design
                            Spinner otherProvidersSp = view.findViewById(R.id.otherProvidersSp);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, catSpinnerArr);
                            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            otherProvidersSp.setAdapter(adapter);

                            //when popup dialog box's button click after selecting new provider
                            Button btnChangeCat = view.findViewById(R.id.btnChangeProvider);
                            btnChangeCat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Product productToUpdate = productList.get(position);
                                    final Provider providerToUpdate = selectedProvider.get(otherProvidersSp.getSelectedItemPosition());
                                    //set product's provider id to new providerId in product table
                                    productToUpdate.setProductProviderId(providerToUpdate.getProviderId());
                                    productAppViewModel.update(productToUpdate);
                                    alertDialogr.dismiss();
                                }
                            });
                        }
                    });
            }
        }



    };
    /************Ends Left Right Swipe Part***************************/


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
        public void onBindViewHolder(@NonNull ProductListActivity.ProductAdapter.ViewHolder holder, int position) {

            holder.productTitle.setText(productList.get(position).getProductName());
            holder.providerName.setText(getIntent().getStringExtra(ProductListActivity.PROVIDER_NAME));
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

            // binding data to show in productDetailActivity class for full view
            public void bind(Product product) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product_id", product.getProductId());
                        intent.putExtra("productProviderId", product.getProductProviderId());
                        intent.putExtra("product_name", product.getProductName());
                        intent.putExtra("product_detail", product.getProductDetail());
                        intent.putExtra("product_price", product.getProductPrice());
                        //set provider latitude and longitude to show map in next activity
                        intent.putExtra("prov_name", getIntent().getStringExtra(ProductListActivity.PROVIDER_NAME));
                        intent.putExtra("prov_lat", getIntent().getDoubleExtra(ProductListActivity.PROVIDER_LAT,43.653));
                        intent.putExtra("prov_lng", getIntent().getDoubleExtra(ProductListActivity.PROVIDER_LNG,79.383));


                        startActivity(intent);
                    }
                });
            }
        }
    }
    /************Ends ProductAdapter part***************************/

}