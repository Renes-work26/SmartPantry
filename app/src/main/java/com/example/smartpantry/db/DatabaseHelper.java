package com.example.smartpantry.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.smartpantry.model.PantryItem;
import android.content.Context;
import android.database.Cursor;

import com.example.smartpantry.model.PantryItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static  final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;
    //pantry table
    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "_ID";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY +" ("+
                COL_PANTRY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QTY + "REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
    }
    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.insert(TABLE_PANTRY, null, values);
    }
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null,
                COL_PANTRY_NAME + " ASC");
        while(cursor.moveToNext()) {
            items.add(cursorToPantryItem(cursor));
        }
        cursor.close();
        return items;
    }
    private PantryItem cursorToPantryItem(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_PANTRY_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_NAME));
        double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QTY));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT));
        String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY));
        return new PantryItem(id, name, quantity, unit, expiry);
    }
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.update(TABLE_PANTRY, values, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }
    public void deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_PANTRY_ID +"=?",
                new String[]{String.valueOf(id)});
    }
    public PantryItem getPantryItem(long id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_PANTRY, null, COL_PANTRY_ID +"=?",
                new String[]{String.valueOf(id)}, null, null, null);

        PantryItem item =null;
        if (cursor.moveToFirst()) {
            item = cursorToPantryItem(cursor);
        }
        cursor.close();
        return item;
    }
}