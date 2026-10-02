package com.example.smartpantry.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.smartpantry.model.PantryItem;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantry.R;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    public interface Listener{
        void onItemClicked(PantryItem item);
    }
    private List<PantryItem> items = new ArrayList<>();
    public final Listener listener;
    public PantryAdapter(Listener listener){
        this.listener = listener;
    }

    public void setItems(List<PantryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        PantryItem item = items.get(position);
        holder.name.setText(item.getName());

        String details = formatQuantity(item.getQuantity()) + " " + item.getUnit();
        holder.details.setText(details);
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
    }
    @Override
    public int getItemCount(){
        return items.size();
    }
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity))
            return String.valueOf((long) quantity);
        return String.valueOf(quantity);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, details;

        ViewHolder(View itemView) {
            super(itemView);
                name = itemView.findViewById(R.id.text_item_name);
                details = itemView.findViewById(R.id.text_item_details);
            }
        }
    }