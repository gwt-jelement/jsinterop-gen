package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.PartialInterfaceDefinition;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

class PartialInterfaceVisitor extends ContextWebIDLBaseVisitor<InterfaceDefinition> {

    public PartialInterfaceVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceDefinition visitPartialInterface(WebIDLParser.PartialInterfaceContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<InterfaceMember> members = ctx.interfaceMembers().accept(new InterfaceMembersVisitor(parsingContext, name));
        InterfaceDefinition partialInterface = new InterfaceDefinition(name, null, null, members);
        return new PartialInterfaceDefinition(partialInterface);
    }
}
