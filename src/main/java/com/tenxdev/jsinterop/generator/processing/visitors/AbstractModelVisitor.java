package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModelVisitor<T> {

    public Map<DefinitionInfo, T> accept(Model model) {
        Map<DefinitionInfo, T> result = new HashMap<>();
        for (DefinitionInfo definitionInfo : model.getDefinitions()) {
            result.put(definitionInfo, visitDefinition(definitionInfo.getDefinition()));
        }
        return result;
    }

    private T visitDefinition(Definition definition) {
        if (definition instanceof InterfaceDefinition) {
            return visitInterfaceDefinition((InterfaceDefinition) definition);
        } else if (definition instanceof DictionaryDefinition) {
            return visitDictionaryDefinition((DictionaryDefinition) definition);
        } else if (definition instanceof EnumDefinition) {
            return visitEnumDefinition((EnumDefinition) definition);
        } else if (definition instanceof TypeDefinition) {
            return visitTypeDefinition((TypeDefinition) definition);
        } else if (definition instanceof ImplementsDefinition) {
            return visitImplementsDefinition((ImplementsDefinition) definition);
        } else if (definition instanceof CallbackDefinition) {
            return visitCallbackDefinition((CallbackDefinition) definition);
        } else {
            throw new IllegalStateException("Unexpected definition type " + definition.getClass().getSimpleName());
        }
    }

    protected abstract T visitTypeDefinition(TypeDefinition definition);

    protected abstract T visitEnumDefinition(EnumDefinition definition);

    protected abstract T visitCallbackDefinition(CallbackDefinition definition);

    protected abstract T visitDictionaryDefinition(DictionaryDefinition definition);

    protected abstract T visitInterfaceDefinition(InterfaceDefinition definition);

    protected abstract T visitImplementsDefinition(ImplementsDefinition definition);
}
