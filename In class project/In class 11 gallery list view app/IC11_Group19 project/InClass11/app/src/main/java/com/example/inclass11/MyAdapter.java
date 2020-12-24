package com.example.inclass11;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Image> mData;

    public MyAdapter(ArrayList<Image> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Image image = mData.get(position);
        Picasso.get().load(image.imageUrl).into(holder.img);

        holder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();

                storageReference = storageReference.child(image.imagePath);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        MainActivity.sourcesList.remove(position);

                        notifyItemRemoved(holder.getAdapterPosition());
//                        notifyItemRangeChanged(holder.getAdapterPosition(), urls.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {

                    }
                });

                return false;
            }
        });

        holder.image = image;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        Image image;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.image = image;

            img = itemView.findViewById(R.id.imgList);

        }
    }
}
