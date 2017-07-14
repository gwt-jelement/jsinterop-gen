package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.DefinitionInfo;
import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

class ModelPartialsMerger {
    private final ErrorReporter errorHandler;
    private final Model model;

    public ModelPartialsMerger(Model model, ErrorReporter errorHandler) {
        this.model = model;
        this.errorHandler = errorHandler;
    }

    public void mergePartials() {
        processPartials();
        processImplements();
        checkForLeftOverPartials();
    }

    private void processPartials() {
        final List<Definition> processed = new ArrayList<>();
        model.getDefinitions().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
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

    private void processImplements() {
        final List<Definition> processed = new ArrayList<>();
        model.getDefinitions().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
            Definition primaryDefinition = definitionInfo.getDefinition();
            processed.clear();
            for (Definition partialDefinition : definitionInfo.getPartials()) {
                if (ImplementsDefinition.is(partialDefinition) && InterfaceDefinition.is(primaryDefinition)) {
                    mergeImplements((InterfaceDefinition) primaryDefinition, (ImplementsDefinition) partialDefinition);
                    processed.add(partialDefinition);
                } else if (ImplementsDefinition.is(partialDefinition) && DictionaryDefinition.is(primaryDefinition)) {
                    mergeImplements((DictionaryDefinition) primaryDefinition, (ImplementsDefinition) partialDefinition);
                    processed.add(partialDefinition);
                }
            }
            definitionInfo.getPartials().removeAll(processed);
        });
    }

    private void checkForLeftOverPartials() {
        model.getDefinitions().stream().filter(info -> !info.getPartials().isEmpty()).forEach(definitionInfo -> {
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

    private void lookupDefinition(ImplementsDefinition implementsDefinition,
                                  Predicate<Definition> accept,
                                  Consumer<Definition> acceptedConsumer) {
        String implementedDefinitionName = implementsDefinition.getParent();
        DefinitionInfo definitionInfo = model.getDefinitionInfo(implementedDefinitionName);
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

    private void mergeImplements(InterfaceDefinition primaryDefinition,
                                 ImplementsDefinition implementsDefinition) {
        lookupDefinition(implementsDefinition,
                definition -> InterfaceDefinition.is(definition),
                definition -> mergeInterfaces(primaryDefinition, (InterfaceDefinition) definition));
    }

    private void mergeImplements(DictionaryDefinition primaryDefinition,
                                 ImplementsDefinition implementsDefinition) {
        lookupDefinition(implementsDefinition,
                definition -> DictionaryDefinition.is(definition),
                definition -> mergeDicstionaries(primaryDefinition, (DictionaryDefinition) definition));
    }

}
