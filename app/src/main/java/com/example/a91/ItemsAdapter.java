package com.example.a91;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<LostFoundItem> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    public ItemsAdapter(List<LostFoundItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost_found, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LostFoundItem item = itemList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(List<LostFoundItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemImage;
        TextView tvItemTypeAndName;
        TextView tvItemTimestamp;
        TextView tvItemLocation;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemTypeAndName = itemView.findViewById(R.id.tvItemTypeAndName);
            tvItemTimestamp = itemView.findViewById(R.id.tvItemTimestamp);
            tvItemLocation = itemView.findViewById(R.id.tvItemLocation);
        }

        public void bind(final LostFoundItem item, final OnItemClickListener listener) {
            Context context = itemView.getContext();
            tvItemTypeAndName.setText(context.getString(R.string.item_format, item.getType(), item.getName()));
            tvItemLocation.setText(context.getString(R.string.location_format, item.getLocation()));
            
            long diff = System.currentTimeMillis() - item.getTimestamp();
            tvItemTimestamp.setText(getTimeString(context, diff));

            if (item.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                ivItemImage.setImageBitmap(bitmap);
            } else {
                ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        private String getTimeString(Context context, long diffMillis) {
            long seconds = diffMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) return context.getString(R.string.days_ago, (int) days);
            if (hours > 0) return context.getString(R.string.hours_ago, (int) hours);
            if (minutes > 0) return context.getString(R.string.minutes_ago, (int) minutes);
            return context.getString(R.string.just_now);
        }
    }
}
