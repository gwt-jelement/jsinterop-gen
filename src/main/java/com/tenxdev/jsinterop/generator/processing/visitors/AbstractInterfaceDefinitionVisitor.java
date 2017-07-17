package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInterfaceDefinitionVisitor<T> {

    public T accept(InterfaceDefinition interfaceDefinition){
        List<T> result=new ArrayList<>();
        result.add(visitConstructors(interfaceDefinition.getConstructors()));
        result.add(visitMethods(interfaceDefinition.getMethods()));
        result.add(visitAttributes(interfaceDefinition.getAttributes()));
        result.add(visitConstants(interfaceDefinition.getConstants()));
        result.add(visitFeatures(interfaceDefinition.getFeatures()));
        return coallesce(result);
    }

    protected abstract T visitConstructors(List<Method> constructors);

    protected abstract T visitFeatures(List<Feature> features);

    protected abstract T coallesce(List<T> result);

    protected abstract T visitConstants(List<Constant> constants);

    protected abstract T visitAttributes(List<Attribute> attributes);

    protected abstract T visitMethods(List<Method> methods);

}
