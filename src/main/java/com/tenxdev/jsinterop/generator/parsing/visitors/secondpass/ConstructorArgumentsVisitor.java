package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.processing.TypeUtil;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

class ConstructorArgumentsVisitor extends ContextWebIDLBaseVisitor<List<MethodArgument>> {

    public ConstructorArgumentsVisitor(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public List<MethodArgument> visitExtendedAttributeInner(WebIDLParser.ExtendedAttributeInnerContext ctx) {
        WebIDLParser.ExtendedAttributeInnerContext extendedAttributeInnerContext = ctx;
        List<MethodArgument> arguments = new ArrayList<>();
        List<String> tokens = new ArrayList<>();
        while (extendedAttributeInnerContext != null) {
            if (extendedAttributeInnerContext.otherOrComma() != null) {
                String token = extendedAttributeInnerContext.otherOrComma().getText();
                if (",".equals(token)) {
                    createArgument(tokens, arguments);
                    tokens.clear();
                } else {
                    tokens.add(token);
                }
                extendedAttributeInnerContext = extendedAttributeInnerContext.extendedAttributeInner(0);
            } else if (extendedAttributeInnerContext.extendedAttributeInner(0) != null) {
                StringBuilder type = new StringBuilder("");
                WebIDLParser.ExtendedAttributeInnerContext subContext = extendedAttributeInnerContext.extendedAttributeInner(0);
                while (subContext != null && subContext.otherOrComma() != null) {
                    String token = subContext.otherOrComma().getText();
                    type.append("or".equals(token) ? "|" : token);
                    subContext = subContext.extendedAttributeInner(0);
                }
                tokens.add(type.toString());
                extendedAttributeInnerContext = extendedAttributeInnerContext.extendedAttributeInner(1);
            } else {
                break;
            }
        }
        createArgument(tokens, arguments);
        return arguments;
    }

    private void createArgument(List<String> tokens, List<MethodArgument> arguments) {
        //This is ugly, but not as ugly as the parse tree generated by the grammar for extended attributes
        if (!tokens.isEmpty()) {
            boolean optional = false;
            if (tokens.remove("optional")) {
                optional = true;
            }
            tokens.remove("?");
            String defaultValue = null;
            int equalsIndex = tokens.indexOf("=");
            if (equalsIndex != -1) {
                defaultValue = tokens.get(equalsIndex + 1);
                tokens.remove(equalsIndex + 1);
                tokens.remove(equalsIndex);
            }

            String name = tokens.get(tokens.size() - 1);
            StringBuilder typeBuilder = new StringBuilder();
            for (int i = 0; i < tokens.size() - 1; ++i) {
                typeBuilder.append(tokens.get(i));
            }
            String type = TypeUtil.INSTANCE.removeOptionalIndicator(typeBuilder.toString());
            if (type.indexOf('|') != -1) {
                String[] types = type.split("\\|");
                arguments.add(new MethodArgument(name, parsingContext.getTypeFactory().getUnionType(types), false, optional, defaultValue));
            } else {
                arguments.add(new MethodArgument(name, parsingContext.getTypeFactory().getType(type),
                        false, optional, defaultValue));
            }
        }
    }
}

