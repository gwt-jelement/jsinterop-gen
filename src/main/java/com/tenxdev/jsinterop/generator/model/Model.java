package com.tenxdev.jsinterop.generator.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Model {

    Map<String, DefinitionInfo> definitions = new HashMap<>();

    public Collection<DefinitionInfo> getDefinitions() {
        return definitions.values();
    }

    public DefinitionInfo getDefinitionInfo(String name) {
        return definitions.get(name);
    }

    public void registerDefinition(Definition definition, String packageSuffix, String filename) throws ConflictingNameExcepton {
        DefinitionInfo definitionInfo = definitions.computeIfAbsent(definition.getName(), key -> new DefinitionInfo(key));
        if (definition instanceof PartialDefinition) {
            definitionInfo.addPartialDefinition((PartialDefinition) definition);
        }else if (definition instanceof ImplementsDefinition){
            definitionInfo.addImpementsDefinition((ImplementsDefinition) definition);
        } else {
            if (definitionInfo.getDefinition() != null && !definitionInfo.getDefinition().equals(definition)) {
                throw new ConflictingNameExcepton(definitionInfo);
            }
            definitionInfo.setDefinition(definition);
            definitionInfo.setPackgeName(packageSuffix);
            definitionInfo.setFilename(filename);
        }
    }

    public class ConflictingNameExcepton extends Exception {
        private final DefinitionInfo definitionInfo;

        private ConflictingNameExcepton(DefinitionInfo definitionInfo) {
            this.definitionInfo = definitionInfo;
        }

        public DefinitionInfo getDefinitionInfo() {
            return definitionInfo;
        }
    }


}