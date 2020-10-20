package com.mapxus.mapxusmapandroiddemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.map.mapxusmap.api.services.model.poi.PoiCategoryInfo;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriesNameInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PoiCategoryInfo> categoryInfos;

    public CategoriesNameInfoAdapter(List<PoiCategoryInfo> categoryInfos) {
        this.categoryInfos = categoryInfos;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycleview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        PoiCategoryInfo categoryInfo = categoryInfos.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvCategory.setText(String.format("%s%s", "category : ", categoryInfo.getCategory()));
        viewHolder.tvTitleEn.setText(String.format("%s%s", "title_en : ", categoryInfo.getTitle().get("en")));
        viewHolder.tvTitleZh.setText(String.format("%s%s", "title_zh : ", categoryInfo.getTitle().get("zh-Hant")));
        viewHolder.tvTitleCn.setText(String.format("%s%s", "title_cn : ", categoryInfo.getTitle().get("zh-Hans")));
    }

    @Override
    public int getItemCount() {
        return (null != categoryInfos ? categoryInfos.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory, tvTitleEn, tvTitleZh, tvTitleCn;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_category);
            tvTitleEn = itemView.findViewById(R.id.tv_title_en);
            tvTitleZh = itemView.findViewById(R.id.tv_title_zh);
            tvTitleCn = itemView.findViewById(R.id.tv_title_cn);
        }
    }
}