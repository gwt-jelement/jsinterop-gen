package com.tenxdev.jsinterop.generator.visitors;

import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import com.tenxdev.jsinterop.generator.model.Definition;
import com.tenxdev.jsinterop.generator.model.Method;
import org.antlr4.webidl.WebIDLBaseVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefinitionsVisitor extends WebIDLBaseVisitor<List<Definition>> {
    private final String filePath;

    public DefinitionsVisitor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Definition> visitDefinitions(WebIDLParser.DefinitionsContext ctx) {
        List<Definition> definitionList = new ArrayList<>();

        WebIDLParser.DefinitionsContext definitions = ctx;
        while (definitions != null && definitions.definition() != null) {
            List<Method> constructors = definitions.extendedAttributeList() != null ?
                    ctx.extendedAttributeList().accept(new ExtendedAttributeListVisitor())
                    : Collections.emptyList();
            Definition definition = definitions.definition().accept(new DefinitionVisitor(filePath, constructors));
            if (definition != null) {
                definitionList.add(definition);
            } else {
                System.err.println("*** Unexpected missed definition: " + ParserUtil.getText(ctx));
            }
            definitions = definitions.definitions();
        }
        return definitionList;
    }
}
