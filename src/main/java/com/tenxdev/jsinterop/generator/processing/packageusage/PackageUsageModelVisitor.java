package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractModelVisitor;

import java.util.*;

public class PackageUsageModelVisitor extends AbstractModelVisitor<List<String>> {

    private static final List<String> NO_IMPLEMENTATION = Collections.emptyList();
    private TypeVisitor typeVisitor=new TypeVisitor();

    @Override
    protected List<String> visitTypeDefinition(TypeDefinition definition) {
        return NO_IMPLEMENTATION;
    }

    @Override
    protected List<String> visitEnumDefinition(EnumDefinition definition) {
        return NO_IMPLEMENTATION;
    }

    @Override
    protected List<String> visitCallbackDefinition(CallbackDefinition definition) {
        return new MethodVisitor().accept(definition.getMethod());
    }

    @Override
    protected List<String> visitDictionaryDefinition(DictionaryDefinition definition) {
        return new DictionaryDefinitionVisitor().accept(definition);
    }

    @Override
    public List<String> visitInterfaceDefinition(InterfaceDefinition interfaceDefinition){
        return new InterfaceDefinitionUsageVisitor().accept(interfaceDefinition);
    }

    @Override
    protected List<String> visitImplementsDefinition(ImplementsDefinition definition) {
        return NO_IMPLEMENTATION;
    }




}
