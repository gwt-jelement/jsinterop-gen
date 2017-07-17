package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

public class ArgumentsVisitor extends ContextWebIDLBaseVisitor<List<MethodArgument>> {

    public ArgumentsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<MethodArgument> visitArguments(WebIDLParser.ArgumentsContext ctx) {
        List<MethodArgument> argumentList = new ArrayList<>();
        for (WebIDLParser.ArgumentsContext arguments = ctx; arguments != null && arguments.argument() != null; arguments = arguments.arguments()) {
            boolean added = visitIfNotNull(arguments.argument().optionalOrRequiredArgument().optionalArgument(), argumentList) ||
                    visitIfNotNull(arguments.argument().optionalOrRequiredArgument().requiredArgument(), argumentList) ||
                    visitIfNotNull(arguments.argument().optionalOrRequiredArgument().requiredVarArgArgument(), argumentList);
            if (!added){
                System.err.println("Invalid state in Arguments visitor");
            }
        }
        return argumentList;
    }

    private boolean visitIfNotNull(ParserRuleContext context, List<MethodArgument> argumentList) {
        if (context != null) {
            argumentList.add(context.accept(new ArgumentVisitor(parsingContetxt)));
            return true;
        }
        return false;
    }

    @Override
    public List<MethodArgument> visitArgumentList(WebIDLParser.ArgumentListContext ctx) {
        List<MethodArgument> argumentList = new ArrayList<>();
        boolean added = visitIfNotNull(ctx.argument().optionalOrRequiredArgument().optionalArgument(), argumentList) ||
                visitIfNotNull(ctx.argument().optionalOrRequiredArgument().requiredArgument(), argumentList) ||
                visitIfNotNull(ctx.argument().optionalOrRequiredArgument().requiredVarArgArgument(), argumentList);
        if (!added){
            System.err.println("Invalid state in Arguments visitor");
        }
        if (ctx.arguments() != null && ctx.arguments().argument() != null) {
            argumentList.addAll(ctx.arguments().accept(this));
        }
        return argumentList;
    }
}
