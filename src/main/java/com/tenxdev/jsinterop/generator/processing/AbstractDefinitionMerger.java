package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDefinitionMerger {

    protected final ErrorReporter errorReporter;

    public AbstractDefinitionMerger(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    protected void mergeDicstionaries(DictionaryDefinition primaryDefinition, DictionaryDefinition partialDefinition) {
        primaryDefinition.getMembers().addAll(partialDefinition.getMembers());
    }

    protected void mergeInterfaces(InterfaceDefinition primaryDefinition, InterfaceDefinition definition) {
        primaryDefinition.getAttributes().addAll(disjoinList(primaryDefinition.getAttributes(), definition.getAttributes()));
        primaryDefinition.getMethods().addAll(disjoinList(primaryDefinition.getMethods(), definition.getMethods()));
        primaryDefinition.getConstants().addAll(disjoinList(primaryDefinition.getConstants(), definition.getConstants()));
        primaryDefinition.getConstructors().addAll(disjoinList(primaryDefinition.getConstructors(), definition.getConstructors()));
        primaryDefinition.getFeatures().addAll(disjoinList(primaryDefinition.getFeatures(), definition.getFeatures()));
    }

    protected void reportTypeMismatch(Definition primaryDefinition, Definition secondaryDefinition) {
        errorReporter.formatError("Do not know how to merge implements of %s %s and %s %s%n",
                primaryDefinition.getClass().getSimpleName(), primaryDefinition.getName(),
                secondaryDefinition.getClass().getSimpleName(), secondaryDefinition.getName());
    }

    /**
     * gets all the elements in secondaryList that are not in primaryList
     * @param primaryList the first list
     * @param secondaryList the second list
     * @param <T> the type of the lists
     * @return a list with all the elements in secondaryList that are not in primaryList
     */
    private <T> List<T> disjoinList(List<T> primaryList, List<T> secondaryList) {
        List<T> result = new ArrayList<>(secondaryList);
        result.removeAll(primaryList);
        return result;
    }
}