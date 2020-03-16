package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;

public class CatalogUtil {

    public static void save(Catalog catalog) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(catalog.getPath()))) {
            oos.writeObject(catalog);
        }
    }

    public static Catalog load(String path)
    {
        Catalog cat = new Catalog("new", path);
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis);)
        {cat = (Catalog) ois.readObject();}
        catch (IOException io){
            System.out.println("Error reading file");
            io.printStackTrace();
        }catch(ClassNotFoundException cn){
            System.out.println("Error loading treets");
            cn.printStackTrace();
        }
        return cat;
}

}

