package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Model;
import com.tenxdev.jsinterop.generator.model.TypeDefinition;

import java.util.Map;
import java.util.stream.Collectors;

public class TypeDefsProcessor {
    private final Model model;

    public TypeDefsProcessor(Model model) {
        this.model=model;
    }

    public void processModel(TypeMapper typeMapper) {
        Map<String, String[]> typesMap = model.getDefinitions().stream()
                .filter(definitionInfo -> definitionInfo.getDefinition() instanceof TypeDefinition)
                .map(definitionInfo -> (TypeDefinition) definitionInfo.getDefinition())
                .collect(Collectors.toMap(TypeDefinition::getName, TypeDefinition::getTypes));
        typeMapper.addTypeDefinitions(typesMap);
    }
}
