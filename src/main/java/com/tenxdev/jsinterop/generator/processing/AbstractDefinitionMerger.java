package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractDefinitionMerger {

    final ErrorReporter errorReporter;

    AbstractDefinitionMerger(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    void mergeDictionaries(DictionaryDefinition primaryDefinition, DictionaryDefinition partialDefinition) {
        primaryDefinition.getMembers().addAll(partialDefinition.getMembers());
    }

    void mergeInterfaces(InterfaceDefinition primaryDefinition, InterfaceDefinition definition) {
        primaryDefinition.getAttributes().addAll(disjointList(primaryDefinition.getAttributes(), definition.getAttributes()));
        primaryDefinition.getMethods().addAll(disjointList(primaryDefinition.getMethods(), definition.getMethods()));
        primaryDefinition.getConstants().addAll(disjointList(primaryDefinition.getConstants(), definition.getConstants()));
        primaryDefinition.getConstructors().addAll(disjointList(primaryDefinition.getConstructors(), definition.getConstructors()));
        primaryDefinition.getFeatures().addAll(disjointList(primaryDefinition.getFeatures(), definition.getFeatures()));
    }

    void reportTypeMismatch(Definition primaryDefinition, Definition secondaryDefinition) {
        errorReporter.formatError("Do not know how to merge implements of %s %s and %s %s%n",
                primaryDefinition.getClass().getSimpleName(), primaryDefinition.getName(),
                secondaryDefinition.getClass().getSimpleName(), secondaryDefinition.getName());
    }

    /**
     * gets all the elements in secondaryList that are not in primaryList
     *
     * @param primaryList   the first list
     * @param secondaryList the second list
     * @param <T>           the type of the lists
     * @return a list with all the elements in secondaryList that are not in primaryList
     */
    private <T> List<T> disjointList(List<T> primaryList, List<T> secondaryList) {
        List<T> result = new ArrayList<>(secondaryList);
        result.removeAll(primaryList);
        return result;
    }
}