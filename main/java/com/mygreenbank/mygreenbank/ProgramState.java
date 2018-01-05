package com.mygreenbank.mygreenbank;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import com.mygreenbank.mygreenbank.Plot;

/**
 * Created by BenJamin on 07-Oct-17.
 */

public class ProgramState {

    public static Tree treeStore;
    public static Plot plotStore;
    public static String save;
    public static List<Plot> plotList;
    public static double[] geotag;
    public static int plotIndex = 0;
    public static int done = 0;


    public static String getPlots(String filename, Context context){
        FileInputStream fstream;
        FileOutputStream wstream;
        if (done ==1){
            return "loaded";
        }
        save= "";
        try {
            File file = context.getFileStreamPath(filename);
            if(file == null || !file.exists()) {
                try{
                    wstream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    wstream.write("".getBytes());
                    wstream.close();
                    return "file created";
                } catch (Exception a){
                    return a.getMessage();
                }
            }
            fstream = context.openFileInput(filename);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(fstream));
            String line = null;
            while ((line = bReader.readLine()) != null) {
                save = save.concat(line);
            }
            loadPlots(save,context,filename);
            done = 1;
            return save;
        } catch (Exception e){
            return e.getMessage();
        }
    }
    public static void loadPlots (String plots, Context context, String filename){
        plotList = new ArrayList<Plot>();
        FileOutputStream wstream;
        try {
            plots = plots.replace("\n", "").replace("\r", "");
            String[] data = plots.split("!");
            for (int x = 0; x < data.length; x++) {
                plotList.add(new Plot("name"));
                plotList.get(x).setCSV(data[x]);
            }
        }catch (Exception e) {
            try{
            // wstream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            // wstream.write("".getBytes());
            // wstream.close();
            }catch (Exception a) {}
        }
    }
    public static String savePlots (){
        ProgramState.save="";
        for(int x = 0; x<plotList.size(); x++){
           ProgramState.save += plotList.get(x).getCSV() + "!";
        }
        return save;
    }
    public static String writePlot(String filename, Context context){
        savePlots();
        FileOutputStream wstream;
        try{
            wstream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            wstream.write(ProgramState.save.getBytes());
            wstream.close();
            return context.getString(R.string.saved_to);
        } catch (Exception a){
            return a.getMessage();
        }
    }
    public static void setIndex(int a){
        ProgramState.plotIndex = a;
    }
}
