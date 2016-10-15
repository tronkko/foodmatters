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
 * Display contents of fridge.
 *
 * Based on sample scanning app from
 * https://code.tutsplus.com/tutorials/android-sdk-create-a-barcode-reader--mobile-17162
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private Button scanButton;
    private TextView contentText;
    private ListView fridgeList;
    private ArrayList<String> fridgeContents;
    private ArrayAdapter<String> fridgeAdapter;

    /*
     * Initialize application.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        /* Set initial view */
        setContentView (R.layout.activity_main);

        /* Set up drawer */
        initializeDrawer ();

        /* Initialize scan button */
        scanButton = (Button) findViewById (R.id.scan_button);
        scanButton.setOnClickListener (this);

        /* Initialize text elements */
        contentText = (TextView) findViewById (R.id.scan_content);

        /* Initialize list */
        fridgeList = (ListView) findViewById (R.id.list_view);

        /* Initialize fridge contents */
        fridgeContents = new ArrayList<String> ();
        fridgeContents.add ("Valio rasvaton maito 1 l");

        /* Define list adapter */
        fridgeAdapter = new ArrayAdapter<String>(
            /* Context */ this,
            /* Row layout */ android.R.layout.simple_list_item_1,
            /* TextView */ android.R.id.text1,
            /* Array of data */ fridgeContents
        );

        /* Assign adapter to ListView */
        fridgeList.setAdapter (fridgeAdapter);

        /* Handle click in list view */
        fridgeList.setOnItemClickListener(
            new AdapterView.OnItemClickListener ()
        {

            @Override
            public void onItemClick(
                AdapterView<?> parent, View view, int position, long id) 
            {

                /* ListView Clicked item index */
                int itemPosition = position;

                /* ListView Clicked item value */
                String itemValue = (String) fridgeList.getItemAtPosition (position);

                /* Show Alert */
                Toast.makeText(
                    getApplicationContext (),
                    "Position :" + itemPosition + "  ListItem : " +itemValue,
                    Toast.LENGTH_LONG
                ).show();
            }

        });
    }

    /*
     * Define options menu.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.fridge_options, menu);
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
        case R.id.item1:
            /* FIXME: option 1 */
            Toast.makeText (
                getApplicationContext (),
                "Item 1 Selected",
                Toast.LENGTH_LONG
            ).show ();
            ok = true;
            break;

        case R.id.item2:
            /* FIXME: option 2 */
            Toast.makeText (
                getApplicationContext (),
                "Item 2 Selected",
                Toast.LENGTH_LONG
            ).show ();
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
    public void onClick (View v){
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
            Toast.makeText (
                getApplicationContext (),
                "Invalid button " + id,
                Toast.LENGTH_LONG
            ).show ();
        }
    }

    /*
     * Retrieve result of scan.
     */
    public void onActivityResult (
            int requestCode, int resultCode, Intent intent)
    {
        /* Got bar code? */
        IntentResult r = IntentIntegrator.parseActivityResult(
            requestCode, resultCode, intent);
        if (r != null) {

            /* Yes, retrieve barcode */
            String barcode = r.getContents ();
            contentText.setText (barcode);

            /* Open food view */
            Intent foodIntent = new Intent (this, FoodActivity.class);
            foodIntent.putExtra ("barcode", barcode);
            startActivity (foodIntent);

            /* Get product */
            //String label = getProductLabel (barcode);

            /* Push code to fridge */
            //fridgeContents.add (label);
            //fridgeAdapter.notifyDataSetChanged ();

            /* Confirm scan */
            /*
            Toast.makeText(
                getApplicationContext (),
                "Added " + label,
                Toast.LENGTH_LONG
            ).show ();
            */

        } else {

            /* No bar code in view */
            Toast.makeText(
                getApplicationContext (),
                "No scan data received!",
                Toast.LENGTH_SHORT
            ).show ();

        }
    }

    /*
     * Find label for barcode.
     */
    public String getProductLabel (String barcode) {
        String label;

        switch (barcode) {
        case "6410530071137":
            label = "MediPlast leikattava kangaslaastari 6x50 cm";
            break;

        case "4304493255685":
            label = "Freeway Sugarfree energy drink 0,25 l";
            break;

        case "20643546":
            label = "Huhtahyvä keittokinkku 300 g";
            break;

        case "20092924":
            label = "Crusti Croc Sour Cream & Cheese 200 g";
            break;

        default:
            label = barcode;
        }

        return label;
    }

    /*
     * Initialize drawer / navigation menu for activity.
     */
    public void initializeDrawer () {
        /* Initialize navigation options */
        drawerList = (ListView) findViewById (R.id.navList);
        String[] options = {
            "Shopping Bag",
            "Design a Meal",
            "My Recipes"
        };
        drawerAdapter = new ArrayAdapter<String>(
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
            public void onItemClick(
                AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                case 0:
                    /* FIXME: Unload shopping bag */
                    Toast.makeText(
                        getApplicationContext (),
                        "Shopping Bag",
                        Toast.LENGTH_SHORT
                    ).show ();
                    break;

                case 1:
                    /* FIXME: Design a Meal */
                    Toast.makeText(
                        getApplicationContext (),
                        "Design a Meal",
                        Toast.LENGTH_SHORT
                    ).show ();
                    break;

                case 2:
                    /* FIXME: Food recipes */
                    Toast.makeText(
                        getApplicationContext (),
                        "My Recipes",
                        Toast.LENGTH_SHORT
                    ).show ();
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
        drawerToggle = new ActionBarDrawerToggle(
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
                invalidateOptionsMenu();
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

