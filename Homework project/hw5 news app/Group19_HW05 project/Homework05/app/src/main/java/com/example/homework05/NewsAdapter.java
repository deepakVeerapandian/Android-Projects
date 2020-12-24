//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework05;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        NewsAdapter.ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list,parent,false);
            viewHolder = new NewsAdapter.ViewHolder();
            viewHolder.iv_news_image = (ImageView) convertView.findViewById(R.id.imgNews);
            viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.tv_news_author = (TextView) convertView.findViewById(R.id.txtAuthor);
            viewHolder.tv_news_date = (TextView) convertView.findViewById(R.id.txtDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (NewsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.iv_news_image.setVisibility(View.VISIBLE);
        viewHolder.tv_news_title.setText(news.title);
        viewHolder.tv_news_date.setText(news.publishedAt);
        viewHolder.tv_news_author.setText(news.author);
        if(news.urlToImage.equals("") || news.urlToImage == null)
            viewHolder.iv_news_image.setVisibility(View.INVISIBLE);
        else
            Picasso.get().load(news.urlToImage).into(viewHolder.iv_news_image);

        return  convertView;
    }

    private static class ViewHolder{
        ImageView iv_news_image;
        TextView tv_news_title;
        TextView tv_news_author;
        TextView tv_news_date;
    }
}
