package com.mygreenbank.mygreenbank;

/**
 * Created by BenJamin on 09-Sep-17.
 */
import android.content.Context;

import com.mygreenbank.mygreenbank.Tree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class Plot {

    String name;
    double[] geotag =  {0,0};
    List<Tree> trees;
    String Csv;
    public Plot(String name){
        this.name = name;
        this.trees = new ArrayList<Tree>(); {
        }
    }

    public void addTree(Tree newTree){
        this.trees.add(newTree);
    }
    public List<Tree> getTrees(){
        return trees;
    }
    public void setTag( double[] geotag){
        this.geotag = geotag;
    }

    public double[] getTag(){
        return this.geotag;
    }

    public String getCSV(){
        String result = name+","+geotag[0]+","+geotag[1]+";";
        for(Tree a :trees){
            result+=a.getCsv();
        }

       return result;
    }


    public void setCSV(String csv){
        this.Csv = csv;
        String[] data = csv.split(";");
        String[] data2 = data[0].split(",");
        this.name = data2[0];
        this.geotag[0] =  Double.parseDouble(data2[1]);
        this.geotag[1] =  Double.parseDouble(data2[2]);
        for(int x = 1; x<data.length; x++){
            String[] dataT = data[x].split(",");
            this.trees.add(new Tree(Float.parseFloat(dataT[1]),Float.parseFloat(dataT[2])));
            this.trees.get(x-1).setInt(x);
        }
    }


 }
