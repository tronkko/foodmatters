package kuopio.foodmatters;

import android.support.v7.app.AppCompatActivity;
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

public class FoodActivity extends BaseActivity implements View.OnClickListener {
    private Button eatButton;
    private Button storeButton;
    private Button disposeButton;
    private EditText quantityField;
    private EditText sizeField;
    private Spinner unitField;
    private EditText eanField;
    private EditText energyField;
    private EditText fatField;
    private EditText sugarField;

    /*
     * Initialize food activity.
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        /* Set view */
        setContentView (R.layout.activity_food);

        /* Show home button */
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar ().setHomeButtonEnabled (true);

        /* Get barcode / search term */
        Bundle extras = getIntent ().getExtras ();
        String barcode = extras.getString ("barcode");

        /* Eat button */
        eatButton = (Button) findViewById (R.id.eat_button);
        eatButton.setOnClickListener (this);

        /* Store button */
        storeButton = (Button) findViewById (R.id.store_button);
        storeButton.setOnClickListener (this);

        /* Dispose button */
        disposeButton = (Button) findViewById (R.id.dispose_button);
        disposeButton.setOnClickListener (this);

        /* Quantity */
        quantityField = (EditText) findViewById (R.id.quantity_value);

        /* Package size */
        sizeField = (EditText) findViewById (R.id.size_value);

        /* Unit */
        unitField = (Spinner) findViewById (R.id.unit_value);
        List<String> units = new ArrayList<String> ();
        units.add ("l");
        units.add ("ml");
        units.add ("kg");
        units.add ("g");
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item);
        unitField.setAdapter (unitAdapter);

        /* EAN */
        eanField = (EditText) findViewById (R.id.ean_value);

        /* Energy */
        energyField = (EditText) findViewById (R.id.energy_value);

        /* Fat */
        fatField = (EditText) findViewById (R.id.fat_value);

        /* Sugar */
        sugarField = (EditText) findViewById (R.id.sugar_value);

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

        case android.R.id.home:
            /* Back button */
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
    public void onClick (View v){
        /* Get identifier of clicked button */
        int id = v.getId ();

        /* Act on button*/
        switch (id) {
        case R.id.eat_button:
            /* Consume food */
            Toast.makeText (
                getApplicationContext (),
                "Eat",
                Toast.LENGTH_LONG
            ).show ();
            break;

        case R.id.store_button:
            /* Store to fridge */
            Toast.makeText (
                getApplicationContext (),
                "Store",
                Toast.LENGTH_LONG
            ).show ();
            break;

        case R.id.dispose_button:
            /* Dispose food (another person consumed food / gargage) */
            Toast.makeText (
                getApplicationContext (),
                "Dispose",
                Toast.LENGTH_LONG
            ).show ();
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


}
