package com.example.root.inventoryappstage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.inventoryappstage1.data.InventoryDbHelper;
import com.example.root.inventoryappstage1.data.InventoryContract.ProductEntry;

public class MainActivity extends AppCompatActivity {

    //Tag for Logs
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryData();

        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertData();
                queryData();
            }
        });

        final Button dropButton = findViewById(R.id.dropButton);
        dropButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dropData();
                queryData();
            }
        });
    }


    private void insertData() {
        //Insert into database.
        //Bring up db object in writable way
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Dummy data values into ContentValues object
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Android CookBook");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2000);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 5);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Supplier 1");
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "48 555-333-444");

        //Put content values into DataBase
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error adding saving product", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Product added with ID:" + newRowId, Toast.LENGTH_LONG).show();
        }
    }

    private void queryData() {
        //Read from database.
        //Remember to close cursor... (try final block)
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        //Open database in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //String array of columns to read
        String[] projection = {
                BaseColumns._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE
        };

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView testDisplay = (TextView) findViewById(R.id.testTextView);
        try {
            //Test display for Inventory database
            testDisplay.setText("Numbers of inventory item:" + cursor.getCount() + "\n\n");
            testDisplay.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + "\n"
            );

            // Check the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);


            while (cursor.moveToNext()) {
                //Use cursor index to extract data from tabel
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                float currentFloatPrice = currentPrice / 100;
                String currentRealPrice = String.format("%.02f", currentFloatPrice);

                testDisplay.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentQuantity + "pcs. - " +
                        currentRealPrice + "$"
                ));

            }
        } finally {
            //Close cursor when done
            cursor.close();
        }
    }

    private void dropData() {
        //Drop all existing data and make fresh empty tabel
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(InventoryDbHelper.SQL_DELETE_ENTRIES);
        db.execSQL(InventoryDbHelper.SQL_CREATE_ENTRIES);

        Toast.makeText(this, "All data dropped...", Toast.LENGTH_LONG).show();

    }
}
