package com.mapxus.mapxusmapandroiddemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryInfo;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils;
import com.mapxus.mapxusmapandroiddemo.utils.SetImageByCategoryUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PoiCategoryInfo> categoryInfos;
    private onItemClickListener onItemClickListener;

    public CategoriesListAdapter(List<PoiCategoryInfo> categoryInfos) {
        this.categoryInfos = categoryInfos;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        PoiCategoryInfo categoryInfo = categoryInfos.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvCategory.setText(LocalLanguageUtils.getLocalLanguageWithPoiCategoryInfo(categoryInfo));
        viewHolder.ivCategory.setImageResource(SetImageByCategoryUtils.setImageByCategory(categoryInfo.getCategory()));
        viewHolder.llCategory.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(categoryInfo.getCategory(), viewHolder.tvCategory.getText().toString().trim(), position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != categoryInfos ? categoryInfos.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory;
        public ImageView ivCategory;
        public LinearLayout llCategory;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_category);
            ivCategory = itemView.findViewById(R.id.iv_category);
            llCategory = itemView.findViewById(R.id.ll_category);
        }
    }

    public interface onItemClickListener {
        void onItemClick(String category, String categoryName, int position, View view);
    }

    public void setOnItemClickListener(CategoriesListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}