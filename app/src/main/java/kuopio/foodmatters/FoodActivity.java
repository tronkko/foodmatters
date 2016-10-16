package kuopio.foodmatters;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

public class FoodActivity extends BaseActivity implements View.OnClickListener {
    private Button storeButton;
    private EditText productField;
    private EditText manufacturerField;
    private EditText sizeField;
    private EditText quantityField;
    private Spinner unitField;
    private EditText eanField;
    private EditText energyField;
    private EditText fatField;
    private EditText carbohydrateField;
    private EditText sugarField;

    /*
     * Initialize food activity.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        /* Set view */
        setContentView (R.layout.activity_food);
        setTitle ("Add Product");

        /* Show home button */
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setHomeButtonEnabled (true);

        /* Store button */
        storeButton = (Button) findViewById (R.id.store_button);
        storeButton.setOnClickListener (this);

        /* Get parameters from calling activity */
        Bundle extras = getIntent ().getExtras ();

        /* Name */
        productField = (EditText) findViewById (R.id.product_value);
        productField.setText (extras.getString ("name"));

        /* Manufacturer */
        manufacturerField = (EditText) findViewById (R.id.manufacturer_value);
        manufacturerField.setText (extras.getString ("manufacturer"));

        /* Package size */
        sizeField = (EditText) findViewById (R.id.size_value);
        double size = extras.getDouble ("size");
        if (size > 0.001) {
            sizeField.setText (String.valueOf (size));
        } else {
            sizeField.setText ("");
        }

        /* Quantity */
        quantityField = (EditText) findViewById (R.id.quantity_value);
        quantityField.setText ("");

        /* Unit */
        String unit = extras.getString ("unit");
        unitField = (Spinner) findViewById (R.id.unit_value);
        List<String> units = new ArrayList<String> ();
        units.add ("l");
        units.add ("ml");
        units.add ("kg");
        units.add ("g");
        units.add ("pcs");
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String> (
            this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        unitField.setAdapter (unitAdapter);
        unitField.setSelection (unitAdapter.getPosition (unit));

        /* EAN */
        eanField = (EditText) findViewById (R.id.ean_value);
        eanField.setText (extras.getString ("barcode"));

        /* Energy */
        energyField = (EditText) findViewById (R.id.energy_value);
        double energy = extras.getDouble ("energy");
        if (energy > 0.001) {
            energyField.setText (String.valueOf (energy));
        } else {
            energyField.setText ("");
        }

        /* Fat */
        fatField = (EditText) findViewById (R.id.fat_value);
        double fat = extras.getDouble ("fat");
        if (fat > 0.001) {
            fatField.setText (String.valueOf (fat));
        } else {
            fatField.setText ("");
        }

        /* Carbohydrates */
        carbohydrateField = (EditText) findViewById (R.id.carbohydrate_value);
        double ch = extras.getDouble ("carbohydrate");
        if (ch > 0.0001) {
            carbohydrateField.setText (String.valueOf (ch));
        } else {
            carbohydrateField.setText ("");
        }

        /* Sugar */
        sugarField = (EditText) findViewById (R.id.sugar_value);
        double sugar = extras.getDouble ("sugar");
        if (sugar > 0.001) {
            sugarField.setText (String.valueOf (sugar));
        } else {
            sugarField.setText ("");
        }
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
            Toast.makeText(
                getApplicationContext (),
                "Item 1 Selected",
                Toast.LENGTH_LONG
            ).show ();
            ok = true;
            break;

        case R.id.item2:
            /* FIXME: option 2 */
            Toast.makeText(
                getApplicationContext (),
                "Item 2 Selected",
                Toast.LENGTH_LONG
            ).show ();
            ok = true;
            break;

        case android.R.id.home:
            /* Back button */
            Intent returnIntent = new Intent ();
            setResult (Activity.RESULT_CANCELED, returnIntent);
            this.finish ();
            ok = true;
            break;

        default:
            /* Navigation menu */
            ok = super.onOptionsItemSelected (item);
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
        case R.id.store_button:
            /* Return product to calling activity */
            try {
                Intent data = new Intent ();
                Bundle params = new Bundle ();

                /* Product name */
                String name = productField.getText ().toString ();
                if (name.equals ("")) {
                    throw new Exception ("Please provide product name");
                }
                params.putString ("name", name);

                /* Manufacturer, may be empty */
                String mf = manufacturerField.getText ().toString ();
                params.putString ("manufacturer", mf);

                /* Package size */
                String size = sizeField.getText ().toString ();
                if (size.equals ("")) {
                    throw new Exception ("Please provide size");
                }
                params.putDouble ("size", Double.parseDouble (size));

                /* Quantity */
                String q = quantityField.getText ().toString ();
                if (q.equals ("")) {
                    q = "1";
                }
                params.putDouble ("quantity", Double.parseDouble (q));

                /* Unit */
                String unit = unitField.getSelectedItem ().toString ();
                params.putString ("unit", unit);

                /* EAN code */
                String barcode = eanField.getText ().toString ();
                params.putString ("barcode", barcode);

                /* Energy content */
                String energy = energyField.getText ().toString ();
                if (energy.equals ("")) {
                    energy = "0";
                }
                params.putDouble ("energy", Double.parseDouble (energy));

                /* Fat content */
                String fat = fatField.getText ().toString ();
                if (fat.equals ("")) {
                    fat = "0";
                }
                params.putDouble ("fat", Double.parseDouble (fat));

                /* Carbohydrates */
                String ch = carbohydrateField.getText ().toString ();
                if (ch.equals ("")) {
                    ch = "0";
                }
                params.putDouble ("carbohydrate", Double.parseDouble (ch));

                /* Sugar */
                String sugar = sugarField.getText ().toString ();
                if (sugar.equals ("")) {
                    sugar = "0";
                }
                params.putDouble ("sugar", Double.parseDouble (sugar));

                /* Return product data */
                data.putExtras (params);
                setResult (Activity.RESULT_OK, data);
                finish ();
            }
            catch (Exception e) {
                Toast.makeText(
                    getApplicationContext (),
                    e.getMessage (),
                    Toast.LENGTH_LONG
                ).show ();
            }
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


}
