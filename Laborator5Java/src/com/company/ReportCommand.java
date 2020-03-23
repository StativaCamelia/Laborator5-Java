package com.company;

import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Writer;
import java.io.BufferedWriter;

public class ReportCommand implements Command{

    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    ReportCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    private void writeHtmlToFile(String location, String content){
        File file = new File(location);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(location))) {
            writer.write(content);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /***
     * In functie de extensie deschidem catalogul folosind una din metodele load sau loadPlaintext
     * Instantiem apoi un obiect de tipul HtmlReport al carui constructor primeste drept parametru Catalogul pentru care dorim sa realizam raportul
     * Si apelam metoda creatHtmlReport corespunzatore obiectului tocmai instantia, apoi preluam raportul in obiectul HtmlReport.
     * Reportul il scriem apoi intr-un file cu extensia html pe care il salvam in Sistemul Local de Fisiere.
     * @throws IOException
     */
    @Override
    public void executeCommand()
    throws IOException {
        for(String parameter: parameters) {
            if (parameter.endsWith("ser")) {
                try {
                    Catalog catalog = CatalogUtil.load(parameter);
                    HtmlReport report = new HtmlReport(catalog);
                    report.createHtmlReport();
                    System.out.println(report.getHtmlReport());
                    String location = parameter.substring(0,parameter.length()-3) + "html";
                    this.writeHtmlToFile(location, report.getHtmlReport().toString());
                } catch (InvalidCatalogException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else if(parameter.endsWith("json")){
                try{
                    Catalog catalog = CatalogUtil.loadPlainText(parameter);
                    HtmlReport report = new HtmlReport(catalog);
                    report.createHtmlReport();
                    System.out.println(report.getHtmlReport());
                    String location = parameter.substring(0,parameter.length()-3) + "html";

                    this.writeHtmlToFile(parameter, report.getHtmlReport().toString());
                }
                catch (InvalidCatalogException|IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
