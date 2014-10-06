package com.lcrojano.trayectoria_gps_lcrojano;

import com.example.promapp.DatabaseHelper;
import com.example.promapp.MenuPrincipalFragment;
import com.example.promapp.SimulatorHelper;
import com.example.trayectoria_gps_lcrojano.R;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private SharedPreferences prefs;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         getApplicationContext();
 		 prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
         menuPrincipal = new MenuPrincipalFragment();       
         FragmentManager fm = getSupportFragmentManager();  
         fm.beginTransaction()        	
           .add(R.id.container, menuPrincipal, "menu_principal")
           .addToBackStack(null)
           .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
