package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.Collections;
import java.util.List;

public class OperationRestVisitor extends WebIDLBaseVisitor<Method>{

    private final String[] returnTypes;
    private final boolean static_;

    public OperationRestVisitor(String[] returnTypes, boolean static_){
        this.returnTypes =returnTypes;
        this.static_=static_;
    }

    @Override
    public Method visitOperationRest(WebIDLParser.OperationRestContext ctx) {
        String name=ctx.optionalIdentifier().getText();
        List<MethodArgument> parameters = ctx.argumentList()==null ||ctx.argumentList().argument()==null ? Collections.emptyList() :
                ctx.argumentList().accept(new ArgumentsVisitor());
        return new Method(name, returnTypes, parameters, static_);
    }
}
