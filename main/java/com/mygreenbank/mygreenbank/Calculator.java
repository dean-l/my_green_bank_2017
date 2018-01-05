package com.mygreenbank.mygreenbank; /**
 * Created by BenJamin on 02-Sep-17.
 */

import java.lang.Math;
public class Calculator {
    private static final double A = 0.0022;
    private static final double B = -0.0202;
    private static final double C = 0.5441;
    private double[] rangeValue  = {1000000,1500000,1800000,2000000,2500000};
    private double[] rangeSizes  = {12,16,19,21,30};
    private int rangeNo = 5;
    private static final double LOGLEN  = 2.3;
    private static final double BHEIGHT  = 1.2;
    private static final double STUMP  = .15;
    private static final double BARK  = .9;
    public Calculator(){

    }
    public double[][] calc(double breastGirth, double merchHeight){
        int noLogs = (int) Math.floor((merchHeight)/LOGLEN);
        double[][] totals = new double[noLogs][5];
        double taper  = 0;
        double rH =0;
        double rL =0;
        double breastDiameter = breastGirth/(Math.PI);
        double value = 0;
        double sizeClass = 0;
        for(int i = 0;i<noLogs ;i++){
            if (i==0){
                taper = A*Math.pow((breastDiameter-2*BARK),2)+B*(breastDiameter-2*BARK)+C;
                rH = ((breastDiameter-2*BARK) -(LOGLEN-BHEIGHT+STUMP)*taper)/2;
            } else {
                taper = A*Math.pow(2*rL,2)+B*2*rL+C;
                rH = (2*rL - LOGLEN * taper)/2;
            }
            value = 0;
            for(int a = 0; a < rangeNo; a++){
                if(2*rH>rangeSizes[a]){
                    value = rangeValue[a];
                    sizeClass = a;

                }
            }
            totals[i][0] = sizeClass;
            totals[i][1] = LOGLEN* Math.PI*Math.pow(rH/100,2)*value;
            totals[i][2] = LOGLEN*Math.PI* Math.pow(rH/100,2);
            totals[i][3] = 2*rH;
            totals[i][4] = taper;

            rL = rH;
        }
        return totals;
    }
    public void setRanges(double[] ranges, double[] values){
        rangeValue = new double[ranges.length];
        rangeSizes = new double[ranges.length];
        rangeNo = ranges.length;
        for (int i=0; i<ranges.length; i++){
            rangeSizes[i] = ranges[i];
            rangeValue[i] = values[i];
        }
    }
    public double[] rangeBracket(){
        return rangeSizes;
    }
    public double[] rangeValue(){
        return rangeValue;
    }
    public int getNo(){
        return rangeNo;
    }

}
