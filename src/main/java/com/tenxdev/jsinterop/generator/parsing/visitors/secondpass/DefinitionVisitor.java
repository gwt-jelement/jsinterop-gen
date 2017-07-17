package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class DefinitionVisitor extends ContextWebIDLBaseVisitor<Definition> {
    private final List<Method> constructors;

    public DefinitionVisitor(ParsingContext context,List<Method> constructors) {
        super(context);
        this.constructors = constructors;
    }

    @Override
    public Definition visitDefinition(WebIDLParser.DefinitionContext ctx) {
        if (ctx.callbackOrInterface() != null) {
            if (ctx.callbackOrInterface().interface_() != null) {
                return ctx.callbackOrInterface().interface_().accept(new InterfaceVisitor(parsingContetxt, constructors));
            }else if (ctx.callbackOrInterface().callbackRestOrInterface()!=null){
                return ctx.callbackOrInterface().callbackRestOrInterface().accept(new CallbackVisitor(parsingContetxt, constructors));
            }
        } else if (ctx.dictionary() != null) {
            return ctx.dictionary().accept(new DictionaryVisitor(parsingContetxt));
        } else if (ctx.enum_()!=null){
            return ctx.enum_().accept(new EnumVisitor(parsingContetxt));
        } else if (ctx.partial()!=null && ctx.partial().partialDefinition()!=null){
            if (ctx.partial().partialDefinition().partialInterface()!=null){
                return ctx.partial().partialDefinition().partialInterface()
                        .accept(new PartialInterfaceVisitor(parsingContetxt));
            }else if ((ctx).partial().partialDefinition().partialDictionary()!=null){
                return ctx.partial().partialDefinition().partialDictionary()
                        .accept(new PartialDictionaryVisitor(parsingContetxt));
            }
        }else if (ctx.typedef()!=null) {
            return ctx.typedef().accept(new TypeDefVisitor(parsingContetxt));
        }else if (ctx.implementsStatement()!=null){
            return ctx.implementsStatement().accept(new ImplementsVisitor(parsingContetxt));
        }
        parsingContetxt.getErrorReporter().reportError("Unexpected state in DefinitionVisitor");
        return null;
    }
}
