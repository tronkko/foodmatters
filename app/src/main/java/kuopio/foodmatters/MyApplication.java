package kuopio.foodmatters;

import android.app.Application;

import java.util.ArrayList;

/**
 * Global functions.
 */
public class MyApplication extends Application {
    private ArrayList<Product> foodContents;

    /*
     * Initialize application.
     */
    @Override
    public void onCreate () {
        super.onCreate ();

        /* Initialize foods consumed */
        foodContents = new ArrayList<Product> ();
    }

    /*
     * Get global data.
     */
    public ArrayList<Product> getProducts () {
        return foodContents;
    }

    /*
     * Find product by barcode.
     */
    public Product findProduct (String barcode) {
        Product p = null;

        /* Find product from consumed products */
        for (int i = 0; i < foodContents.size (); i++) {
            Product q = (Product) foodContents.get (i);
            if (q.barcode.equals (barcode)) {
                /* Found from consumed foods */
                p = q;
                break;
            }
        }

        /* Find default product from database */
        if (p == null) {
            p = _findProduct (barcode);
        }
        return p;
    }
    protected Product _findProduct (String barcode) {
        Product p = new Product ();

        switch (barcode) {
        case "6410530071137":
            p.name = "Leikattava kangaslaastari";
            p.manufacturer = "MediPlast";
            p.size = 0.0;
            p.quantity = 0.0;
            p.unit = "pcs";
            p.barcode = "6410530071137";
            p.energy = 0.0;
            p.fat = 0.0;
            p.carbohydrate = 0.0;
            p.sugar = 0.0;
            break;

        case "4304493255685":
            p.name = "Sugarfree energy drink";
            p.manufacturer = "Freeway";
            p.size = 0.25;
            p.quantity = 0.0;
            p.unit = "l";
            p.barcode = "4304493255685";
            p.energy = 40.0;
            p.fat = 0.0;
            p.carbohydrate = 11.2;
            p.sugar = 11.1;
            break;

        case "5060088500972":
            p.name = "Juiced energy caffeine + taurine + vitamins";
            p.manufacturer = "Freeway";
            p.size = 0.25;
            p.quantity = 0.0;
            p.unit = "l";
            p.barcode = "5060088500972";
            p.energy = 40.0;
            p.fat = 0.0;
            p.carbohydrate = 11.2;
            p.sugar = 11.1;
            break;

        case "20643546":
            p.name = "Keittokinkku";
            p.manufacturer = "Huhtahyvät";
            p.size = 300.0;
            p.quantity = 0.0;
            p.unit = "g";
            p.barcode = "20643546";
            p.energy = 94.0;
            p.fat = 2.5;
            p.carbohydrate = 0.7;
            p.sugar = 0.6;
            break;

        case "20092924":
            p.name = "Sour Cream & Cheese -perunalastu";
            p.manufacturer = "Crusti Croc";
            p.size = 200;
            p.quantity = 0.0;
            p.unit = "g";
            p.barcode = "20092924";
            p.energy = 540.0;
            p.fat = 34.0;
            p.carbohydrate = 50.5;
            p.sugar = 3.3;
            break;

        case "20642693":
            p.name = "Meetvursti";
            p.manufacturer = "Huhtahyvät";
            p.size = 200;
            p.quantity = 0.0;
            p.unit = "g";
            p.barcode = "20092924";
            p.energy = 540.0;
            p.fat = 34.0;
            p.carbohydrate = 50.5;
            p.sugar = 3.3;
            break;

        default:
            /* Empty product */
            p.name = "";
            p.manufacturer = "";
            p.size = 0;
            p.quantity = 0.0;
            p.unit = "g";
            p.barcode = barcode;
            p.energy = 0;
            p.fat = 0;
            p.carbohydrate = 0;
            p.sugar = 0;
        }
        return p;
    }

    /*
     * Load consumed foods from database.
     */
    public void loadProducts (DatabaseHandler db) {
        /* Clear existing contents */
        foodContents.clear ();

        /* Load products from database */
        ArrayList<Product> arr = db.loadProducts ();

        /* Insert products from database to existing array */
        for (int i = 0; i < arr.size (); i++) {
            Product p = (Product) arr.get (i);
            if (p.quantity > 0.0001) {
                foodContents.add (p);
            }
        }
    }

    /*
     * Save product.
     */
    public void saveProduct (DatabaseHandler db, Product p) {
        /* Save product to database */
        db.saveProduct (p);

        /* Re-load database to memory */
        loadProducts (db);
    }

}
