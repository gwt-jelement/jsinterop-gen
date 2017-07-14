package com.tenxdev.jsinterop.generator;

import com.tenxdev.jsinterop.generator.model.Definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefinitionInfo {

    private final String name;
    private Definition definition;
    private List<Definition> partials;
    private String packgeName;
    private String filename;
    private List<String> importedPackages;

    public DefinitionInfo(String name) {
        this.name = name;
    }

    public List<String> getImportedPackages() {
        return importedPackages;
    }

    public void setImportedPackages(List<String> importedPackages) {
        this.importedPackages = importedPackages;
    }

    public String getName() {
        return name;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public List<Definition> getPartials() {
        return partials == null ? Collections.emptyList() : partials;
    }

    public void addPartial(Definition definition) {
        if (partials == null) {
            partials = new ArrayList<>();
        }
        partials.add(definition);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
