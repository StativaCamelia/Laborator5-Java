package com.company;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
	Main app = new Main();
	app.testCreateSave();
	app.testLoadView();
    }

    /**
     * Creeaza un nou catalog si adauga un document in acesta
     * Apoi salveaza local catalogul cu ajutorul functiei save definita in clasa CatalogUtil
     */
    private void testCreateSave(){
        Catalog catalog =
                new Catalog("Java Resources", "d:/JAVA/catalog.ser");
        Document doc = new Document("java1", "Java Course 1", "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        doc.addTag("type", "Slides");
        catalog.add(doc);
        try {
            CatalogUtil.save(catalog);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Incarca un catalog(folosind metoda Load ), cauta un document in acesta si apoi deschide documentul cu o aplicatie nativa a sistemului de operare(functionalitate realizata de
     * metoda view)
     */
    private void testLoadView(){
        Catalog catalog = new Catalog();
        try {
            catalog = CatalogUtil.load("d:/JAVA/catalog.ser");
        }
        catch(IOException| InvalidCatalogException| ClassNotFoundException e) {
            e.printStackTrace();
        }
        Document doc = catalog.findById("java1");
        try {
            CatalogUtil.view(doc);
        }
        catch (IOException|URISyntaxException e){
            e.printStackTrace();
        }


    }
}
