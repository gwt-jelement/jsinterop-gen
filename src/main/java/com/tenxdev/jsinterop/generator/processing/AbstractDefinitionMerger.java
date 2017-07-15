package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;

public abstract class AbstractDefinitionMerger {

    protected final ErrorReporter errorReporter;

    public AbstractDefinitionMerger(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    protected void mergeDicstionaries(DictionaryDefinition primaryDefinition, DictionaryDefinition partialDefinition) {
        primaryDefinition.getMembers().addAll(partialDefinition.getMembers());
    }

    protected void mergeInterfaces(InterfaceDefinition primaryDefinition, InterfaceDefinition definition) {
        primaryDefinition.getAttributes().addAll(definition.getAttributes());
        primaryDefinition.getMethods().addAll(definition.getMethods());
        primaryDefinition.getConstants().addAll(definition.getConstants());
        primaryDefinition.getConstructors().addAll(definition.getConstructors());
        primaryDefinition.getFeatures().addAll(definition.getFeatures());
    }

    protected void reportTypeMismatch(Definition primaryDefinition, Definition secondaryDefinition) {
        errorReporter.formatError("Do not know how to merge implements of %s %s and %s %s%n",
                primaryDefinition.getClass().getSimpleName(), primaryDefinition.getName(),
                secondaryDefinition.getClass().getSimpleName(), secondaryDefinition.getName());
    }
}