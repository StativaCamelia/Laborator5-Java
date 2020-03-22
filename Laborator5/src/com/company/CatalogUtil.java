package com.company;

import com.google.gson.*;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


public class CatalogUtil {

    /**
     * Creeaza si salveaza un nou fisier local care are path-ul dat ca parametru
     * Catalogul poate fi scris ca un ObjectOutputStream deoarece este serializabil
     * @param catalog
     * @throws IOException
     */

    public static void savePlaintext(Catalog catalog)
    throws IOException
    {
        try{
           Gson gson = new Gson();
           String jsonObj = gson.toJson(catalog);
           Writer writerJ = new FileWriter(catalog.getPath());
           writerJ.write(jsonObj);
           writerJ.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Catalog loadPlainText(String path)
    throws InvalidCatalogException, IOException
    {
        Catalog cat = new Catalog();
        try {
          Gson gson = new Gson();
          Reader readerJ = new FileReader(path);
          cat = gson.fromJson(readerJ, Catalog.class);

        }
        catch (IOException io){
            System.out.println("Error reading file");
            io.printStackTrace();
        }
        return cat;
    }


    public static void save(Catalog catalog)
    throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(catalog.getPath()))) {
            oos.writeObject(catalog);
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Fiind dat path-ul unui catalog il incarca drept un ObjectInputStream si citeste continutul acestuia folosind metoda readObject
     * @param path
     * @return
     * @throws InvalidCatalogException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Catalog load(String path)
    throws InvalidCatalogException, IOException, ClassNotFoundException
    {
        Catalog cat = new Catalog("new", path);
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cat = (Catalog) ois.readObject();
        }
        catch (IOException io){
            System.out.println("Error reading file");
            io.printStackTrace();
        }catch(ClassNotFoundException cn){
            System.out.println("Error loading treets");
            cn.printStackTrace();
        }
        return cat;
    }

    /**
     * Fiind dat un document incearca sa il deschida cu ajutorul modului Desktop, daca path-ul acestuia este un URI va folosi metoda browse in caz contrar daca este un document
     * local va folosi metoda desktop.open;
     * @param doc
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void view(Document doc)
    throws IOException, URISyntaxException {
        try {
            Desktop desktop = Desktop.getDesktop();
            String path = doc.getLocation();

            boolean isWeb = path.startsWith("http://") || path.startsWith("https://");
            if(isWeb)
                desktop.browse(new URI(path));
            else{
                File file = new File(doc.getLocation());
                desktop.open(file);
            }
        } catch (IOException | URISyntaxException io) {
            io.printStackTrace();
        }
    }

}

