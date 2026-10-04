package com.example.smartpantry;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        if (nav == null) return;

        nav.setSelectedItemId(selectedItemId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return true;

            Intent intent;
            if (id == R.id.nav_pantry) {
                intent = new Intent(this, PantryList.class);
            } else if (id == R.id.nav_recipes) {
                intent = new Intent(this, SuggestedRecipes.class);
            } else {
                intent = new Intent(this, Settings.class);
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        });
    }
}