package com.mygreenbank.mygreenbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setNavigationViewListener();
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setNavigationViewListener(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent h = new Intent(AboutUs.this, MainActivity.class);
                startActivity(h);
                return true;
            case R.id.menu_tree_calculation:
                Intent i = new Intent(AboutUs.this, TreeCalculation.class);
                startActivity(i);
                return true;
            case R.id.menu_create_plot:
                Intent j = new Intent(AboutUs.this, CreatePlot.class);
                startActivity(j);
                return true;
            case R.id.menu_manage_plot:
                Intent k = new Intent(AboutUs.this, ManagePlot.class);
                startActivity(k);
                return true;
            case R.id.menu_settings:
                Intent l = new Intent(AboutUs.this, Settings.class);
                startActivity(l);
                return true;
        }
        mDrawLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
