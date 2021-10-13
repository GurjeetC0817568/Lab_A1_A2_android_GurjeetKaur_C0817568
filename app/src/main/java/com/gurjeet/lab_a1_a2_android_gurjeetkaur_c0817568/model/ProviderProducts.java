package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProviderProducts {
    @Embedded
    public Provider provider;
    @Relation(
            parentColumn = "provider_id",
            entityColumn = "productProviderId"
    )
    public List<Product> products;

}
