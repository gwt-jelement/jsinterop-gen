package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class PartialInterfaceVisitor extends WebIDLBaseVisitor<InterfaceDefinition>{

    @Override
    public InterfaceDefinition visitPartialInterface(WebIDLParser.PartialInterfaceContext ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<InterfaceMember> members = ctx.interfaceMembers().accept(new InterfaceMembersVisitor(name));
        InterfaceDefinition partialInterface = new InterfaceDefinition(name, null, null, members);
        partialInterface.setPartial(true);
        return partialInterface;
    }
}
