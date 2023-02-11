package com.example.lab_baotrieuthai_866206_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lab_baotrieuthai_866206_android.Favorite;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteRoomDb extends RoomDatabase {

    private static final String DB_NAME = "employee_room_db";

    public abstract FavoriteDao favoriteDao();

    private static volatile FavoriteRoomDb INSTANCE;

    public static FavoriteRoomDb getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavoriteRoomDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        return INSTANCE;
    }
}

