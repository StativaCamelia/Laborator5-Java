package com.company;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.security.krb5.internal.crypto.Des;

import javax.print.attribute.URISyntax;
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

