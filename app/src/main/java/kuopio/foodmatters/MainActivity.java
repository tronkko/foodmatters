package kuopio.foodmatters;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import java.util.ArrayList;

/*
 * Display list of consumed foods.
 *
 * Based on sample scanning app from
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    static final int FOOD_REQUEST = 1;

    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private Button scanButton;
    private TextView contentText;
    private ListView foodList;
    private ArrayAdapter<Product> foodAdapter;

    private DatabaseHandler db;

    /*
     * Initialize application.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        /* Initialize database connection */
        db = new DatabaseHandler (this);
        app.loadProducts (db);

        /* Set initial view */
        setContentView (R.layout.activity_main);

        /* Set up drawer (navigation menu) */
        initializeDrawer ();

        /* Initialize scan button */
        scanButton = (Button) findViewById (R.id.scan_button);
        scanButton.setOnClickListener (this);

        /* Initialize text elements */
        contentText = (TextView) findViewById (R.id.scan_content);

        /* Initialize list */
        foodList = (ListView) findViewById (R.id.list_view);

        /* Initialize foods */
        ArrayList<Product> foodContents = app.getProducts ();

        /* Define list adapter */
        foodAdapter = new ArrayAdapter<Product> (
            /* Context */ this,
            /* Row layout */ android.R.layout.simple_list_item_1,
            /* TextView */ android.R.id.text1,
            /* Array of data */ foodContents
        );

        /* Assign adapter to ListView */
        foodList.setAdapter (foodAdapter);

        /* Handle click in list view */
        foodList.setOnItemClickListener(
            new AdapterView.OnItemClickListener () {

            @Override
            public void onItemClick(
                AdapterView<?> parent, View view, int position, long id)
            {

                /* Get clicked product */
                Product p = (Product) foodList.getItemAtPosition (position);

                /* Open product in food view */
                Intent foodIntent = new Intent (MainActivity.this, FoodActivity.class);
                Bundle params = new Bundle ();
                params.putString ("title", "Add Food Item");
                params.putString ("name", p.name);
                params.putString ("manufacturer", p.manufacturer);
                params.putDouble ("size", p.size);
                params.putDouble ("quantity", p.quantity);
                params.putString ("unit", p.unit);
                params.putString ("barcode", p.barcode);
                params.putDouble ("energy", p.energy);
                params.putDouble ("fat", p.fat);
                params.putDouble ("carbohydrate", p.carbohydrate);
                params.putDouble ("sugar", p.sugar);
                foodIntent.putExtras (params);
                startActivityForResult (foodIntent, FOOD_REQUEST);

            }

        });
    }

    /*
     * Define options menu.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.main_options, menu);
        return true;
    }

    /*
     * Handle button presses on options menu.
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        boolean ok;

        int id = item.getItemId ();
        switch (id) {
        case R.id.statistics:
            /* Open results view */
            Intent resultsIntent = new Intent (MainActivity.this, ResultsActivity.class);
            Bundle params = new Bundle ();
            params.putString ("title", "Add Food Item");
            params.putString ("name", "");
            params.putString ("manufacturer", "");
            params.putDouble ("size", 123.0);
            params.putDouble ("quantity", 11.0);
            params.putString ("unit", "g");
            params.putString ("barcode", "");
            params.putDouble ("energy", 123.0);
            params.putDouble ("fat", 555.0);
            params.putDouble ("carbohydrate", 234.3);
            params.putDouble ("sugar", 234.3);
            resultsIntent.putExtras (params);
            startActivityForResult (resultsIntent, FOOD_REQUEST);
            ok = true;
            break;

        default:
            /* Navigation menu */
            if (drawerToggle.onOptionsItemSelected (item)) {
                ok = true;
            } else {
                /* Option menu */
                ok = super.onOptionsItemSelected (item);
            }
        }
        return ok;
    }

    /*
     * Respond to button presses.
     */
    public void onClick (View v) {
        /* Get identifier of clicked button */
        int id = v.getId ();

        /* Act on button*/
        switch (id) {
        case R.id.scan_button:
            /* Scan image */
            IntentIntegrator scanIntegrator = new IntentIntegrator (this);
            scanIntegrator.initiateScan ();
            break;

        default:
            /* Invalid button */
            Toast.makeText(
                getApplicationContext (),
                "Invalid button " + id,
                Toast.LENGTH_LONG
            ).show ();
        }
    }

    /*
     * Retrieve result of scan.
     */
    public void onActivityResult(
        int requestCode, int resultCode, Intent data)
    {
        /* Got bar code? */
        IntentResult r = IntentIntegrator.parseActivityResult(
            requestCode, resultCode, data);
        if (r != null) {

            /* Yes, retrieve barcode */
            String barcode = r.getContents ();
            if (barcode != null) {

                /* Got barcode */
                contentText.setText (barcode);

                /* Retrieve product data using barcode */
                Product p = app.findProduct (barcode);

                /* Open food view */
                Intent foodIntent = new Intent (this, FoodActivity.class);
                Bundle params = new Bundle ();
                params.putString ("title", "Add Food Item");
                params.putString ("name", p.name);
                params.putString ("manufacturer", p.manufacturer);
                params.putDouble ("size", p.size);
                params.putDouble ("quantity", p.quantity);
                params.putString ("unit", p.unit);
                params.putString ("barcode", barcode);
                params.putDouble ("energy", p.energy);
                params.putDouble ("fat", p.fat);
                params.putDouble ("carbohydrate", p.carbohydrate);
                params.putDouble ("sugar", p.sugar);
                foodIntent.putExtras (params);
                startActivityForResult (foodIntent, FOOD_REQUEST);

            }

        } else {
            
            /* Handle other activities */
            if (requestCode == FOOD_REQUEST) {
                if (resultCode == RESULT_OK) {

                    /* Read product info */
                    Bundle result = data.getExtras ();
                    Product p = new Product ();
                    p.name = result.getString ("name");
                    p.manufacturer = result.getString ("manufacturer");
                    p.size = result.getDouble ("size");
                    p.quantity = result.getDouble ("quantity");
                    p.unit = result.getString ("unit");
                    p.barcode = result.getString ("barcode");
                    p.energy = result.getDouble ("energy");
                    p.fat = result.getDouble ("fat");
                    p.carbohydrate = result.getDouble ("carbohydrate");
                    p.sugar = result.getDouble ("sugar");

                    /* Update foods consumed */
                    app.saveProduct (db, p);

                    /* Update view */
                    foodAdapter.notifyDataSetChanged ();

                    /* Format confirmation message */
                    String msg = "Added "
                        + String.valueOf (p.quantity)
                        + " x " + p.name;

                    /* Confirmation message */
                    Toast.makeText(
                        getApplicationContext (),
                        msg,
                        Toast.LENGTH_SHORT
                    ).show ();
                }
            } else {

                /* Unhandled result */
                Toast.makeText(
                    getApplicationContext (),
                    "Invalid activity result!",
                    Toast.LENGTH_SHORT
                ).show ();

            }

        }
    }

    /*
     * Initialize drawer / navigation menu for activity.
     */
    public void initializeDrawer () {
        /* Initialize navigation options */
        drawerList = (ListView) findViewById (R.id.navList);
        String[] options = {
            "Add Restaurant Meal",
            "Add Custom Food Item",
        };
        drawerAdapter = new ArrayAdapter<String> (
            /* Context */ this,
            /* Row layout */ android.R.layout.simple_list_item_1,
            /* TextView */ android.R.id.text1,
            /* Data */ options
        );
        drawerList.setAdapter (drawerAdapter);

        /* Define drawer actions */
        drawerList.setOnItemClickListener(
            new AdapterView.OnItemClickListener ()
        {

            @Override
            public void onItemClick (
                    AdapterView<?> parent, View view, int position, long id)
            {
                Intent foodIntent;
                Bundle params = new Bundle ();

                switch (position) {
                case 0:
                    /* Add restaurant meal */
                    foodIntent = new Intent (MainActivity.this, FoodActivity.class);
                    params.putString ("title", "Add Restaurant Meal");
                    params.putString ("name", "Restaurant Meal");
                    params.putString ("manufacturer", "");
                    params.putDouble ("size", 1.0);
                    params.putDouble ("quantity", 1.0);
                    params.putString ("unit", "pcs");
                    params.putString ("barcode", "");
                    params.putDouble ("energy", 560.0);
                    params.putDouble ("fat", 30.0);
                    params.putDouble ("carbohydrate", 47.0);
                    params.putDouble ("sugar", 8.0);
                    foodIntent.putExtras (params);
                    startActivityForResult (foodIntent, FOOD_REQUEST);
                    break;

                case 1:
                    /* Add any product manually */
                    foodIntent = new Intent (MainActivity.this, FoodActivity.class);
                    params.putString ("title", "Add Custom Food Item");
                    params.putString ("name", "");
                    params.putString ("manufacturer", "");
                    params.putDouble ("size", 0.0);
                    params.putDouble ("quantity", 0.0);
                    params.putString ("unit", "g");
                    params.putString ("barcode", "");
                    params.putDouble ("energy", 0.0);
                    params.putDouble ("fat", 0.0);
                    params.putDouble ("carbohydrate", 0.0);
                    params.putDouble ("sugar", 0.0);
                    foodIntent.putExtras (params);
                    startActivityForResult (foodIntent, FOOD_REQUEST);
                    break;

                default:
                    /* Invalid menu option */
                    Toast.makeText(
                        getApplicationContext (),
                        "Invalid menu option " + position,
                        Toast.LENGTH_SHORT
                    ).show ();
                }

                /* Close drawer */
                drawerLayout.closeDrawers ();
            }

        });

        /* Grab hold of drawer */
        drawerLayout = (DrawerLayout) findViewById (R.id.drawer_layout);

        /* Open and close drawer */
        drawerToggle = new ActionBarDrawerToggle (
            this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerOpened (View view) {
                super.onDrawerOpened (view);

                /* Bring menu to front so clicks register correctly */
                drawerList.bringToFront ();

                /* Rebuild option menu */
                invalidateOptionsMenu ();
            }

            public void onDrawerClosed (View view) {
                super.onDrawerClosed (view);

                /* Rebuild option menu */
                invalidateOptionsMenu ();
            }
        };

        /* Show drawer button */
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setHomeButtonEnabled (true);
        drawerToggle.setDrawerIndicatorEnabled (true);
        drawerLayout.setDrawerListener (drawerToggle);

    }

    /*
     * Synchronize navigation menu.
     * http://blog.teamtreehouse.com/add-navigation-drawer-android
     */
    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate (savedInstanceState);
        drawerToggle.syncState ();
    }

    /*
     * Update navigation menu when tablet goes from portrait to landscape
     * or vice versa.
     */
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged (newConfig);
        drawerToggle.onConfigurationChanged (newConfig);
    }

}

