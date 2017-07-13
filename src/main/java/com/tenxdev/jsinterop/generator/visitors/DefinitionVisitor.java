package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class DefinitionVisitor extends WebIDLBaseVisitor<Definition> {
    private final String filePath;
    private final List<Method> constructors;

    public DefinitionVisitor(String filePath, List<Method> constructors) {
        this.filePath = filePath;
        this.constructors = constructors;
    }

    @Override
    public Definition visitDefinition(WebIDLParser.DefinitionContext ctx) {
        if (ctx.callbackOrInterface() != null) {
            if (ctx.callbackOrInterface().interface_() != null) {
                return ctx.callbackOrInterface().interface_().accept(new InterfaceVisitor(constructors));
            }else if (ctx.callbackOrInterface().callbackRestOrInterface()!=null){
                return ctx.callbackOrInterface().callbackRestOrInterface().accept(new CallbackVisitor(constructors));
            }
        } else if (ctx.dictionary() != null) {
            return ctx.dictionary().accept(new DictionaryVisitor());
        } else if (ctx.enum_()!=null){
            return ctx.enum_().accept(new EnumVisitor());
        } else if (ctx.partial()!=null && ctx.partial().partialDefinition()!=null){
            if (ctx.partial().partialDefinition().partialInterface()!=null){
                return ctx.partial().partialDefinition().partialInterface().accept(new PartialInterfaceVisitor());
            }else if ((ctx).partial().partialDefinition().partialDictionary()!=null){
                return ctx.partial().partialDefinition().partialDictionary().accept(new PartialDictionaryVisitor());
            }
        }else if (ctx.typedef()!=null) {
            return ctx.typedef().accept(new TypeDefVisitor());
        }else if (ctx.implementsStatement()!=null){
            return ctx.implementsStatement().accept(new ImplementsVisitor());
        }
        return null;
    }
}
