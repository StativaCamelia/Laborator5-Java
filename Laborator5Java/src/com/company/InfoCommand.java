package com.company;

import com.company.Catalog;
import com.company.Command;
import com.company.Document;
import com.company.InvalidCatalogException;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class InfoCommand implements Command {
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    InfoCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Folosim modulul Apache Tika facem urmatorele operatii pentru a afla metadatele corespunzatoare unui file dat drept argument:
     * instantiem un parser de tipul autodetect.
     * Metoda de parsarea in vederea extragerii de metadate are nevoie de urmatorele argumente:
     * stream(File-ul din care dorim sa extragem metadate instantiat ca un Stream de date),
     * handler(transmite evenimente care apar pe timpul parsarii documentului, cum ar fi inceputul, sfarsitul acestuia etc),
     * metadata(un obiect de tipul Metadata in care sunt stocate informatiile obtinute in urma parsarii)
     * context(ofera parser-ului informatii din contextul actual care contribuie la customizarea informatiilor).
     */
    @Override
    public void executeCommand(){
    for(String par: parameters){
        try {
            File file = new File(par);

            Parser parser = new AutoDetectParser();
            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();

            InputStream inputStream = new FileInputStream(file);
            ParseContext context = new ParseContext();

            parser.parse(inputStream, handler, metadata, context);

            System.out.println(handler.toString());

            String[] metadataNames = metadata.names();

            for (String name : metadataNames) {
                System.out.println(name + ": " + metadata.get(name));
            }
        }
        catch (IOException| SAXException|TikaException e){
            e.printStackTrace();
        }

    }
    }
}
