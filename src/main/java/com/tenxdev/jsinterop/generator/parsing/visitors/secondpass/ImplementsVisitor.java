package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.ImplementsDefinition;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ImplementsVisitor extends ContextWebIDLBaseVisitor<ImplementsDefinition>{

    public ImplementsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public ImplementsDefinition visitImplementsStatement(WebIDLParser.ImplementsStatementContext ctx) {
        String name=ctx.IDENTIFIER_WEBIDL(0).getText();
        String type=ctx.IDENTIFIER_WEBIDL(1).getText();
        return new ImplementsDefinition(name, type);
    }
}
