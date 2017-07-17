package com.tenxdev.jsinterop.generator.parsing.visitors.firstpass;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.ContextWebIDLBaseVisitor;
import com.tenxdev.jsinterop.generator.parsing.visitors.secondpass.DictionaryMemberVisitor;
import org.antlr4.webidl.WebIDLParser;

import java.util.List;

public class DictionaryScanner extends ContextWebIDLBaseVisitor<Void>{

    public DictionaryScanner(ParsingContext parsingContext) {
        super(parsingContext);
    }

    @Override
    public Void visitDictionary(WebIDLParser.DictionaryContext ctx) {
        String name=ctx.IDENTIFIER_WEBIDL().getText();
        parsingContetxt.getTypeFactory().registerType(name,
                new ObjectType(name, parsingContetxt.getPackageSuffix()));
        return null;
    }
}
