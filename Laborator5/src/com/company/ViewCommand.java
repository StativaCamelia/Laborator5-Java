package com.company;

import com.google.gson.Gson;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ViewCommand implements Command{
    private List<String> parameters = new ArrayList<>();

    @Override
    public List<String> getParameters() {
        return this.parameters;
    }

    ViewCommand(String[] parameters){
        for(int i = 0; i< parameters.length; i++){
            this.parameters.add(parameters[i]);
        }
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void executeCommand()
    throws IOException {
        for(String parameter: parameters)
        try {
            Desktop desktop = Desktop.getDesktop();

            boolean isWeb = parameter.startsWith("http://") || parameter.startsWith("https://");
            if(isWeb)
                desktop.browse(new URI(parameter));
            else{
                File file = new File(parameter);
                desktop.open(file);
            }
        } catch (IOException | URISyntaxException io) {
            io.printStackTrace();
        }
    }



}
