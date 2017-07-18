package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class EnumScanner extends ContextWebIDLBaseVisitor<Void> {

    public EnumScanner(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Void visitEnum_(WebIDLParser.Enum_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        parsingContext.getTypeFactory().registerType(name, new ObjectType(name, parsingContext.getPackageSuffix()));
        return null;
    }
}
