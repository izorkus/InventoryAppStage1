package com.example.root.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.root.inventoryappstage1.data.InventoryContract.ProductEntry;

public class InventoryDbHelper extends SQLiteOpenHelper{

        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "InventoryDatabase.db";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                        ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NON NULL, " +
                        ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NON NULL, " +
                        ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NON NULL, " +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT, " +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
    }