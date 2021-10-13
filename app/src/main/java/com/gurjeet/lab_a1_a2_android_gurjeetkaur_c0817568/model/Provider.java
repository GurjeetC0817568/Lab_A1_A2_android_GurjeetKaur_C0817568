package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "provider")
public class Provider {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "provider_id")
    int providerId;

    @NonNull
    @ColumnInfo(name = "provider_name")
    private String providerName;

    @ColumnInfo(name = "provider_email")
    private String providerEmail;

    @ColumnInfo(name = "provider_phone")
    private String providerPhone;

    @ColumnInfo(name = "provider_latitude")
    private String providerLatitude;

    @ColumnInfo(name = "provider_longitude")
    private String providerLongitude;

    public Provider(@NonNull String providerName, String providerEmail, String providerPhone, String providerLatitude, String providerLongitude) {
        this.providerName = providerName;
        this.providerEmail = providerEmail;
        this.providerPhone = providerPhone;
        this.providerLatitude = providerLatitude;
        this.providerLongitude = providerLongitude;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    @NonNull
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(@NonNull String providerName) {
        this.providerName = providerName;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public String getProviderLatitude() {
        return providerLatitude;
    }

    public void setProviderLatitude(String providerLatitude) {
        this.providerLatitude = providerLatitude;
    }

    public String getProviderLongitude() {
        return providerLongitude;
    }

    public void setProviderLongitude(String providerLongitude) {
        this.providerLongitude = providerLongitude;
    }


}

