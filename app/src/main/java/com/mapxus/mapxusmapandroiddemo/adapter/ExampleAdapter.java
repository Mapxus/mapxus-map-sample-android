package com.mapxus.mapxusmapandroiddemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.ExampleItemModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ExampleItemModel> dataSource;
    private Context context;
    private onItemClickListener onItemClickListener;

    public ExampleAdapter(Context context, List<ExampleItemModel> dataSource) {
        this.dataSource = dataSource;
        this.context = context;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        ExampleItemModel detailItem = dataSource.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if (detailItem.getImg() == 0) {
            viewHolder.imageView.setImageDrawable(null);
        } else {
            Picasso.with(context)
                    .load(detailItem.getImg())
                    .into(viewHolder.imageView);
        }

        viewHolder.titleTextView.setText(context.getString(detailItem.getTitle()));
        viewHolder.descriptionTextView.setText(context.getString(detailItem.getDescription()));
        viewHolder.llExample.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataSource ? dataSource.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView imageView;
        public LinearLayout llExample;

        public ViewHolder(final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.example_image);
            titleTextView = itemView.findViewById(R.id.example_title);
            descriptionTextView = itemView.findViewById(R.id.example_description);
            llExample = itemView.findViewById(R.id.ll_example);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(ExampleAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}