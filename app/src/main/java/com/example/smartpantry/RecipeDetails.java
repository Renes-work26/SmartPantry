package com.example.smartpantry;

import android.widget.TextView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.model.Recipe;

public class RecipeDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        long recipeId = getIntent().getLongExtra(SuggestedRecipes.EXTRA_RECIPE_ID, -1);
        Recipe recipe = DatabaseHelper.getInstance(this).getRecipeById(recipeId);

        TextView nameView = findViewById(R.id.text_recipe_name);
        TextView ingredientsView = findViewById(R.id.text_recipe_ingredients);
        TextView stepsView = findViewById(R.id.text_recipe_steps);

        if (recipe == null) {
            nameView.setText("Recipe not found");
            return;
        }
        setTitle(recipe.getName());
        nameView.setText(recipe.getName());
        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientsText.append(". ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getName())
                    .append("\n");
        }
        ingredientsView.setText(ingredientsText.toString().trim());
        stepsView.setText(recipe.getSteps());
    }
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) return String.valueOf((long) quantity);
        return String.valueOf(quantity);
    }
}