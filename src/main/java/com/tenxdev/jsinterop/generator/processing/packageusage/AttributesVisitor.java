package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.Attribute;

import java.util.List;
import java.util.stream.Collectors;

public class AttributesVisitor {

    private TypeVisitor typeVisitor=new TypeVisitor();

    List<String> accept(List<Attribute> attributes){
        return attributes.stream()
                .map(this::visitAttribute)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> visitAttribute(Attribute attribute) {
        return typeVisitor.accept(attribute.getType());
    }
}
