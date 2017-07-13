package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.ImplementsDefinition;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ImplementsVisitor extends WebIDLBaseVisitor<ImplementsDefinition>{

    @Override
    public ImplementsDefinition visitImplementsStatement(WebIDLParser.ImplementsStatementContext ctx) {
        String name=ctx.IDENTIFIER_WEBIDL(0).getText();
        String type=ctx.IDENTIFIER_WEBIDL(1).getText();
        return new ImplementsDefinition(name, type);
    }
}
