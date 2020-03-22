package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalog implements Serializable {
    private String name;
    private String path;
    private List<Document> documents = new ArrayList<>();

    Catalog(){

    }

    Catalog(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getPath() {
        return path;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void addDoc(Document doc) throws DuplicateNameException {
        for(Document document: this.documents) {
            if(document.equals(doc))
                throw new DuplicateNameException();
        }
            documents.add(doc);
    }

    /**
     * Folosind stream-uri cauta un document in functie de id-ul acestuia in cazul in care nu exista va returna null
     * @param id
     * @return
     */
    public Document findById(String id){
        return documents.stream().filter(d->d.getId().equals(id)).findFirst().orElse(null);
    }

    public String toString(){
        return this.name;
    }

}
