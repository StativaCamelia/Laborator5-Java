package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	Main app = new Main();
	app.testCreateSave();
	app.testLoadView();
    }

    private void testCreateSave(){
        Catalog catalog =
                new Catalog("Java Resources", "d:/JAVA/catalog.ser");
        Document doc = new Document("java1", "Java Course 1", "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slides_en.pdf");
        doc.addTag("type", "Slides");
        catalog.add(doc);
        try {
            CatalogUtil.save(catalog);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void testLoadView(){
        Catalog catalog = CatalogUtil.load("d:/JAVA/catalog.ser");
        System.out.println(catalog);
        Document doc = catalog.findById("java1");
    }
}
