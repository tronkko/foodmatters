package kuopio.foodmatters;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class ResultsActivity extends BaseActivity {
    private EditText energyField;
    private EditText fatField;
    private EditText carbohydrateField;
    private EditText sugarField;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_results);

        /* Get parameters from calling activity */
        Bundle extras = getIntent ().getExtras ();

        /* Set view */
        setContentView (R.layout.activity_results);

        /* Set window title */
        setTitle ("Statistics");

        /* Show home button */
        //getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        //getSupportActionBar ().setHomeButtonEnabled (true);

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

}
