package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.ImplementsDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefinitionInfo {

    private final String name;
    private Definition definition;
    private List<PartialDefinition> partialDefinitions;
    private List<ImplementsDefinition> implementsDefinitions;
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

    public List<PartialDefinition> getPartialDefinitions() {
        return partialDefinitions == null ? Collections.emptyList() : partialDefinitions;
    }

    public List<ImplementsDefinition> getImplementsDefinitions() {
        return implementsDefinitions == null ? Collections.emptyList() : implementsDefinitions;
    }

    public void addPartialDefinition(PartialDefinition definition) {
        if (partialDefinitions == null) {
            partialDefinitions = new ArrayList<>();
        }
        partialDefinitions.add(definition);
    }

    public void addImpementsDefinition(ImplementsDefinition definition) {
        if (implementsDefinitions == null) {
            implementsDefinitions = new ArrayList<>();
        }
        implementsDefinitions.add(definition);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
