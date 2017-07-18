package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * We need to order the application of implements, so if A extends B,
 * and B extends C, C is merged into B before B is merged into A
 */
public class ImplementsMerger extends AbstractDefinitionMerger {

    private final Model model;
    private final Map<String, List<String>> implementsMap = new HashMap<>();

    public ImplementsMerger(Model model, ErrorReporter errorReporter) {
        super(errorReporter);
        this.model = model;
    }

    public void processModel() {
        model.getDefinitions().forEach(definitionInfo ->
                definitionInfo.getImplementsDefinitions().forEach(implementsDefinition -> {
                    String definitionName = implementsDefinition.getName();
                    String implementsName = implementsDefinition.getParent();
                    implementsMap.computeIfAbsent(definitionName, key -> new ArrayList<>()).add(implementsName);
                }));
        implementsMap.keySet().forEach(this::processImplements);
    }

    private void processImplements(String definitionName) {
        List<String> implementsNames = implementsMap.get(definitionName);
        while (!implementsNames.isEmpty()) {
            String implementsName = implementsNames.remove(0);
            if (implementsInterfaces(implementsName)) {
                processImplements(implementsName);
            }
            processModel(definitionName, implementsName);
        }
    }

    private void getDefinitionByName(String name, Consumer<Definition> consumer) {
        DefinitionInfo definitionInfo = model.getDefinitionInfo(name);
        if (definitionInfo == null) {
            errorReporter.formatError("Unknown definition %s implements %n", name);
        } else {
            consumer.accept(definitionInfo.getDefinition());
        }
    }

    private void processModel(String definitionName, String implementsName) {
        getDefinitionByName(definitionName, primaryDefinition ->
                getDefinitionByName(implementsName, implementsDefinition -> {
                    if (primaryDefinition instanceof InterfaceDefinition && implementsDefinition instanceof InterfaceDefinition) {
                        mergeInterfaces((InterfaceDefinition) primaryDefinition, (InterfaceDefinition) implementsDefinition);
                    } else if (primaryDefinition instanceof DictionaryDefinition && implementsDefinition instanceof DictionaryDefinition) {
                        mergeDictionaries((DictionaryDefinition) primaryDefinition, (DictionaryDefinition) implementsDefinition);
                    } else {
                        reportTypeMismatch(primaryDefinition, implementsDefinition);
                    }
                }));
    }

    private boolean implementsInterfaces(String implementsName) {
        return implementsMap.containsKey(implementsName);
    }

}
