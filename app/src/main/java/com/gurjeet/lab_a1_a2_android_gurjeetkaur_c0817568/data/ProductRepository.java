package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Product;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Provider;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.util.ProductRoomDB;

import java.util.List;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Provider>> allProviders;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application) {
        ProductRoomDB db = ProductRoomDB.getInstance(application);
        productDao = db.productDao();
        allProviders = productDao.getAllProviders();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Provider>> getAllProviders() {
       /* if(allProviders==0){
        loadDemoData();
        }
        */
        return allProviders;
    }

   /* public void loadDemoData(){
        // Product employee = new Product("Mo", "cs", "", 1111);
        // ProductDao.insertProduct(employee);
        // Product employee = new Product("Mo", "cs", "", 1111);
        // ProductDao.insertProduct(employee);
    }*/

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }


    public void insertProvider(Provider provider) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.insertProvider(provider));
    }

    public void insertProduct(Product product) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.insertProduct(product));
    }

    // Get products by Provider
    public LiveData<List<Product>> getProductsByProvider(int catId) {
        return productDao.getProductsForProvider(catId);
    }

    public LiveData<List<Product>> getProductsByProvider(int catId, boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        return productDao.getProductsForProvider(catId, isAsc, isDesc, searchKey, byDate);
    }
    public LiveData<List<Product>> getAllProducts(boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        return productDao.getAllProducts(isAsc, isDesc, searchKey, byDate);
    }





    // Get a product by Id
    public LiveData<Product> getProductById(int id) {
        return productDao.getProductById(id);
    }  public LiveData<Provider> getProviderById(int id) {
        return productDao.getProviderById(id);
    }

    /* //implement it later in related files like productViewModel
    // updates a product
    public void update(Product product) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.update(product));
    }

    // updates a product
    public void updateProvider(Provider provider) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.updateProvider(provider));
    }

    public void delete(Product product) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.deleteProduct(product));
    }
    public void deleteProvider(int providerId) {
        ProductRoomDB.databaseWriteExecutor.execute(() -> productDao.deleteProvider(providerId));
    }

     */
}
