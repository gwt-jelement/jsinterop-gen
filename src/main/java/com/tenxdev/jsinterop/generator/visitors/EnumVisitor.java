package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.EnumDefinition;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class EnumVisitor extends WebIDLBaseVisitor<EnumDefinition> {

    @Override
    public EnumDefinition visitEnum_(WebIDLParser.Enum_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        List<String> values = ctx.enumValueList().accept(new EnumValuesVisitor());
        return new EnumDefinition(name, values);
    }
}
