package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Constant;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

public class ConstantVisitor extends WebIDLBaseVisitor<Constant> {

    @Override
    public Constant visitConst_(WebIDLParser.Const_Context ctx) {
        String name = ctx.IDENTIFIER_WEBIDL().getText();
        String type = ctx.constType().accept(new TypeVisitor());
        String value = ctx.constValue().getText();
        return new Constant(name, type, value);
    }
}
