package com.example.lab_baotrieuthai_866206_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class FavoriteAdapter extends ArrayAdapter {
    private static final String TAG = "FavoriteAdapter";

    Context context;
    int layoutRes;
    List<Favorite> favoriteList;
    FavoriteRoomDb favoriteRoomDb;
    public FavoriteAdapter(@NonNull Context context, int resource, List<Favorite> favoriteList) {
        super(context, resource);
        this.favoriteList = favoriteList;
        this.context = context;
        this.layoutRes = resource;
        favoriteRoomDb = FavoriteRoomDb.getInstance(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        TextView nameTV = v.findViewById(R.id.tv_name);
        TextView latTV = v.findViewById(R.id.tv_lat);
        TextView lngTV = v.findViewById(R.id.tv_lng);
        TextView savedDateTV = v.findViewById(R.id.tv_saved_date);

        final Favorite favorite = favoriteList.get(position);
        nameTV.setText(favorite.getName());
        latTV.setText(String.valueOf(favorite.getLat()));
        lngTV.setText(String.valueOf(favorite.getLng()));
        savedDateTV.setText(favorite.getSavedDate());

        v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavorite(favorite);
            }

            private void deleteFavorite(final Favorite favorite) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favoriteRoomDb.favoriteDao().deleteFavorite(favorite.getId());
                        loadFavorites();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The favorite place (" + favorite.getName() + ") is not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return v;
    }

    private void loadFavorites() {
        favoriteList = favoriteRoomDb.favoriteDao().getAllFavorites();
        notifyDataSetChanged();
    }
}
