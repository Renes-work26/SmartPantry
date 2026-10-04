package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    public interface Listener{
        void onRecipeClicked(Recipe recipe);
    }
    private final Listener listener;
    private List<Recipe> items = new ArrayList<>();
    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }
    public void setItems(List<Recipe>items) {
        this.items = items;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = items.get(position);
        holder.name.setText(recipe.getName());
        holder.subtitle.setText(recipe.getIngredients().size() + "ingredients");
        holder.itemView.setOnClickListener(v -> listener.onRecipeClicked(recipe));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, subtitle;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_recipe_name);
            subtitle = itemView.findViewById(R.id.text_recipe_subtitle);
        }
    }
}
