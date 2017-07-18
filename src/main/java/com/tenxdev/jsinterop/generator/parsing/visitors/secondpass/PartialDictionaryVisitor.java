package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.PartialDictionaryDefinition;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class PartialDictionaryVisitor extends ContextWebIDLBaseVisitor<DictionaryDefinition> {

    public PartialDictionaryVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public DictionaryDefinition visitPartialDictionary(WebIDLParser.PartialDictionaryContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members = ctx.dictionaryMembers().accept(new DictionaryMemberVisitor(parsingContext));
        DictionaryDefinition partialDictionary = new DictionaryDefinition(name, null, members);
        return new PartialDictionaryDefinition(partialDictionary);

    }
}
