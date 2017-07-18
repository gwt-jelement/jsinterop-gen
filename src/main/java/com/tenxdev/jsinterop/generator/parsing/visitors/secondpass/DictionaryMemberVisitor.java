package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

public class DictionaryMemberVisitor extends ContextWebIDLBaseVisitor<List<DictionaryMember>> {

    DictionaryMemberVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<DictionaryMember> visitDictionaryMembers(WebIDLParser.DictionaryMembersContext ctx) {
        List<DictionaryMember> members = new ArrayList<>();
        WebIDLParser.DictionaryMembersContext context = ctx;
        while (context != null && context.dictionaryMember() != null) {
            WebIDLParser.DictionaryMemberContext memberContext = context.dictionaryMember();
            boolean required = memberContext.required() != null && "required".equals(memberContext.required().getText());
            Type type = memberContext.type().accept(new TypeVisitor(parsingContext));
            String name = memberContext.IDENTIFIER_WEBIDL().getText();
            String defaultValue = memberContext.default_() == null || memberContext.default_().defaultValue() == null ? null :
                    memberContext.default_().defaultValue().getText();
            members.add(new DictionaryMember(name, type, required, defaultValue));
            context = context.dictionaryMembers();
        }
        return members;
    }
}
