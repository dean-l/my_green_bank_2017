package com.mygreenbank.mygreenbank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mygreenbank.mygreenbank.Plot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ManagePlot extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ExpandableListView.OnChildClickListener{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private int plotNo=999;
    private int treeNo=999;
    int menuName=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_plot);

        setNavigationViewListener();
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawLayout, R.string.open, R.string.close);

        mDrawLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        plotToListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(this);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuName, menu);
    }

    private void plotToListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<ArrayList<String>> insert = new ArrayList<ArrayList<String>>();
        DecimalFormat df = new DecimalFormat("####0.00");
        for (int i=0; i<ProgramState.plotList.size();i++){
            listDataHeader.add(ProgramState.plotList.get(i).name);
            insert.add(new ArrayList<String>());
            for (int x=0; x<ProgramState.plotList.get(i).trees.size();x++){
                Tree current = ProgramState.plotList.get(i).trees.get(x);
                insert.get(i).add(getString(R.string.tree)+current.id+", "+ current.getDiameter()+getString(R.string.cm_girth)+df.format(current.getMerch())+getString(R.string.m_mheight));
            }
            listDataChild.put(listDataHeader.get(i),insert.get(i));
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent h = new Intent(ManagePlot.this, MainActivity.class);
                startActivity(h);
                return true;
            case R.id.menu_tree_calculation:
                Intent i = new Intent(ManagePlot.this, TreeCalculation.class);
                startActivity(i);
                return true;
            case R.id.menu_create_plot:
                Intent j = new Intent(ManagePlot.this, CreatePlot.class);
                startActivity(j);
                return true;
            case R.id.menu_manage_plot:
                Intent k = new Intent(ManagePlot.this, ManagePlot.class);
                startActivity(k);
                return true;
            case R.id.menu_settings:
                Intent l = new Intent(ManagePlot.this, Settings.class);
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


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //TODO Delete and cancel prompts
        plotNo = groupPosition;
        treeNo = childPosition;
        menuName = R.menu.delete;
        registerForContextMenu(expListView);
        openContextMenu(expListView);
        return true;
    }
    public boolean onContextItemSelected(MenuItem item) {
        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.option1:
                doOptionOne();
                return true;
            case R.id.option2:
                doOptionTwo();
                return true;
            case R.id.option0:
                doOptionZero();
                return true;
            case R.id.option01:
                doOptionZeroConfirm();
                return true;
            default:
                return false;
        }
    }

            //method to execute when option one is chosen
    private void doOptionOne() {
        // preparing list data
        if(plotNo<900&&treeNo<900) {
            ProgramState.plotList.get(plotNo).getTrees().remove(treeNo);
            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            plotToListData();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
            ProgramState.writePlot("Plots.save", this);
        }

        plotNo = 999;
        treeNo = 999;

    }

    //method to execute when option two is chosen
    private void doOptionTwo() {
        plotNo = 999;
        treeNo = 999;
    }
    private void doOptionZero() {
        if(plotNo<900&&treeNo<900) {
            menuName = R.menu.deleteconfirm;
            closeContextMenu();
            registerForContextMenu(expListView);
            openContextMenu(expListView);
        }
    }
    private void doOptionZeroConfirm() {
        if(plotNo<900&&treeNo<900) {
            ProgramState.plotList.remove(plotNo);
            expListView = (ExpandableListView) findViewById(R.id.lvExp);
            plotToListData();
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(this);
            ProgramState.writePlot("Plots.save", this);
        }

        plotNo = 999;
        treeNo = 999;

    }
}
