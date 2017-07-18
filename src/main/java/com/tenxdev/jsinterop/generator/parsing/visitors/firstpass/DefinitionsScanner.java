package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class DefinitionsScanner extends ContextWebIDLBaseVisitor<Void> {

    public DefinitionsScanner(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Void visitDefinitions(WebIDLParser.DefinitionsContext ctx) {
        WebIDLParser.DefinitionsContext definitions = ctx;
        while (definitions != null && definitions.definition() != null) {
            definitions.definition().accept(new DefinitionScanner(parsingContext));
            definitions = definitions.definitions();
        }
        return null;
    }
}
