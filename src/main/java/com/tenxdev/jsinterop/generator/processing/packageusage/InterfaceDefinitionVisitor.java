package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractInterfaceDefinitionVisitor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDefinitionVisitor extends AbstractInterfaceDefinitionVisitor<List<String>> {

    private TypeVisitor typeVisitor=new TypeVisitor();

    @Override
    protected List<String> visitConstructors(List<Method> constructors) {
        return visitMethods(constructors);
    }

    @Override
    protected List<String> visitFeatures(List<Feature> features) {
        //TODO revisit
        return Collections.emptyList();
    }

    @Override
    protected List<String> coallesce(List<List<String>> result) {
        return result.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    protected List<String> visitConstants(List<Constant> constants) {
        return constants.stream()
                .map(this::visitConstant)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

    }

    private List<String> visitConstant(Constant constant) {
        return typeVisitor.accept(constant.getType());
    }

    @Override
    protected List<String> visitAttributes(List<Attribute> attributes) {
        return new AttributesVisitor().accept(attributes);
    }

    @Override
    protected List<String> visitMethods(List<Method> methods) {
        MethodVisitor methodVisitor=new MethodVisitor();
        return methods.stream()
                .map(methodVisitor::accept)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
