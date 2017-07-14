package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

public class DictionaryMemberVisitor extends WebIDLBaseVisitor<List<DictionaryMember>> {

    @Override
    public List<DictionaryMember> visitDictionaryMembers(WebIDLParser.DictionaryMembersContext ctx) {
        List<DictionaryMember> members = new ArrayList<>();
        WebIDLParser.DictionaryMembersContext context = ctx;
        while (context != null && context.dictionaryMember() != null) {
            WebIDLParser.DictionaryMemberContext memberContext = context.dictionaryMember();
            boolean required = memberContext.required() != null && "required".equals(memberContext.required().getText());
            String[] types=memberContext.type().accept(new TypeVisitor());
            String name=memberContext.IDENTIFIER_WEBIDL().getText();
            String defaultValue=memberContext.default_()==null || memberContext.default_().defaultValue()==null?null:
                    memberContext.default_().defaultValue().getText();
            members.add(new DictionaryMember(name, types, required, defaultValue));
            context=context.dictionaryMembers();
        }
        return members;
    }
}
