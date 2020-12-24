package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SourceAdapter extends ArrayAdapter<Songs> {

    public SourceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Songs> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Songs songs = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_artistName = (TextView) convertView.findViewById(R.id.txtArtistNameValue);
            viewHolder.tv_trackName = (TextView) convertView.findViewById(R.id.txtTrackNameValue);
            viewHolder.tv_album = (TextView) convertView.findViewById(R.id.txtAlbumValue);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.txtDateValue);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_album.setText(songs.albumName);
        viewHolder.tv_trackName.setText(songs.trackName);
        viewHolder.tv_artistName.setText(songs.artistName);
        viewHolder.tv_date.setText(songs.updatedTime);
        return  convertView;
    }

    private static class ViewHolder{
        TextView tv_album;
        TextView tv_trackName;
        TextView tv_artistName;
        TextView tv_date;
    }
}

