package com.example.application.services;

import com.example.application.data.AssetEntity;

public final class RoundingUtil {
    public static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double displayAmount(AssetEntity assetEntity) {
        if (assetEntity.getRate() > 0) {
            return round(assetEntity.getAmount() * assetEntity.getRate());
        } else {
            return round(assetEntity.getAmount());
        }
    }
}
