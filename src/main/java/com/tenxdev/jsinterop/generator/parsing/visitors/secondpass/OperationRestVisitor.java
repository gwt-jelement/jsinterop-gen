/*
 * Copyright 2017 Abed Tony BenBrahim <tony.benrahim@10xdev.com>
 *     and Gwt-JElement project contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tenxdev.jsinterop.generator.parsing.visitors.secondpass;

import com.tenxdev.jsinterop.generator.model.ExtendedAttributes;
import com.tenxdev.jsinterop.generator.model.Method;
import com.tenxdev.jsinterop.generator.model.MethodArgument;
import com.tenxdev.jsinterop.generator.model.types.GenericType;
import com.tenxdev.jsinterop.generator.model.types.ParameterisedType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class OperationRestVisitor extends ContextWebIDLBaseVisitor<Method> {

    private final Type returnType;
    private final boolean staticMethod;
    private final List<String> extendedAttributes;

    OperationRestVisitor(ParsingContext context, Type returnType, boolean staticMethod, List<String> extendedAttributes) {
        super(context);
        this.returnType = returnType;
        this.staticMethod = staticMethod;
        this.extendedAttributes = extendedAttributes;
    }

    @Override
    public Method visitOperationRest(WebIDLParser.OperationRestContext ctx) {
        String name = ctx.optionalIdentifier().getText();
        List<MethodArgument> parameters = ctx.argumentList() == null || ctx.argumentList().argument() == null ? Collections.emptyList() :
                ctx.argumentList().accept(new ArgumentsVisitor(parsingContext));
        String genericTypeSpecifiers = null;
        ExtendedAttributes extendedAttributes = new ExtendedAttributes(this.extendedAttributes);
        Type effectiveReturnType = returnType;
        Type replacedReturnedType = null;
        if (extendedAttributes.hasExtendedAttribute(ExtendedAttributes.GENERIC_RETURN)) {
            genericTypeSpecifiers = "T extends " + returnType.displayValue();
            effectiveReturnType = new GenericType("T");
            replacedReturnedType = returnType;
        }
        String genericSubstitution = extendedAttributes.extractValue(ExtendedAttributes.GENERIC_SUB, null);
        if (genericSubstitution != null) {
            effectiveReturnType = new GenericType(genericSubstitution);
        }
        String[] genericParameterNames = extendedAttributes.extractValues(ExtendedAttributes.GENERIC_PARAMETER, null);
        if (genericParameterNames != null) {
            List<Type> genericParameters = Arrays.stream(genericParameterNames)
                    .map(param -> param.length() == 1 ?
                            new GenericType(param) :
                            parsingContext.getTypeFactory().getTypeNoParse(param))
                    .collect(Collectors.toList());
            effectiveReturnType = new ParameterisedType(returnType, genericParameters);
            replacedReturnedType = returnType;
        }
        return new Method(name, effectiveReturnType, parameters, staticMethod, genericTypeSpecifiers,
                extendedAttributes, replacedReturnedType);
    }

}
