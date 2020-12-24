package com.example.a801100869_midterm;

import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Forecast> mData;

    public MyAdapter(ArrayList<Forecast> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_list, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Forecast music = mData.get(position);
        holder.tv_temp.setText("Temperature : "+music.temp + " F");
        holder.tv_humidity.setText("Humdity : "+music.humidity + " %");
        holder.tv_desc.setText(music.description);
        holder.tv_date.setText("At " + music.dt_txt);
        if(music.icon != null)
            Picasso.get().load("http://openweathermap.org/img/wn/"+music.icon+"@2x.png").into(holder.img);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        try {
//            Date date = format.parse(music.dt_txt);
//            String required_format  = (String) DateFormat.format("HH", date);
////                                Date required_format  = (Date) DateFormat.format("MM-dd-yyyy", date);
//            source.releaseDate = date;
////                                source.releaseDate = new Date(track.getString("releaseDate"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String [] x = music.dt_txt.split(" ");
        String [] y = x[1].split(":");
        int yy = Integer.parseInt(y[0].toString());
        if(yy <= 12)
            holder.tv_date.setText("At " + y[0] + " AM");
        else {
            yy = yy-12;
            holder.tv_date.setText("At " + yy + " PM");
            if(yy==0)
                holder.tv_date.setText("At 12 PM");
        }

        holder.music = music;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_date;
        TextView tv_temp;
        TextView tv_desc;
        TextView tv_humidity;
        ImageView img;
        Forecast music;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.music = music;
            tv_date = itemView.findViewById(R.id.txtWFdate);
            tv_desc = itemView.findViewById(R.id.txtWFDesc);
            tv_humidity = itemView.findViewById(R.id.humidityWF);
            tv_temp = itemView.findViewById(R.id.txtWFtemp);
            img = itemView.findViewById(R.id.imgWF);

        }
    }
}

