package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.PartialDictionaryDefinition;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class PartialDictionaryVisitor extends WebIDLBaseVisitor<DictionaryDefinition> {

    @Override
    public DictionaryDefinition visitPartialDictionary(WebIDLParser.PartialDictionaryContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members = ctx.dictionaryMembers().accept(new DictionaryMemberVisitor());
        DictionaryDefinition partialDictionary = new DictionaryDefinition(name, null, members);
        return new PartialDictionaryDefinition(partialDictionary);

    }
}
