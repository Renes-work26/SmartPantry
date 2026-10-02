package com.example.smartpantry.util;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientMatcher {
    public static String normalizeName(String rawName) {
        if (rawName == null) return "";
        String name = rawName.trim().toLowerCase();
        name = name.replaceAll("[^a-z ]", "");
        name = name.trim().replaceAll("\\s+", " ");

        if (name.endsWith("oes") && name.length() > 4) {
            name = name.substring(0, name.length() - 2);
        } else if (name.endsWith("ies") && name.length() > 4) {
            name = name.substring(0, name.length() - 3) + "y";
        } else if (name.endsWith("s") && !name.endsWith("ss") && name.length() > 3) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }
    public static String normalizeUnit(String rawUnit) {
        if (rawUnit == null) return "";
        String u = rawUnit.trim().toLowerCase();
        switch (u) {
            case "kg":
            case"kilogram":
            case "kilograms":
                return "kg";
            case "g":
            case"gram":
            case "grams":
                return "g";
            case "ml":
            case"milliliter":
            case "milliliters":
                return "ml";
            case "tbsp":
            case"tablespoon":
            case "tablespoons":
                return "tbsp";
            case "tsp":
            case"teaspoon":
            case "teaspoons":
                return "tsp";
            case "pcs":
            case"pc":
            case "piece":
                return "pcs";
            case "cup":
            case"cups":
                return "cup";
            default:
                return u;
        }
    }
    private static String familyOf(String unit) {
        switch (unit) {
            case "ml":
            case"l":
            case "tsp":
            case"tbsp":
            case "cup":
                return "VOLUME";
            case "g":
            case"kg":
                return "MASS";
            default:
                return "";
        }
    }
    private static double toBaseUnits(double quantity, String unit) {
        switch (unit) {
            case"kg":
                return quantity * 1000.0;
            case"l":
                return quantity * 1000.0;
            case"tbsp":
                return quantity * 15.0;
            case"tsp":
                return quantity * 5.0;
            case"cup":
                return quantity * 240.0;
            default:
                return quantity;
            }
        }
        private static boolean pantryHasEnough(RecipeIngredient required, List<PantryItem> pantry) {
        String requiredName = normalizeName(required.getName());
        String requiredUnit = normalizeUnit(required.getUnit());
        String requiredFamily = familyOf(requiredUnit);

        double totalAvailable = 0;
        boolean ingredientFound = false;

        for (PantryItem item : pantry) {
            if (!normalizeName(item.getName()).equals(requiredName)) continue;
            ingredientFound = true;

            String pantryUnit = normalizeUnit(item.getUnit());
            String pantryFamily = familyOf(pantryUnit);

            if(!requiredFamily.isEmpty() && requiredFamily.equals(pantryFamily)) {
                totalAvailable += toBaseUnits(item.getQuantity(), pantryUnit);
            }
            else {
                totalAvailable += item.getQuantity();
            }
        }
        if (!ingredientFound) return false;
        double requiredAmount = requiredFamily.isEmpty()
                ? required.getQuantity()
                : toBaseUnits(required.getQuantity(), requiredUnit);
        return totalAvailable + 1e-6 >= requiredAmount;
        }
        public static List<Recipe> getSuggestedRecipes(List<Recipe> recipes, List<PantryItem>pantry) {
        List<Recipe> suggested = new ArrayList<>();

        for (Recipe recipe : recipes) {
            boolean allIngredientsAvailable = true;
            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                if (!pantryHasEnough(ingredient, pantry)) {
                    allIngredientsAvailable = false;
                    break;
                }
            }
            if (allIngredientsAvailable){
                suggested.add(recipe);
            }
        }
        return suggested;
        }
    }