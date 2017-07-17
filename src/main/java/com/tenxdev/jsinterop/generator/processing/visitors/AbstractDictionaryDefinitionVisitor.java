package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;

import java.util.List;

public abstract class AbstractDictionaryDefinitionVisitor<T> {

    public T accept(DictionaryDefinition definition){
        return visitMembers(definition.getMembers());
    }

    protected abstract T visitMembers(List<DictionaryMember> members);
}
