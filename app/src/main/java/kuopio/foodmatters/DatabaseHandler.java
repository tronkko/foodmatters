package kuopio.foodmatters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tronkko on 16.10.2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    /* Version number of database */
    private static final int DATABASE_VERSION = 1;

    /*
     * Create database helper class.
     */
    public DatabaseHandler (Context context) {
        super (context, "foodmatters", null, DATABASE_VERSION);
    }

    /*
     * Load consumed foods from database.
     */
    public ArrayList<Product> loadProducts () {
        ArrayList<Product> products = new ArrayList<Product> ();

        /* Get database handle */
        SQLiteDatabase db = this.getWritableDatabase ();

        /* Select all records from database */
        /* FIXME: Restrict the search to past 30 days */
        Cursor cursor = db.rawQuery(
            "SELECT * FROM consumption", null
        );

        /* Loop through rows */
        if (cursor.moveToFirst ()) {
            do {

                /* Create product out of database record */
                Product p = new Product ();

                p.name = cursor.getString(
                    cursor.getColumnIndex ("name"));

                p.manufacturer = cursor.getString(
                    cursor.getColumnIndex ("manufacturer"));

                p.size = cursor.getDouble(
                    cursor.getColumnIndex ("size"));

                p.quantity = cursor.getDouble(
                    cursor.getColumnIndex ("quantity"));

                p.unit = cursor.getString(
                    cursor.getColumnIndex ("unit"));

                p.barcode = cursor.getString(
                    cursor.getColumnIndex ("barcode"));

                p.energy = cursor.getDouble(
                    cursor.getColumnIndex ("energy"));

                p.fat = cursor.getDouble(
                    cursor.getColumnIndex ("fat"));

                p.carbohydrate = cursor.getDouble(
                    cursor.getColumnIndex ("carbohydrate"));

                p.sugar = cursor.getDouble(
                    cursor.getColumnIndex ("sugar"));

                /* Update foods consumed */
                boolean insert = true;
                for (int i = 0; i < products.size (); i++) {
                    Product q = (Product) products.get (i);
                    if (q.barcode.equals (p.barcode)
                            && q.name.equals (p.name)
                            && Math.abs (q.size - p.size) < 0.0001
                            && Math.abs (q.energy - p.energy) < 0.0001) {

                        /* Update existing product */
                        q.manufacturer = p.manufacturer;
                        q.quantity += p.quantity;
                        q.unit = p.unit;
                        q.fat = p.fat;
                        q.carbohydrate = p.carbohydrate;
                        q.sugar = p.sugar;
                        insert = false;

                    }
                }
                if (insert) {
                    /* Add product */
                    products.add (p);
                }

            } while (cursor.moveToNext ());

            /* Close cursor */
            cursor.close ();
        }

        /* Close database connection */
        db.close ();
        return products;
    }

    /*
     * Insert product to database.
     */
    public void saveProduct (Product p) {
        /* Get database handle */
        SQLiteDatabase db = this.getWritableDatabase ();

        /* Get current date */
        SimpleDateFormat fmt = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String now = fmt.format (new Date ());

        /* Prepare database record */
        ContentValues values = new ContentValues ();
        values.put ("name", p.name);
        values.put ("manufacturer", p.manufacturer);
        values.put ("size", p.size);
        values.put ("quantity", p.quantity);
        values.put ("unit", p.unit);
        values.put ("barcode", p.barcode);
        values.put ("energy", p.energy);
        values.put ("fat", p.fat);
        values.put ("carbohydrate", p.carbohydrate);
        values.put ("sugar", p.sugar);
        values.put ("created", now);

        /* Insert record to database */
        db.insert ("consumption", null, values);

        /* Close database connection */
        db.close();
    }

    /*
     * Create database tables.
     */
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE consumption ( "
            + "  id INTEGER PRIMARY KEY,"
            + "  name VARCHAR(255) NOT NULL DEFAULT '',"
            + "  manufacturer VARCHAR(255) NOT NULL DEFAULT '',"
            + "  size DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  quantity DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  unit VARCHAR(10) NOT NULL DEFAULT 'g',"
            + "  barcode VARCHAR(20) NOT NULL DEFAULT '',"
            + "  energy DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  fat DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  carbohydrate DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  sugar DECIMAL(10,2) NOT NULL DEFAULT 0,"
            + "  created DATETIME NOT NULL"
            + ")"
        );
    }

    /*
     * Upgrade database.
     */
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        /* Drop old tables */
        db.execSQL("DROP TABLE IF EXISTS consumption");

        /* Create tables again */
        onCreate (db);
    }
}
