package com.example.smartpantry;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PantryListActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = DatabaseHelper.getInstance(this);
        emptyView = findViewById(R.id.text_empty_pantry);

        RecyclerView recyclerView = findViewById(R.id.recycler_pantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_item);
        fab.setOnClickListener(v -> Toast.makeText(this, "Add screen",
                Toast.LENGTH_SHORT).show());
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }
    private void loadPantry() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        adapter.setItems(items);
        emptyView.setVisibility(items.isEmpty()? View.VISIBLE : View.GONE);
    }
    @Override
    public void onItemClicked(PantryItem item){
        Toast.makeText(this, "Edit screen" + item.getName(), Toast.LENGTH_SHORT).show();
    }
}