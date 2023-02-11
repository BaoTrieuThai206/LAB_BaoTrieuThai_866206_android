package com.example.lab_baotrieuthai_866206_android;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class Favorite {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "lat")
    private Double lat;

    @ColumnInfo(name = "lng")
    private Double lng;

    @ColumnInfo(name = "saved_date")
    private String savedDate;

    public Favorite(String name, Double lat, Double lng, String savedDate) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.savedDate = savedDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double Lat) {
        this.lat = lat;
    }



    public Double getLng() {
        return lng;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }

    @NonNull
    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
