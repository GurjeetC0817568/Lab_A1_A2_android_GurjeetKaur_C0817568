package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "product", foreignKeys = @ForeignKey(entity = Provider.class,
        parentColumns = "provider_id",
        childColumns = "productProviderId",
        onDelete = CASCADE))

public class Product{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    private int productId;

    @NonNull
    private int productProviderId;

    @NonNull
    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_detail")
    private String productDetail;

    @ColumnInfo(name = "product_price")
    private String productPrice;

    @ColumnInfo(name = "product_date")
    private String productDate;

    public Product(){}
    public Product(int productProviderId, @NonNull String productName, String productDetail, String productPrice, String productDate) {
        this.productProviderId = productProviderId;
        this.productName = productName;
        this.productDetail = productDetail;
        this.productPrice = productPrice;
        this.productDate = productDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductProviderId() {
        return productProviderId;
    }

    public void setProductProviderId(int productProviderId) {
        this.productProviderId = productProviderId;
    }

    @NonNull
    public String getProductName() {
        return productName;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }
}
