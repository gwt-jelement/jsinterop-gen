package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractDictionaryDefinitionVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryDefinitionVisitor extends AbstractDictionaryDefinitionVisitor<List<String>>{
    @Override
    protected List<String> visitMembers(List<DictionaryMember> members) {
        TypeVisitor typeVisitor=new TypeVisitor();
        return members.stream()
                .map(member->typeVisitor.accept(member.getType()))
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
