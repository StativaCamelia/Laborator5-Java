package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Main {

    public static void main(String[] args) throws IOException{
	Main app = new Main();
	app.testCreateSave();

	/*OPTIONAL SALVARE PLAIN TEXT*/
	app.testCreateSaveJson();
	app.testLoadJson();

	//OPTIONAL shell
        String commandFull = new String();
        String commandName = new String();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

	while(true){
        System.out.println("Introduceti comanda:");
        commandFull = console.readLine();
        String[] commandParse = commandFull.split(" ");
        commandName = commandParse[0];

        if(commandName.toUpperCase().equals("LOAD")){

            Command newCommand = new LoadCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (InvalidCatalogException|IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else if(commandName.toUpperCase().equals("LIST")){
            Command newCommand = new ListCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (InvalidCatalogException|IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        else if(commandName.toUpperCase().equals("VIEW")){
            Command newCommand = new ViewCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (InvalidCatalogException|IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        else if(commandName.toUpperCase().equals("REPORT_HTML")){
            Command newCommand = new ReportCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (InvalidCatalogException| IOException| ClassNotFoundException e){
                e.printStackTrace();
            }
            }
        else if(commandName.toUpperCase().equals("INFO")){
            Command newCommand = new InfoCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else if(commandName.toUpperCase().equals("REPORT")){
            Command newCommand = new ReportMultipleCommand(Arrays.copyOfRange(commandParse, 1, commandParse.length));
            try {
                newCommand.executeCommand();
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else
            throw new InvalidCommand("Comanda introdusa nu exista");
	}
    }



    /**
     * Creeaza un nou catalog si adauga un document in acesta
     * Apoi salveaza local catalogul cu ajutorul functiei save definita in clasa CatalogUtil
     */
    private void testCreateSave(){
        Catalog catalog =
                new Catalog("Java Resources", "D:/JAVA/Laborator5/Laborator5Java/catalog.ser");
        Document doc1 = new Document("java1", "Java Course 1", "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        Document doc2 = new Document("java2", "Java Course 1", "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        doc1.addTag("type", "Slides");

        try {
            catalog.addDoc(doc1);
            catalog.addDoc(doc2);
        }
        catch (DuplicateNameException dne){
            dne.printStackTrace();
        }

        try {
            CatalogUtil.save(catalog);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void testCreateSaveJson(){
        Catalog catalog = new Catalog("Java Resources", "D:/JAVA/Laborator5/Laborator5Java/catalog.json");
        Document doc = new Document("java1", "Java Course 1", "https://profs.info.uaic.ro/~acf/java/slides/en/intro_slide_en.pdf");
        doc.addTag("type", "Slides");

        try {
            catalog.addDoc(doc);
        }
        catch (DuplicateNameException e){
            e.printStackTrace();
        }

        try {
            CatalogUtil.savePlaintext(catalog);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    
    public void testLoadJson(){
        Catalog catalog = new Catalog();
        try {
            catalog = CatalogUtil.loadPlainText("D:/JAVA/Laborator5/Laborator5Java/catalog.json");
        }
        catch(IOException| InvalidCatalogException e) {
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
            catalog = CatalogUtil.load("D:/JAVA/Laborator5/Laborator5Java/catalog.ser");
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
