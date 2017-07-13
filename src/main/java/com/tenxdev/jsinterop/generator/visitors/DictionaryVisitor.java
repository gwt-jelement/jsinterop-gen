package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class DictionaryVisitor extends WebIDLBaseVisitor<DictionaryDefinition>{

    @Override
    public DictionaryDefinition visitDictionary(WebIDLParser.DictionaryContext ctx) {
        String name=ctx.IDENTIFIER_WEBIDL().getText();
        String parent=ctx.inheritance()==null||ctx.inheritance().IDENTIFIER_WEBIDL()==null?null:
                ctx.inheritance().IDENTIFIER_WEBIDL().getText();
        List<DictionaryMember> members=ctx.dictionaryMembers().accept(new DictionaryMemberVisitor());
        return new DictionaryDefinition(name, parent, members);
    }
}
