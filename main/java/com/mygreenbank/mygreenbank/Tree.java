package com.mygreenbank.mygreenbank;
/**
 * Created by BenJamin on 09-Sep-17.
 */

public class Tree {
    double diameter;
    double merch;
    int id;
    double[][] values;
    public Tree(float diameter, float merch){
        this.diameter = diameter;
        this.merch = merch;
    }

    public double getDiameter(){
        return this.diameter;
    }
    public double getMerch(){
        return this.merch;
    }
    public int getInt(){
        return this.id;
    }
    public void setInt(int id){
        this.id = id;
    }

    public double[][] getValues(){
        return this.values;
    }
    public String getCsv(){
        String result=id+","+Double.toString(diameter)+","+Double.toString(merch)+";";
       return result;
    }
}
