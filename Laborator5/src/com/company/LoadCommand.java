package com.company;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadCommand implements Command {
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    LoadCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void executeCommand()
    throws InvalidCatalogException, IOException, ClassNotFoundException
    {
            for(String parameter : this.parameters) {
                Catalog cat = new Catalog("new", parameter);
                if(parameter.endsWith("ser")) {
                    try {
                        FileInputStream fis = new FileInputStream(parameter);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        cat = (Catalog) ois.readObject();
                    } catch (IOException io) {
                        System.out.println("Error reading file");
                        io.printStackTrace();
                    } catch (ClassNotFoundException cn) {
                        System.out.println("Error loading treets");
                        cn.printStackTrace();
                    }
                    System.out.println(cat + " a fost incarcat cu succes");
                }
                else if(parameter.endsWith("json")){
                    try {
                        Gson gson = new Gson();
                        Reader readerJ = new FileReader(parameter);
                        cat = gson.fromJson(readerJ, Catalog.class);
                    }
                    catch (IOException io){
                        System.out.println("Error reading file");
                        io.printStackTrace();
                    }
                    System.out.println(cat + " a fost incarcat cu succes");
                }
            }
    }



}
