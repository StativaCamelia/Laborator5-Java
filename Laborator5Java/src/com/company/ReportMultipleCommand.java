package com.company;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.xml.sax.SAXException;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class ReportMultipleCommand implements Command {
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    ReportMultipleCommand(String[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    private String getFileName(String location, String extension){
        String fileName = location.substring(0, location.indexOf(".")) + "." + extension;
        return fileName;
    }

    private void createHtmlReport(){
        for (String par : parameters.subList(0, parameters.size())) {
            if(par.endsWith("ser")){
                try(Writer fileWriter = new FileWriter(this.getFileName(par, "html"))) {
                    Catalog cat = CatalogUtil.load(par);
                    VelocityEngine velocityEngine = new VelocityEngine();
                    velocityEngine.init();

                    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
                    velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());

                    Template t = velocityEngine.getTemplate("src/template.vm");

                    VelocityContext velocityContext = new VelocityContext();
                    for (Document d : cat.getDocuments()) {
                        velocityContext.put("par", d.toString());
                    }

                    StringWriter stringWriter = new StringWriter();
                    t.merge(velocityContext, stringWriter);

                    fileWriter.write(stringWriter.toString());
                    System.out.println(stringWriter);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(par.endsWith("json")){
                try(Writer fileWriter = new FileWriter(getFileName(par,"html"))) {
                    Catalog cat = CatalogUtil.loadPlainText(par);
                    VelocityEngine velocityEngine = new VelocityEngine();
                    velocityEngine.init();

                    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
                    velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());

                    Template t = velocityEngine.getTemplate("src/template.vm");

                    VelocityContext velocityContext = new VelocityContext();
                    for (Document d : cat.getDocuments()) {
                        velocityContext.put("par", d.toString());
                    }

                    StringWriter stringWriter = new StringWriter();
                    t.merge(velocityContext, stringWriter);

                    fileWriter.write(stringWriter.toString());
                    System.out.println(stringWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createPdfReport(){
        try{
            report().columns(col.column("Document", "document", type.stringType()))
                    .title(cmp.text("Report"))
                    .pageFooter(cmp.pageXofY())
                    .setDataSource(createDataSource())
                    .show();
        }
    }

    @Override
    public void executeCommand() {
        if(parameters.get(0).equals("html")){
            createHtmlReport();
        }
    }

}
