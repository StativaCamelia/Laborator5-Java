package com.company;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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

import javax.print.Doc;

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

    /***
     * Incarcam catalogul pentru care dorim sa realizam raportul.Acest lucru se realizeaza, din nou, particularizat fie pentru .ser, fie pentru .json
     * Initializam un motor "completare" a unui template si ii asociem un template care are extensia vm si este realizat de noi .
     * Ulterior creem un context cu ajutorul caruia putem adauga numele documentelor din catalog in template-ul asociat.
     * Metoda put asociata clasei VelocityContext care primeste drept prim argument numele variabilei pe care trebuie sa o gaseasca in template,
     * si respectiv valoare pe care o asociaza acestei variabile.Continutul raportului este afisat in consola,
     * dar in acelasi timp se realizeaza si un document html cu acesta.
     * @throws InvalidCatalogException
     */

    private void createHtmlReport() throws InvalidCatalogException{
        for (String par : parameters.subList(1, parameters.size())) {
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
                    System.out.println("Dccoumentul" + getFileName(par,"html")+" created");
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
                    System.out.println("Dccoumentul" + getFileName(par,"html")+" created");
                    System.out.println(stringWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * Dupa ce incarcam catalogul, incepem sa construit structura raportului, respectiv inpartirea pe coloane si componentele care
     * apar in cadrul acestui, iar informatia a fost adaugata folosind un obiect de tipul DRDDataSource in care
     * am adaugat numele documentelor din catalogul pentru care incercam sa realizam catalogul.
     * @throws InvalidCatalogException
     */

    private void createPdfReport()throws InvalidCatalogException{
        for (String par : parameters.subList(1, parameters.size()))
            if(par.endsWith("ser")) {
                try {
                    Catalog catalog = CatalogUtil.load(par);
                    report().columns(col.column("Document", "document", type.stringType()))
                            .title(cmp.text("Report"))
                            .pageFooter(cmp.pageXofY())
                            .setDataSource(createDataSource(catalog))
                            .show().toPdf(new FileOutputStream(getFileName(par, "pdf")));
                    System.out.println("Dccoumentul" + getFileName(par,"pdf")+" created");
                } catch (ClassNotFoundException | IOException | DRException e) {
                    e.printStackTrace();
                }
            }
        else if(par.endsWith("json")){
            try {Catalog catalog = CatalogUtil.loadPlainText(par);
                report().columns(col.column("Document", "document", type.stringType()))
                        .title(cmp.text("Report"))
                        .pageFooter(cmp.pageXofY())
                        .setDataSource(createDataSource(catalog))
                        .show().toPdf(new FileOutputStream(getFileName(par, "pdf")));
                System.out.println("Dccoumentul" + getFileName(par,"pdf")+" created");
            } catch (IOException | DRException e) {
            e.printStackTrace();
            }
        }
    }

    private JRDataSource createDataSource(Catalog cat){
        DRDataSource dataSource = new DRDataSource("document");
        for( Document doc: cat.getDocuments())
            dataSource.add(doc.toString());
        return dataSource;
    }

    /**
     * Pentru a realiza aceasta metoda am folosit modulul Apache POI.Din nou, incarcam catalogul in functie de extensie.
     * Continutul documentul cu extensia doc va fi reprezentat de un obiect de tipul XWPFDocument. Ulterior in cadrul acestui document putem
     * creea paragrafe in cadrul acetui document, iar paragrafele la randul lor pot fi dizivate in obiecte de tipul XWPFRun, respectiv zone care
     * au aceleasi proprietati. Intr-un astfel de obiect XWPFRun, setam continutul raportului , care este reprezentat de documentele existente
     * in acesta.
     * @throws InvalidCatalogException
     */
    public void createWord() throws InvalidCatalogException{
        for (String par : parameters.subList(1, parameters.size())){
            if(par.endsWith("ser")) {
                try {
                    Catalog catalog = CatalogUtil.load(par);
                    XWPFDocument document = new XWPFDocument();

                    FileOutputStream out = new FileOutputStream(new File(getFileName(par, "docx")));

                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();

                    for(Document doc: catalog.getDocuments()){
                        run.setText(doc.toString()+"\n");
                    }
                    document.write(out);

                    out.close();
                    System.out.println("Dccoumentul" + getFileName(par,"docx")+" created");
                }
                catch (IOException| ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
            else if(par.endsWith("json")){
                try {
                    Catalog catalog = CatalogUtil.loadPlainText(par);
                    XWPFDocument document = new XWPFDocument();

                    FileOutputStream out = new FileOutputStream(new File(getFileName(par, "docx")));

                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();

                    for(Document doc: catalog.getDocuments()){
                        run.setText(doc.toString()+"\n");
                    }
                    document.write(out);

                    out.close();
                    System.out.println("Dccoumentul" + getFileName(par,"docx")+" created");
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void executeCommand() {
        if(parameters.get(0).equals("html")){
            createHtmlReport();
        }
        else if(parameters.get(0).equals("pdf")){
            createPdfReport();
        }
        else if(parameters.get(0).equals("doc")){
            createWord();
        }
    }

}
