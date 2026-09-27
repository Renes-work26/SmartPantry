package com.example.smartpantry.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

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

    }
}