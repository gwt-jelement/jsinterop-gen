package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.DefinitionInfo;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.ImplementsDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

class ModelPartialsMerger {
    private final ErrorReporter errorHandler;

    public ModelPartialsMerger(ErrorReporter errorHandler) {
        this.errorHandler=errorHandler;
    }

    public void mergePartials(Map<String, DefinitionInfo> definitionInfoMap) {
        processPartials(definitionInfoMap);
        processImplements(definitionInfoMap);
        checkForLeftOverPartials(definitionInfoMap);
    }

    private void processPartials(Map<String, DefinitionInfo> definitionInfoMap) {
        final List<Definition> processed = new ArrayList<>();
        definitionInfoMap.values().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
            Definition primaryDefinition = definitionInfo.getDefinition();
            processed.clear();
            for (Definition partialDefinition : definitionInfo.getPartials()) {
                if (InterfaceDefinition.is(partialDefinition) && InterfaceDefinition.is(primaryDefinition)) {
                    mergeInterfaces((InterfaceDefinition) primaryDefinition, (InterfaceDefinition) partialDefinition);
                    processed.add(partialDefinition);
                } else if (DictionaryDefinition.is(partialDefinition) && DictionaryDefinition.is(primaryDefinition)) {
                    mergeDicstionaries((DictionaryDefinition) primaryDefinition, (DictionaryDefinition) partialDefinition);
                    processed.add(partialDefinition);
                }
            }
            definitionInfo.getPartials().removeAll(processed);
        });
    }

    private void processImplements(Map<String, DefinitionInfo> definitionInfoMap) {
        final List<Definition> processed = new ArrayList<>();
        definitionInfoMap.values().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
            Definition primaryDefinition = definitionInfo.getDefinition();
            processed.clear();
            for (Definition partialDefinition : definitionInfo.getPartials()) {
                if (ImplementsDefinition.is(partialDefinition) && InterfaceDefinition.is(primaryDefinition)) {
                    mergeImplements(definitionInfoMap, (InterfaceDefinition) primaryDefinition, (ImplementsDefinition) partialDefinition);
                    processed.add(partialDefinition);
                } else if (ImplementsDefinition.is(partialDefinition) && DictionaryDefinition.is(primaryDefinition)) {
                    mergeImplements(definitionInfoMap, (DictionaryDefinition) primaryDefinition, (ImplementsDefinition) partialDefinition);
                    processed.add(partialDefinition);
                }
            }
            definitionInfo.getPartials().removeAll(processed);
        });
    }

    private void checkForLeftOverPartials(Map<String, DefinitionInfo> definitionInfoMap) {
        definitionInfoMap.values().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
            for (Definition partialDefinition : definitionInfo.getPartials()) {
                errorHandler.formatError("Could not merge %s %s into %s %s%n",
                        partialDefinition.getClass().getSimpleName(), partialDefinition.getName(),
                        definitionInfo.getDefinition().getClass().getSimpleName(),
                        definitionInfo.getDefinition().getName());
            }
        });
    }

    private void mergeDicstionaries(DictionaryDefinition primaryDefinition, DictionaryDefinition partialDefinition) {
        primaryDefinition.getMembers().addAll(partialDefinition.getMembers());
    }

    private void mergeInterfaces(InterfaceDefinition primaryDefinition, InterfaceDefinition definition) {
        primaryDefinition.getAttributes().addAll(definition.getAttributes());
        primaryDefinition.getMethods().addAll(definition.getMethods());
        primaryDefinition.getConstants().addAll(definition.getConstants());
        primaryDefinition.getConstructors().addAll(definition.getConstructors());
        primaryDefinition.getFeatures().addAll(definition.getFeatures());
    }

    private void lookupDefinition(Map<String, DefinitionInfo> definitionInfoMap,
                                  ImplementsDefinition implementsDefinition,
                                  Predicate<Definition> accept,
                                  Consumer<Definition> acceptedConsumer) {
        String implementedDefinitionName = implementsDefinition.getParent();
        DefinitionInfo definitionInfo = definitionInfoMap.get(implementedDefinitionName);
        if (definitionInfo == null) {
            System.err.format("Could not find deinition for %s implemented by %s%n",
                    implementedDefinitionName, implementsDefinition.getName());
            return;
        }
        if (accept.test(definitionInfo.getDefinition())) {
            acceptedConsumer.accept(definitionInfo.getDefinition());
        } else {
            System.err.format("Could not merge implemented %s%n", definitionInfo.getName());
        }
    }

    private void mergeImplements(Map<String, DefinitionInfo> definitionInfoMap,
                                 InterfaceDefinition primaryDefinition,
                                 ImplementsDefinition implementsDefinition) {
        lookupDefinition(definitionInfoMap, implementsDefinition,
                definition -> InterfaceDefinition.is(definition),
                definition -> mergeInterfaces(primaryDefinition, (InterfaceDefinition) definition));
    }

    private void mergeImplements(Map<String, DefinitionInfo> definitionInfoMap,
                                 DictionaryDefinition primaryDefinition,
                                 ImplementsDefinition implementsDefinition) {
        lookupDefinition(definitionInfoMap, implementsDefinition,
                definition -> DictionaryDefinition.is(definition),
                definition -> mergeDicstionaries(primaryDefinition, (DictionaryDefinition) definition));
    }

}
