package com.tenxdev.jsinterop.generator.parsing.visitors.types;

import com.tenxdev.jsinterop.generator.errors.ErrorReporter;
import com.tenxdev.jsinterop.generator.model.types.*;
import com.tenxdev.jsinterop.generator.parsing.TypeFactory;
import com.tenxdev.jsinterop.generator.processing.ParserUtil;
import org.antlr.v4.runtime.*;
import org.antlr4.webidl.TypesBaseVisitor;
import org.antlr4.webidl.TypesLexer;
import org.antlr4.webidl.TypesParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TypeParser extends TypesBaseVisitor<Type> {


    private final TypeFactory typeFactory;
    private final ErrorReporter errorReporter;

    public TypeParser(TypeFactory typeFactory, ErrorReporter errorReporter) {
        this.typeFactory = typeFactory;
        this.errorReporter = errorReporter;
    }

    public Type parseType(String typeName) {
        TypesLexer lexer = new TypesLexer(new ANTLRInputStream(typeName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        org.antlr4.webidl.TypesParser parser = new org.antlr4.webidl.TypesParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                errorReporter.formatError("TypeParser: %s position %d:%n\t%s%n", typeName, charPositionInLine, msg);
            }
        });
        return parser.type().accept(this);
    }

    @Override
    public Type visitType(TypesParser.TypeContext ctx) {
        if (ctx.simpleType != null && ctx.simpleType.getText() != null) {
            return determineType(ctx.simpleType.getText());
        }
        if (ctx.isArray != null && ctx.isArray != null) {
            return new ArrayType(ctx.isArray.accept(this));
        }
        if (ctx.baseType != null) {
            if (ctx.promiseRest() != null && ctx.promiseRest().type() != null) {
                return new ParameterizedType(determineType(ctx.baseType.getText()),
                        Arrays.asList(typeFactory.boxType(ctx.promiseRest().type().accept(this))));
            }
            if (ctx.promiseRest2() != null && ctx.promiseRest2().type(0) != null
                    && ctx.promiseRest2().type(1) != null) {
                return new ParameterizedType(determineType(ctx.baseType.getText()),
                        Arrays.asList(typeFactory.boxType(ctx.promiseRest2().type(0).accept(this)),
                                typeFactory.boxType(ctx.promiseRest2().type(1).accept(this))));
            }
            return new ParameterizedType(determineType(ctx.baseType.getText()), Collections.emptyList());
        }
        if (ctx.unionType != null) {
            List<Type> types = new ArrayList<>();
            types.add(ctx.unionType.accept(this));
            processUnionTypeRest(ctx.unionTypeRest(), types);
            return new UnionType(types);

        }
        errorReporter.reportError("TypeParser: Unable to parse " + ParserUtil.getText(ctx));
        return new NativeType("Object");
    }

    private void processUnionTypeRest(TypesParser.UnionTypeRestContext ctx, List<Type> types) {
        if (ctx.type() != null) {
            types.add(ctx.type().accept(this));
            processUnionTypeRest(ctx.unionTypeRest(), types);
        }
    }

    private Type determineType(String text) {
        return typeFactory.getTypeNoParse(text);
    }
}
