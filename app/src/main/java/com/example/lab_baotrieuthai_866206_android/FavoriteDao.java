package com.example.lab_baotrieuthai_866206_android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insertFavorite(Favorite favorite);

    @Query("DELETE FROM favorite")
    void deleteAllFavorites();

    @Query("DELETE FROM favorite WHERE id = :id" )
    int deleteFavorite(int id);

    @Query("UPDATE favorite SET lat = :lat, lng = :lng WHERE id = :id")
    int updateFavorite(int id, Double lat, Double lng);

    @Query("SELECT * FROM favorite ORDER BY saved_date")
    List<Favorite> getAllFavorites();

}
