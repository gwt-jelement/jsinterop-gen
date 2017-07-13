package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class InterfaceVisitor extends WebIDLBaseVisitor<InterfaceDefinition> {

    private final List<Method> constructors;

    public InterfaceVisitor(List<Method> constructors) {
        this.constructors = constructors;
    }

    @Override
    public InterfaceDefinition visitInterface_(WebIDLParser.Interface_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        String parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null
                ? null : ctx.inheritance().IDENTIFIER_WEBIDL().getText();
        List<InterfaceMember> members = ctx.interfaceMembers().accept(new InterfaceMembersVisitor());
        return new InterfaceDefinition(name, parent, constructors, members);
    }
}
