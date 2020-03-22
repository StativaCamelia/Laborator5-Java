package com.company;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.velocity.*;
import org.apache.velocity.app.*;
import org.apache.velocity.tools.generic.RenderTool;

public class ReportMultipleCommand implements Command{
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    ReportMultipleCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void executeCommand() {
        for (String par : parameters) {
            try(Writer fWrite = new FileWriter("template.vm")) {
                Catalog cat = CatalogUtil.load(par);
                Velocity.init();
                Template template = Velocity.getTemplate("template.vm");
                VelocityContext context = new VelocityContext();
                context.put("parameters", cat.getDocuments());

                Writer writer = new StringWriter();
                template.merge(context, writer);
                fWrite.write(writer.toString());

            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
