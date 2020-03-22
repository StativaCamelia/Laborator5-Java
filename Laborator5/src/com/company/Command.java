package com.company;

import java.io.IOException;
import java.util.List;

public interface Command{
    List<String> getParameters();
    void executeCommand() throws InvalidCatalogException, IOException, ClassNotFoundException;
}
