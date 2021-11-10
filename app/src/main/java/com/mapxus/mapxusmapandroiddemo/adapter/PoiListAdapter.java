package com.mapxus.mapxusmapandroiddemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.map.mapxusmap.api.services.model.poi.PoiInfo;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.utils.LocalLanguageUtils;
import com.mapxus.mapxusmapandroiddemo.utils.SetImageByCategoryUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoiListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PoiInfo> poiInfos;
    private String categoryName;
    private String category;
    private onItemClickListener onItemClickListener;

    public PoiListAdapter(List<PoiInfo> poiInfos, String categoryName, String category) {
        this.poiInfos = poiInfos;
        this.categoryName = categoryName;
        this.category = category;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        PoiInfo poiInfo = poiInfos.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvPoiName.setText(LocalLanguageUtils.getLocalLanguageWithPoiInfo(poiInfo));
        viewHolder.tvPoiInfo.setText(String.format("%s · %s", categoryName, poiInfo.getFloor()));
        int catgegoryImage = SetImageByCategoryUtils.setImageByCategory(category.toLowerCase());
        viewHolder.ivCategory.setImageResource(catgegoryImage);
        viewHolder.llCategory.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(poiInfo.getFloor(), viewHolder.tvPoiName.getText().toString().trim(), catgegoryImage, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != poiInfos ? poiInfos.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPoiName, tvPoiInfo;
        public ImageView ivCategory;
        public LinearLayout llCategory;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivCategory = itemView.findViewById(R.id.iv_category);
            llCategory = itemView.findViewById(R.id.ll_category);
            tvPoiName = itemView.findViewById(R.id.tv_poi_name);
            tvPoiInfo = itemView.findViewById(R.id.tv_poi_info);
        }
    }

    public interface onItemClickListener {
        void onItemClick(String floorName, String poiName, int catgegoryImage, int position, View view);
    }

    public void setOnItemClickListener(PoiListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}