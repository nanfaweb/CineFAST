package com.example.a1_smd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast_snacks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SNACKS = "snacks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    public SnackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_SNACKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_IMAGE + " TEXT)";
        db.execSQL(createTableQuery);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        insertSnack(db, "Popcorn", "Large / Buttered", 250, "popcorn_img");
        insertSnack(db, "Nachos", "With Cheese Dip", 270, "nachos_img");
        insertSnack(db, "Soft Drink", "Large / Any Flavor", 100, "softdrink_img");
        insertSnack(db, "Candy Mix", "Assorted Candies", 120, "candymix_img");
    }

    private void insertSnack(SQLiteDatabase db, String name, String desc, int price, String imageName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, imageName);
        db.insert(TABLE_SNACKS, null, values);
    }

    public List<Snack> getAllSnacks() {
        List<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SNACKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                Snack snack = new Snack(id, name, desc, price, image);
                snackList.add(snack);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        
        return snackList;
    }
}
