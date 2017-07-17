package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.EnumDefinition;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class EnumVisitor extends ContextWebIDLBaseVisitor<EnumDefinition> {

    public EnumVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public EnumDefinition visitEnum_(WebIDLParser.Enum_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<String> values = ctx.enumValueList().accept(new EnumValuesVisitor(parsingContetxt));
        return new EnumDefinition(name, values);
    }
}
