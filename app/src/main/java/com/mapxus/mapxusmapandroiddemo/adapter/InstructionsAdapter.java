package com.mapxus.mapxusmapandroiddemo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapxus.map.mapxusmap.api.map.MapxusMapContext;
import com.mapxus.map.mapxusmap.api.services.model.planning.InstructionDto;
import com.mapxus.mapxusmapandroiddemo.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<InstructionDto> instructionDtos;

    private int currentPosition = -1;

    public InstructionsAdapter(List<InstructionDto> instructionDtos) {
        this.instructionDtos = instructionDtos;
    }

    public void notifyCurrentPosition(int position) {
        if (currentPosition != position) {
            currentPosition = position;
            notifyDataSetChanged();
        }

    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructions_recycleview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {

        InstructionDto instructionDto = instructionDtos.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if (currentPosition != -1 && position == currentPosition) {
            viewHolder.instructionItem.setBackgroundColor(MapxusMapContext.getContext().getColor(android.R.color.holo_blue_light));
            viewHolder.tvText.setTextColor(Color.WHITE);
            viewHolder.tvDistance.setTextColor(Color.WHITE);
        } else {
            viewHolder.instructionItem.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.tvText.setTextColor(Color.BLACK);
            viewHolder.tvDistance.setTextColor(Color.BLACK);
        }

        viewHolder.tvText.setText(instructionDto.getText());
        viewHolder.tvDistance.setText(String.format("%sm", (int) Math.floor(instructionDto.getDistance())));
    }

    @Override
    public int getItemCount() {
        return (null != instructionDtos ? instructionDtos.size() : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout instructionItem;
        public TextView tvText, tvDistance;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tv_instruction_text);
            tvDistance = itemView.findViewById(R.id.tv_instruction_distance);
            instructionItem = itemView.findViewById(R.id.instruction_item);
        }
    }
}