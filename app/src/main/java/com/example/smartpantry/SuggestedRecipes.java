package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.IngredientMatcher;

import java.util.List;

public class SuggestedRecipes extends BaseActivity implements RecipeAdapter.Listener {
    private TextView emptyView;
    private RecipeAdapter adapter;
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_recipes);
        setTitle("Suggested Recipes");
        emptyView = findViewById(R.id.text_empty_recipes);

        RecyclerView recyclerView = findViewById(R.id.recycler_suggested);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        recyclerView.setAdapter(adapter);

        setupBottomNavigation(R.id.nav_recipes);
    }
    @Override
    protected void onResume() {
        super.onResume();
        runMatching();
    }
    private void runMatching() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        List<Recipe> allRecipes = dbHelper.getAllRecipesWithIngredients();
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> suggested = IngredientMatcher.getSuggestedRecipes(allRecipes, pantry);
        adapter.setItems(suggested);
        emptyView.setVisibility(suggested.isEmpty() ? View.VISIBLE : View.GONE);
    }
    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent intent = new Intent((this), RecipeDetails.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
