package com.example.movieapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Myliste> {
    private Activity mContext;
    List<Myliste> myliste;

    public ListAdapter(@NonNull Context context, List<Myliste> myliste) {
        super(context, R.layout.list_item, myliste);
        this.mContext = (Activity) context;
        this.myliste = myliste;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item, null, true);

        TextView MovieName = listItemView.findViewById(R.id.movieName);
        TextView Date = listItemView.findViewById(R.id.date);
        TextView Time = listItemView.findViewById(R.id.time);
        TextView Note = listItemView.findViewById(R.id.note);

        Myliste mylisteItem = myliste.get(position);

        MovieName.setText(mylisteItem.getMovieName());
        Date.setText(mylisteItem.getDate());
        Time.setText(mylisteItem.getTime());
        Note.setText(mylisteItem.getNote());


        return listItemView;
    }
}
