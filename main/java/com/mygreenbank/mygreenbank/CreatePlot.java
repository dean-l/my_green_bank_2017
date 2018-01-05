package com.mygreenbank.mygreenbank;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CreatePlot extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Marker mMarker;
    private Marker tempMarker;
    private List<Marker> plotMarkers;
    private DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private Context con = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plot);

        setNavigationViewListener();
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button mShowDialog = (Button) findViewById(R.id.createButton);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarker == null) {
                    Toast.makeText(CreatePlot.this, "Place Marker", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreatePlot.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_create_plot, null);
                    final EditText mPlotName = (EditText) mView.findViewById(R.id.plotName);
                    Button mCreate = (Button) mView.findViewById(R.id.createPlotName);

                    mCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!mPlotName.getText().toString().isEmpty()) {
                                Plot newplot = new Plot(mPlotName.getText().toString());
                                double[] geotag =  {mMarker.getPosition().latitude,mMarker.getPosition().longitude};
                                newplot.setTag(geotag);
                                ProgramState.plotList.add(newplot);
                                ProgramState.writePlot("Plots.save", con);
                                Intent i = new Intent(CreatePlot.this, ManagePlot.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(CreatePlot.this,
                                        R.string.invalid_plot_name, Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            }
        });
        try{
            for (Plot plot: ProgramState.plotList){
                tempMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(plot.geotag[0],plot.geotag[0])));
                plotMarkers.add(tempMarker);
            }
        } catch(Exception e){}
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]
                        {
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                        }, 10);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        //Latitude and Longitude of Laos
        LatLng laosLatLng = new LatLng(19.85, 102.49);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(laosLatLng, 6));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent h = new Intent(CreatePlot.this, MainActivity.class);
                startActivity(h);
                return true;
            case R.id.menu_tree_calculation:
                Intent i = new Intent(CreatePlot.this, TreeCalculation.class);
                startActivity(i);
                return true;
            case R.id.menu_create_plot:
                Intent j = new Intent(CreatePlot.this, CreatePlot.class);
                startActivity(j);
                return true;
            case R.id.menu_manage_plot:
                Intent k = new Intent(CreatePlot.this, ManagePlot.class);
                startActivity(k);
                return true;
            case R.id.menu_settings:
                Intent l = new Intent(CreatePlot.this, Settings.class);
                startActivity(l);
                return true;
        }
        mDrawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNavigationViewListener(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
