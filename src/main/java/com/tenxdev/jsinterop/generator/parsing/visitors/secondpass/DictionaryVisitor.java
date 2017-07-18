package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

class DictionaryVisitor extends ContextWebIDLBaseVisitor<DictionaryDefinition> {

    public DictionaryVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public DictionaryDefinition visitDictionary(WebIDLParser.DictionaryContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        String parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null ? null :
                ctx.inheritance().IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members = ctx.dictionaryMembers().accept(new DictionaryMemberVisitor(parsingContext));
        return new DictionaryDefinition(name, parent, members);
    }
}
