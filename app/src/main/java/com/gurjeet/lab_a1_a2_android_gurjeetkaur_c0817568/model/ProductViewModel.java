package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.data.ProductRepository;

import java.util.List;

public class ProductViewModel  extends AndroidViewModel {
    private ProductRepository repository;
    private final LiveData<List<Provider>> allProviders;
    private LiveData<List<Product>> allProducts;

    //constructor
    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProviders = repository.getAllProviders();
        allProducts = repository.getAllProducts();
    }
    public LiveData<List<Provider>> getAllProviders() {
        return allProviders;
    }


    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }
    public LiveData<List<Product>> getAllProducts(boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        allProducts = repository.getAllProducts(isAsc, isDesc, searchKey, byDate);
        return allProducts;
    }

    public LiveData<List<Product>> getProductsByProvider(int providerId, boolean isAsc, boolean isDesc, String searchKey, boolean byDate) {
        allProducts = repository.getProductsByProvider(providerId, isAsc, isDesc, searchKey, byDate);
        return allProducts;
    }

    public LiveData<List<Product>> getProductsByProvider(int providerId) {
        allProducts = repository.getProductsByProvider(providerId);
        return allProducts;
    }

    public void insertProvider(Provider provider) {repository.insertProvider(provider);}


    public void insertProduct(Product product) {
        repository.insertProduct(product);
    }

    public LiveData<Product> getProductById(int id) {
        return repository.getProductById(id);
    }
    public LiveData<Provider> getProviderById(int id) {
        return repository.getProviderById(id);
    }


    //TODO: not needed now, will check issue later
    public void delete(Product product) {
        repository.delete(product);
    }

    public void deleteProvider(int providerId) {
        repository.deleteProvider(providerId);
    }
    public void update(Product product) {
        repository.update(product);
    }

    public void updateProvider(Provider provider) {
        repository.updateProvider(provider);
    }


}
