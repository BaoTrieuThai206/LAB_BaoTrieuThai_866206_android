package com.example.lab_baotrieuthai_866206_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private FavoriteRoomDb favoriteRoomDb;
    List<Favorite> favoriteList;
    ListView favoriteListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteListView = findViewById(R.id.listView);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                view.getContext().startActivity(intent);}
        });

        favoriteRoomDb = FavoriteRoomDb.getInstance(this);
        loadFavorite();
    }

    private void loadFavorite() {
        favoriteList = favoriteRoomDb.favoriteDao().getAllFavorites();
        // create an adapter to display the employees
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, R.layout.list_layout_favorite, favoriteList);
        favoriteListView.setAdapter(favoriteAdapter);
    }
}