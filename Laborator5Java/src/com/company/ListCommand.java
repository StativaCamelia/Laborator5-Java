package com.company;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListCommand implements  Command{
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    ListCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * In functie de extensia catalogului dat ca parametru, respectiv(ser sau json), afiseaza documentele adaugate un lista de documente ale acestuia.
     * Pentru formatul ser:
     * Foloseste obiecte de tip ObjectInputStream pentru a deserializa continutul fisierului si pentru al aduce la forma unui obiect de tipul Catalog
     * din care ulterior poate obtine lista de documente
     * Pentru formatul json:
     * Folosim libraria Gson, respectiv metoda fromJson pentru a obtine catalogul din care provine continutul fisierului dat drept parametru.
     * @throws InvalidCatalogException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void executeCommand() throws InvalidCatalogException, IOException, ClassNotFoundException {
        for(String parameter : this.parameters) {
            Catalog cat = new Catalog("new", parameter);
            if (parameter.endsWith("ser")) {

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
                System.out.println(cat.getDocuments());
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
                System.out.println(cat.getDocuments());
            }
        }
    }
}
