package com.example.movieapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerview_adapter extends RecyclerView.Adapter<recyclerview_adapter.ViewHolder> {

    private ArrayList<recycleview_list> recycleview_list;
    private Context context;


    public recyclerview_adapter(ArrayList<recycleview_list> recycleview_list, Context context) {
        this.recycleview_list = recycleview_list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewShowName;
        private ImageView imageViewShow;
        CardView cardView;

        //reuseable view holder ( in this cas it reuse only in the recycle )
        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            textViewShowName = itemView.findViewById(R.id.textView);
            imageViewShow = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cartView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerview_adapter.ViewHolder holder, int position) {
        holder.textViewShowName.setText(recycleview_list.get(position).getShowName());
        Picasso.get().load(recycleview_list.get(position).getShowImageUrl()).into(holder.imageViewShow);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detailsmovie.class);
                intent.putExtra("Name",recycleview_list.get(position).getShowName());
                intent.putExtra("show_id",recycleview_list.get(position).getShowId());
                intent.putExtra("show_language",recycleview_list.get(position).getShowLanguage());
                intent.putExtra("show_premiered",recycleview_list.get(position).getShowPremiered());
                intent.putExtra("show_summary",recycleview_list.get(position).getShowSummary());
                intent.putExtra("show_img_url",recycleview_list.get(position).getShowImageUrl());
                context.startActivity(intent);
            }
        });

    }


    @Override

    public int getItemCount() {
        return recycleview_list != null ? recycleview_list.size() : 0;
    }




}
