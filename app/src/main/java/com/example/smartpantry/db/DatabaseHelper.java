package com.example.smartpantry.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import android.content.Context;
import android.database.Cursor;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static  final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;
    //pantry table
    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "_id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    //recipe table
    public static final String  TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "_id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    //recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "_id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "name";
    public static final String COL_RI_QTY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    private DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY +" ("+
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QTY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES +" ("+
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QTY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " +
                TABLE_RECIPES + "(" + COL_RECIPE_ID + ") ON DELETE CASCADE)");

        seedRecipes(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }
    //Create, Read, Update, and Delete.
    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = pantryToValues(item);
        return db.insert(TABLE_PANTRY, null, values);
    }
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = pantryToValues(item);
        return db.update(TABLE_PANTRY, values, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }
    public void deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
    }
    public PantryItem getPantryItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        PantryItem item = null;
        if (c.moveToFirst()) {
            item = cursorToPantryItem(c);
        }
        c.close();
        return item;
    }
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");
        while (c.moveToNext()){
            list.add(cursorToPantryItem(c));
        }
        c.close();
        return list;
    }
    private ContentValues pantryToValues(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return values;
    }
    private PantryItem cursorToPantryItem(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow(COL_PANTRY_ID));
        String name = c.getString(c.getColumnIndexOrThrow(COL_PANTRY_NAME));
        double qty = c.getDouble(c.getColumnIndexOrThrow(COL_PANTRY_QTY));
        String unit = c.getString(c.getColumnIndexOrThrow(COL_PANTRY_UNIT));
        String expiry = c.getString(c.getColumnIndexOrThrow(COL_PANTRY_EXPIRY));
        return new PantryItem(id, name, qty, unit, expiry);
    }
    //Read recipes
    public List<Recipe> getAllRecipesWithIngredients() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        while (c.moveToNext()) {
            Recipe recipe = new Recipe();
            recipe.setId(c.getLong(c.getColumnIndexOrThrow(COL_RECIPE_ID)));
            recipe.setName(c.getString(c.getColumnIndexOrThrow(COL_RECIPE_NAME)));
            recipe.setSteps(c.getString(c.getColumnIndexOrThrow(COL_RECIPE_STEPS)));
            recipe.setIngredients(getIngredientsForRecipe(db, recipe.getId()));
            recipes.add(recipe);
        }
        c.close();
        return recipes;
    }
    public Recipe getRecipeById(long recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        Recipe recipe = null;
        if (c.moveToFirst()) {
            recipe = new Recipe();
            recipe.setId(c.getLong(c.getColumnIndexOrThrow(COL_RECIPE_ID)));
            recipe.setName(c.getString(c.getColumnIndexOrThrow(COL_RECIPE_NAME)));
            recipe.setSteps(c.getString(c.getColumnIndexOrThrow(COL_RECIPE_STEPS)));
            recipe.setIngredients(getIngredientsForRecipe(db, recipeId));
        }
        c.close();
        return recipe;
    }
    private List<RecipeIngredient> getIngredientsForRecipe(SQLiteDatabase db, long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, COL_RI_NAME + " ASC");
        while (c.moveToNext()) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(c.getLong(c.getColumnIndexOrThrow(COL_RI_ID)));
            recipeIngredient.setRecipeId(recipeId);
            recipeIngredient.setName(c.getString(c.getColumnIndexOrThrow(COL_RI_NAME)));
            recipeIngredient.setQuantity(c.getDouble(c.getColumnIndexOrThrow(COL_RI_QTY)));
            recipeIngredient.setUnit(c.getString(c.getColumnIndexOrThrow(COL_RI_UNIT)));
            ingredients.add(recipeIngredient);
        }
        c.close();
        return ingredients;
    }
    //seed data
    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Tomato and Garlic Pasta",
                "1. Cook the pasta until al dente.\n2. Saute onion and garlic, then add canned tomatoes and oregano.\n3. Simmer into a sauce and toss with the pasta.",
                new Object[][]{{"Pasta", 200, "g"}, {"Canned tomatoes", 2, "pcs"}, {"Garlic", 3, "pcs"}, {"Onion", 1, "pcs"}, {"Oregano", 1, "tsp"}});

        addRecipe(db, "Soy Fried Rice",
                "1. Saute onion and garlic in hot pan.\n2. Add cold cooked rice and soy sauce, stir-fry well.\n3. Push rice aside, scrable the egg, then mix through.",
                new Object[][]{{"Rice", 2, "cup"}, {"Onion", 1, "pcs"}, {"Garlic", 2, "pcs"}, {"Soy sauce", 1, "tbsp"}, {"Egg", 2, "pcs"}});

        addRecipe(db, "Garlic Butter Noodles",
                "1. Cook the pasta until al dente.\n2. Melt butter and gently cook minced garlic.\n3. Toss the pasta through with cracked black pepper.",
                new Object[][]{{"Pasta", 200, "g"}, {"Butter", 2, "tbsp"}, {"Garlic", 3, "pcs"}, {"Black pepper", 1, "tsp"}});

        addRecipe(db, "Tomato Soup",
                "1. Saute onion and garlic.\n2. Add canned tomatoes and broth, then simmer and blend.\n3. Finish with a swirl of butter.",
                new Object[][]{{"Canned tomatoes", 2, "pcs"}, {"Broth", 1, "cup"}, {"Garlic", 2, "pcs"}, {"Onion", 1, "pcs"}, {"Butter", 1, "tbsp"}});

        addRecipe(db, "Bean stew",
                "1. Saute onion and garlic.\n2. Add beans, tomato paste, brith, cumin, and paprika.\n3. Simmer until thickened.",
                new Object[][]{{"Beans", 1, "cup"}, {"Tomato paste", 1, "tbsp"}, {"Garlic", 2, "pcs"}, {"Onion", 1, "pcs"}, {"Broth", 1, "cup"}, {"Cumin", 1, "tsp"}, {"Paprika", 1, "tsp"}});

        addRecipe(db, "French omelette",
                "1. Whisk the eggs with salt and pepper.\n2. Melt butter in a pan over low heat.\n3. Cook the eggs gently, folding, until just set.",
                new Object[][]{{"Egg", 3, "pcs"}, {"Butter", 1, "tbsp"}, {"Salt", 1, "tsp"}, {"Black pepper", 1, "tsp"}});

        addRecipe(db, "Scrambled eggs and caramelised onions",
                "1. Cook onion slowly in butter until deeply browned and sweet.\n2. Whisk the eggs.\n3. Scramble the eggs into the onions until softly set.",
                new Object[][]{{"Egg", 3, "pcs"}, {"Onion", 2, "pcs"}, {"Butter", 1, "tbsp"}});

        addRecipe(db, "Flatbread",
                "1. Mix flour, water, oil, and salt into dough.\n2. Roll out thinly.\n3. Pan-sear on both sides until lightly charred.",
                new Object[][]{{"Flour", 1, "cup"}, {"Water", 1, "cup"}, {"Oil", 1, "tbsp"}, {"Salt", 1, "tsp"}});

        addRecipe(db, "Oat pancakes",
                "1. Mix oats with water, egg, salt, and pepper.\n2. Let the batter sit briefly.\n3. Fry spoonfuls until golden on both sides.",
                new Object[][]{{"Oats", 1, "cup"}, {"Water", 1, "cup"}, {"Egg", 1, "pcs"}, {"Salt", 1, "tsp"}, {"Black pepper", 1, "tsp"}});

        addRecipe(db, "Cheese Toast",
                "1. Butter the bread.\n2. Add cheese.\n3. Toast until golden and the cheese melts.",
                new Object[][]{{"Bread", 2, "pcs"}, {"Cheese", 2, "pcs"}, {"Butter", 1, "tbsp"}});

        addRecipe(db, "Banana pancakes",
                "1. Mash the banana.\n2. Mix with flour, egg and milk into a batter.\n3. Cook spoonfuls on a hot pan until golden.",
                new Object[][]{{"Banana", 2, "pcs"}, {"Flour", 1, "cup"}, {"Egg", 1, "pcs"}, {"Milk", 1, "cup"}});

        addRecipe(db, "Chicken curry",
                "1. Brown the chicken.\n2. Saute onion and tomato.\n3. Add curry powder and simmer intil cooked through.",
                new Object[][]{{"Chicken", 500, "g"}, {"Onion", 2, "pcs"}, {"Tomato", 2, "pcs"}, {"Curry Powder", 2, "tbsp"}});

        addRecipe(db, "Potato wedges",
                "1. Cut the potato into wedges.\n2. Toss with oil, salt and paprika.\n3. Bake until crisp.",
                new Object[][]{{"Potato", 3, "pcs"}, {"Oil", 2, "tbsp"}, {"Salt", 1, "tsp"}, {"paprika", 1, "tsp"}});

        addRecipe(db, "Peanut butter toast",
                "1. Toast the bread.\n2. Spread peanut butter evenly.",
                new Object[][]{{"Bread", 2, "pcs"}, {"Peanut butter", 2, "tbsp"}});

        addRecipe(db, "Egg and Avo toast",
                "1. Fry egg in  hot pan.\n2. Toast bread until golden.\n3. Mash avocado with a pinch of salt and black pepper.\n4. Spread avocado on bread and add fried egg on top.",
                new Object[][]{{"Egg", 2, "pcs"}, {"Bread", 2, "pcs"}, {"Oil", 2, "tbsp"}, {"Avocado", 1, "pcs"}, {"Salt", 1, "tsp"}, {"Black pepper", 1, "tsp"}});

        addRecipe(db, "fruit salad",
                "1. Chop a banana, apple, and orange.\n2. Combine in a bowl.\n3. Drizzle with honey and yogurt.",
                new Object[][]{{"Banana", 1, "pcs"}, {"Apple", 1, "pcs"}, {"Orange", 1, "pcs"}, {"Honey", 1, "tbsp"}, {"Yogurt", 2, "tsp"}});
    }

    private void addRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (Object[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(COL_RI_RECIPE_ID, recipeId);
            ingredientValues.put(COL_RI_NAME, (String) ingredient[0]);
            ingredientValues.put(COL_RI_QTY, ((Number) ingredient[1]).doubleValue());
            ingredientValues.put(COL_RI_UNIT, (String) ingredient[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
}