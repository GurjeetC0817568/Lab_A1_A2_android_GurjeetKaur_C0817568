package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Product;
import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model.Provider;

import java.util.List;

@Dao
public interface ProductDao {

    //insert queries for provider & product
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProvider(Provider provider);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Product product);


    // select query for provider and product
    @Query("SELECT * FROM provider")
    LiveData<List<Provider>> getAllProviders();
    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAllProducts();


    //sql for sorting
    @Query("SELECT * FROM product WHERE productProviderId = :providerId AND product_name LIKE  '%' || :searchKey || '%'  ORDER BY  " +
            "CASE  WHEN :isAsc = 1 THEN product_name END ASC, " +
            "CASE WHEN :isDesc = 1 THEN product_name END DESC, " +
            "CASE WHEN :byDate = 1 THEN product_date END DESC"
    )
    LiveData<List<Product>> getProductsForProvider(int providerId, boolean isAsc, boolean isDesc, String searchKey, boolean byDate);

    @Query("SELECT * FROM product WHERE product_name LIKE  '%' || :searchKey || '%'  ORDER BY  " +
            "CASE  WHEN :isAsc = 1 THEN product_name END ASC, " +
            "CASE WHEN :isDesc = 1 THEN product_name END DESC, " +
            "CASE WHEN :byDate = 1 THEN product_date END DESC"
    )
    LiveData<List<Product>> getAllProducts(boolean isAsc, boolean isDesc, String searchKey, boolean byDate);



    //sql for search
    @Query("SELECT * FROM product WHERE productProviderId = :providerId AND product_name LIKE :searchKey")
    public List<Product> getProductsBySearch(int providerId, String searchKey);

    //sql show products by providers
    @Query("SELECT * FROM product WHERE productProviderId = :providerId")
    LiveData<List<Product>> getProductsForProvider(int providerId);

    //sql show product by its id
    @Query("SELECT * FROM product WHERE product_id = :id LIMIT 1")
    public LiveData<Product> getProductById(int id);

    //sql show provider by its id
    @Query("SELECT * FROM provider WHERE provider_id = :id LIMIT 1")
    public LiveData<Provider> getProviderById(int id);

    // update and delete queries
    @Query("DELETE FROM product")
    void deleteAll();
    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Product product);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateProvider(Provider provider);

    @Query("SELECT * FROM provider WHERE provider_id <> :providerId")
    LiveData<List<Provider>> getAllOtherProvidersExcept(int providerId);

    @Delete
    void deleteProduct(Product product);

    @Query("DELETE FROM provider WHERE provider_id = :providerId")
    void deleteProvider(int providerId);
}
