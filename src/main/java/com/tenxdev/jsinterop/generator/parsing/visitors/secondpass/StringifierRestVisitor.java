package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;

public class StringifierRestVisitor extends ContextWebIDLBaseVisitor<InterfaceMember> {

    public StringifierRestVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public InterfaceMember visitStringifierRest(WebIDLParser.StringifierRestContext ctx) {
        if (ctx.attributeRest() != null) {
            boolean readOnly = !ctx.readOnly().isEmpty();
            return ctx.attributeRest().accept(new AttributeRestVisitor(parsingContetxt, readOnly, false));
        } else if (ctx.operationRest() != null) {
            Type returnType = ctx.returnType().accept(new TypeVisitor(parsingContetxt));
            return ctx.operationRest().accept(new OperationRestVisitor(parsingContetxt, returnType, false));
        } else {
            return new Method("toString", parsingContetxt.getTypeFactory().getType("DOMString"),
                    Collections.emptyList(), false);
        }
    }
}
