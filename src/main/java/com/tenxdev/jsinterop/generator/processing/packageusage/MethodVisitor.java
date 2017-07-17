package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodVisitor {

    private TypeVisitor typeVisitor=new TypeVisitor();

    public List<String> accept(Method method) {
        ArrayList<String> packages = method.getArguments().stream()
                .map(this::visitMethodArgument)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        packages.addAll(typeVisitor.accept(method.getReturnType()));
        return packages.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> visitMethodArgument(MethodArgument methodArgument) {
        return typeVisitor.accept(methodArgument.getType());
    }

}
