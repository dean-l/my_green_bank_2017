package com.mygreenbank.mygreenbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mygreenbank.mygreenbank.Calculator;

import java.io.Console;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class TreeCalculation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener {

    EditText treeDia;
    EditText treeHeight;
    TextView treeVol;
    GridLayout logView;
    Button moreResults;
    String plot = "";
    Tree currentTree;
    private DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    Spinner plotSpinner;
    Spinner currencySpinner;
    ArrayAdapter<String> adapter;



    Button calcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_calculation);

        setNavigationViewListener();
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get id fields from the xml display
        treeDia = (EditText) findViewById(R.id.girth);
        treeHeight = (EditText) findViewById(R.id.totalHeight);
        treeVol = (TextView) findViewById(R.id.totalVolume);
        plotSpinner = (Spinner) findViewById(R.id.selectPlot);
        currencySpinner = (Spinner) findViewById(R.id.selectCurrency);
        logView = (GridLayout) findViewById(R.id.logSizeTable);
        moreResults = (Button) findViewById(R.id.more_results);
        List plotNames = new ArrayList<String>();
        for(Plot name: ProgramState.plotList){
            plotNames.add(name.name);
        }
        List currencies = new ArrayList<String>();
        currencies.add("Laotian Kip");
        currencies.add("US Dollars");
        currencies.add("Australian Dollar");
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, plotNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plotSpinner.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);
        plotSpinner.setOnItemSelectedListener(this);
        currencySpinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        ProgramState.setIndex(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void onButtonClick(View v){
        if(v.getId() == R.id.estimate){
            calculate();
            moreResults.setVisibility(View.VISIBLE);
        }
        if(v.getId() == R.id.addToPlot){
            addToPlot();
        }
        if(v.getId() == R.id.more_results){
            if (logView.getVisibility() == View.VISIBLE) {
                logView.setVisibility(View.INVISIBLE);
            } else {
                logView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void addToPlot(){
        if (plot == null){
            return;
        }
        currentTree = new Tree(Float.parseFloat(treeDia.getText().toString()),Float.parseFloat(treeHeight.getText().toString()));
        ProgramState.plotList.get(ProgramState.plotIndex).addTree(currentTree);
        logView.removeAllViews();
        String volText = "";
        volText = ProgramState.writePlot("Plots.save",this);
        //ProgramState.savePlots();
        volText = volText + ProgramState.plotList.get(ProgramState.plotIndex).name;
        //treeVol.setText(volText);
        Toast.makeText(TreeCalculation.this,
                volText, Toast.LENGTH_LONG).show();

    }

    /* Test method - Calc
    Calculated the volume of a tree given height and diameter
     */
    protected double[][] calculate(){
        Calculator calc = new Calculator();
        float diameter = 0;
        float height = 0;
        float volume = 0;
        String volText = "";
        double[][] output;
        float total=0;

        //add values to variables
        diameter = Float.parseFloat(treeDia.getText().toString());
        height = Float.parseFloat(treeHeight.getText().toString());
        output = calc.calc(diameter,height);
        logView.removeAllViews();

        TableLayout logTable = new TableLayout(this);


        //Add Table Headings
        TableRow rowTitle = new TableRow(this);
        TextView size = new TextView(this);
        //Spacing and sizing cells - column 1
        size.setPadding(5, 5, 5, 5);
        size.setTextSize(24);
        size.setText(R.string.log_size);
        rowTitle.addView(size);
        TextView vol = new TextView(this);
        //Spacing and sizing cells - column 1
        vol.setPadding(5, 5, 5, 5);
        vol.setTextSize(24);
        vol.setText(R.string.tree_value);
        rowTitle.addView(vol);
        logTable.addView(rowTitle);

        Spinner currency = (Spinner) findViewById(R.id.selectCurrency);
        String currencyType = currency.getSelectedItem().toString();


        for (double[] value : output){
            TableRow row = new TableRow(this);
            TextView sizeVal = new TextView(this);
            //Non marketable logs - too small
            if(value[1]<=0){
                sizeVal.setText(R.string.too_small);
            } else if (calc.getNo()>value[0]+1) {
                sizeVal.setText(Double.toString(calc.rangeBracket()[(int)value[0]])+"-"+calc.rangeBracket()[(int)value[0]+1]);
            }else{
                sizeVal.setText(Double.toString(calc.rangeBracket()[(int)value[0]])+"+");
            }
            //cell spacing
            sizeVal.setPadding(5, 5, 5, 5);
            sizeVal.setTextSize(24);
            row.addView(sizeVal);
            TextView volumeVal = new TextView(this);
            if(currencyType == "Laotian Kip") {
                volumeVal.setText("₭" + Double.toString(Math.round(value[1])));
                // volumeVal.setText("₭"+Double.toString(value[3])) ;
            } else if (currencyType == "US Dollars"){
                volumeVal.setText("$" + Double.toString(Math.round(value[1]/8333)));
            } else {
                volumeVal.setText("$" + Double.toString(Math.round(value[1]/6250)));
            }
            volumeVal.setPadding(5, 5, 5, 5);
            volumeVal.setTextSize(24);
            row.addView(volumeVal);
            logTable.addView(row);
            total += value[1];
        }

        logView.addView(logTable);

        //write to screen
        if(currencyType == "Laotian Kip") {
            volText = getString(R.string.total_tree_value)+" ₭" +Double.toString((Math.round(total)));
        } else if (currencyType == "US Dollars"){
            volText = getString(R.string.total_tree_value)+" $" + Double.toString((Math.round(total/8302.10)));
        } else {
            volText = getString(R.string.total_tree_value)+" $" +Double.toString((Math.round(total/6404.78)));
        }
        treeVol.setText(volText);

        return output;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent h = new Intent(TreeCalculation.this, MainActivity.class);
                startActivity(h);
                return true;
            case R.id.menu_tree_calculation:
                Intent i = new Intent(TreeCalculation.this, TreeCalculation.class);
                startActivity(i);
                return true;
            case R.id.menu_create_plot:
                Intent j = new Intent(TreeCalculation.this, CreatePlot.class);
                startActivity(j);
                return true;
            case R.id.menu_manage_plot:
                Intent k = new Intent(TreeCalculation.this, ManagePlot.class);
                startActivity(k);
                return true;
            case R.id.menu_settings:
                Intent l = new Intent(TreeCalculation.this, Settings.class);
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
