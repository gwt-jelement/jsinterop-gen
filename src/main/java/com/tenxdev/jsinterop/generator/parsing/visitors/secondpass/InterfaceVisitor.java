package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition;
import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class InterfaceVisitor extends ContextWebIDLBaseVisitor<InterfaceDefinition> {

    private final List<Method> constructors;

    public InterfaceVisitor(ParsingContext context, List<Method> constructors) {
        super(context);
        this.constructors = constructors;
    }

    @Override
    public InterfaceDefinition visitInterface_(WebIDLParser.Interface_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        Type parent = ctx.inheritance() == null || ctx.inheritance().IDENTIFIER_WEBIDL() == null
                ? null : parsingContetxt.getTypeFactory().getTypeNoParse(ctx.inheritance().IDENTIFIER_WEBIDL().getText());
        List<InterfaceMember> members = ctx.interfaceMembers().accept(new InterfaceMembersVisitor(parsingContetxt, name));
        return new InterfaceDefinition(name, parent, constructors, members);
    }
}
