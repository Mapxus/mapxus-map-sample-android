package com.mapxus.mapxusmapandroiddemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.ExampleItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExampleItemModel> dataSource;
    private Context context;

    public ExampleAdapter(Context context, List<ExampleItemModel> dataSource) {
        this.dataSource = dataSource;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() != 0) {
            return;
        }

        ExampleItemModel detailItem = dataSource.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if (detailItem.getImg() == 0) {
            viewHolder.imageView.setImageDrawable(null);
        } else {
            Picasso.with(context)
                    .load(detailItem.getImg())
                    .into(viewHolder.imageView);
        }

        if (detailItem.getShowNewIcon()) {
            viewHolder.newIconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.new_icon));
        } else {
            viewHolder.newIconImageView.setImageDrawable(null);
        }

        viewHolder.titleTextView.setText(context.getString(detailItem.getTitle()));
        viewHolder.descriptionTextView.setText(context.getString(detailItem.getDescription()));
    }

    @Override
    public int getItemCount() {
        return (null != dataSource ? dataSource.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
        public ImageView newIconImageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.example_image);
            titleTextView = (TextView) itemView.findViewById(R.id.example_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.example_description);
            newIconImageView = (ImageView) itemView.findViewById(R.id.new_icon_image_view);
        }
    }

    public static class ViewHolderDescription extends RecyclerView.ViewHolder {

        public ViewHolderDescription(final View itemView) {
            super(itemView);
        }
    }
}