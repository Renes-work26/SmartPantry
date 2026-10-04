# Project: Smart Pantry Manager
Is an Android app that helps reduce food waste by tracking ingredients
a user has at home in their pantry and suggesting recipes they can cook using strictly
those ingredients. 

## Required Functionality
- Pantry management: Add, edit, and delete pantry ingredients.
- Pantry list screen backed by a RecyclerView and SQLite.
- Recipe collection: 16 pre-seeded recipes, each with required ingredients and method steps.
- Suggested Recipes screen: that strictly matches recipes against the current pantry.
- Recipe Detail screen: that shows full ingredients and method for a recipe.
- Settings screen: toggle for expiry alerts and a units preference.
- Bottom navigation across Pantry, Recipes, and Settings.
- Basic feedback when there is no recipe match the current pantry.

## Database: SQLite
This app uses SQLite via `SQLiteOpenHelper`, implemented locally on-device.
I chose SQLite because:
- The app's data is personal and single-user, no need for cloud sync.
- It works fully offline, with no setup required beyond installing the app.
- `SQLiteOpenHelper` gives direct control over schema and queries, which
  made it easier to implement and explain the strict ingredient-matching logic.

## How to run App:
1. Open Android Studio (2023.1 or newer recommended).
2. File > Open, select this project's root folder.
3. Let Gradle sync.
4. Run on an emulator or physical device.
5. On first launch, the app automatically seeds its own recipe database —
   no manual setup needed.

## Project structure
- `adapter/` — PantryAdapter, RecipeAdapter (RecyclerView adapters).
- `db/` — DatabaseHelper (SQLite schema, CRUD , and seed data)
- `model/` — PantryItem, Recipe, RecipeIngredient classes.
- `util/` — IngredientMatcher for strict-matching business logic.
- `AddEditIngredient`, `BaseActivity`, `PantryList`, `RecipeDetails`, `Settings`, `SuggestedRecipes`.
- `layout/` - activity_main.xml, activity_pantry_list.xml, activity_recipe_detail.xml,
  add_edit_ingredient, item_pantry.xml, item_recipe.xml, settings.xml, suggested_recipes.xml.
- `menu` - bottom_nav.xml
