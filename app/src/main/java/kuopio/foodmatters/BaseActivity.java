package kuopio.foodmatters;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Common properties for all activities.
 */
public class BaseActivity extends AppCompatActivity {
    protected MyApplication app;

    /*
     * Initialize activity
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        /* Reference to main application */
        app = (MyApplication) getApplication ();
    }

}
