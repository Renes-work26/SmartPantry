package com.example.smartpantry;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;

import java.util.Arrays;


public class AddEditIngredientActivity extends AppCompatActivity {
    private static final String[] UNITS = {"pcs", "kg", "g", "ml", "l", "tbsp", "tsp", "cup"};
    private DatabaseHelper dbHelper;
    private EditText inputName, inputQuantity;
    private Spinner inputUnit;
    private long editingItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = DatabaseHelper.getInstance(this);
        inputName= findViewById(R.id.input_name);
        inputQuantity= findViewById(R.id.input_quantity);
        inputUnit= findViewById(R.id.input_unit);
        Button saveButton = findViewById(R.id.button_save);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS);
        inputUnit.setAdapter(unitAdapter);

        editingItemId = getIntent().getLongExtra(PantryListActivity.EXTRA_ITEM_ID, -1);
        if (editingItemId != -1) {
            setTitle("Edit Ingredient");
            populateForEdit(editingItemId);
        }
        else{
            setTitle("Add Ingredient");
        }
        saveButton.setOnClickListener(v -> save());
    }
    private void populateForEdit(long id) {
        PantryItem item = dbHelper.getPantryItem(id);
        if (item==null) return;

        inputName.setText(item.getName());
        inputQuantity.setText(String.valueOf(item.getQuantity()));
        int unitPosition = Arrays.asList(UNITS).indexOf(item.getUnit());
        if (unitPosition >= 0) {
            inputUnit.setSelection(unitPosition);
        }
    }
    private void save() {
        String name = inputName.getText().toString().trim();
        String quantityText = inputQuantity.getText().toString().trim();
        String unit = (String) inputUnit.getSelectedItem();

        if(TextUtils.isEmpty(name)) {
        inputName.setError("Enter an ingredient name");
        return;
        }
        if(TextUtils.isEmpty(quantityText)) {
            inputQuantity.setError("Enter a quantity");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e){
            inputQuantity.setError("Enter a valid number");
            return;
        }
        if (quantity <= 0) {
            inputQuantity.setError("Quantity must be greater than 0");
            return;
        }
        PantryItem item = new PantryItem(editingItemId, name, quantity, unit, null);
        if (editingItemId == -1) {
            dbHelper.addPantryItem(item);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        }
        else {
            dbHelper.updatePantryItem(item);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}