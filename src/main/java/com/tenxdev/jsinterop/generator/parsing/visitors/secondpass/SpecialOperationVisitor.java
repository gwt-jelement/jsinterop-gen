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
import com.tenxdev.jsinterop.generator.model.SpecialOperationMembers;
import com.tenxdev.jsinterop.generator.model.types.GenericType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.parsing.ParsingContext;
import org.antlr4.webidl.WebIDLParser;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.xtext.xbase.lib.StringExtensions.toFirstUpper;

public class SpecialOperationVisitor extends ContextWebIDLBaseVisitor<SpecialOperationMembers> {

    private String containingType;
    private List<String> extendedAttributes;

    SpecialOperationVisitor(ParsingContext parsingContext, String containingType,
                                   List<String> extendedAttributes) {
        super(parsingContext);
        this.containingType = containingType;
        this.extendedAttributes = extendedAttributes;
    }

    @Override
    public SpecialOperationMembers visitSpecialOperation(WebIDLParser.SpecialOperationContext ctx) {
        String specialOperation = ctx.special().getText();
        Type returnType =
                ctx.returnType() == null ? parsingContext.getTypeFactory().getTypeNoParse("void") :
                        ctx.returnType().type() != null ?
                                ctx.returnType().type().accept(new TypeVisitor(parsingContext))
                                : parsingContext.getTypeFactory().getTypeNoParse(ctx.returnType().getText());
        String name = ctx.IDENTIFIER_WEBIDL() != null && ctx.IDENTIFIER_WEBIDL().getText() != null ?
                ctx.IDENTIFIER_WEBIDL().getText() : null;
        List<MethodArgument> arguments = ctx.argumentList().accept(new ArgumentsVisitor(parsingContext));

        ExtendedAttributes extendedAttributes = new ExtendedAttributes(this.extendedAttributes);
        String genericTypeSpecifiers = null;
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

        SpecialOperationMembers specialOperationMembers = new SpecialOperationMembers();

        String defaultName = getDefaultName(specialOperation, ctx);
        if (defaultName != null) {
            Method defaultMethod = new Method(defaultName, effectiveReturnType, arguments, false,
                    genericTypeSpecifiers, extendedAttributes, replacedReturnedType);
            addMethodBody(defaultMethod, specialOperation);
            specialOperationMembers.getMethods().add(defaultMethod);
        }

        if (name != null) {
            Method namedMethod = new Method(name, effectiveReturnType, arguments, false, genericTypeSpecifiers,
                    extendedAttributes, returnType);
            specialOperationMembers.getMethods().add(namedMethod);
        }
        return specialOperationMembers;
    }

    private void addMethodBody(Method method, String specialOperation) {
        method.setBody(getMethodBody(specialOperation, method.getReturnType(), method.getArguments()));
        if (method.getExtraTypes() == null) {
            method.setExtraTypes(new ArrayList<>());
        }
        method.getExtraTypes().add(parsingContext.getTypeFactory().getTypeNoParse("Js"));
    }

    private String getDefaultName(String specialOperation, WebIDLParser.SpecialOperationContext ctx) {
        switch (specialOperation) {
            case "getter":
                return "get";
            case "setter":
                return "set";
            case "deleter":
                return "delete";
            case "legacycaller":
                return "call";
            default:
                parsingContext.getlogger().formatError("Unknown special operation %s in %s",
                        specialOperation, parsingContext.getTree(ctx));
                return null;
        }
    }

    private String getMethodBody(String specialOperation, Type returnType, List<MethodArgument> arguments) {
        switch (specialOperation) {
            case "getter":
                if (arguments.size() != 1) {
                    parsingContext.getlogger().formatError("Unexpected number of arguments (%d) in setter for %s",
                            arguments.size(), containingType);
                    return null;
                }
                if (returnType.isGwtPrimitiveType()) {
                    return String.format("return Js.get%s(this.object(), %s);",
                            toFirstUpper(returnType.displayValue()),
                            arguments.get(0).getName());
                } else if (returnType.isLongPrimitiveType()) {
                    return String.format("return (long) Js.getDouble(this.object(), %s);",
                            arguments.get(0).getName());
                } else {
                    return String.format("return ($RETURN_TYPE) Js.get(this.object(), %s);",
                            arguments.get(0).getName());
                }
            case "setter":
                if (arguments.size() != 2) {
                    parsingContext.getlogger().formatError("Unexpected number of arguments (%d) in setter for %s",
                            arguments.size(), containingType);
                    return null;
                }
                Type propertyType = arguments.get(1).getType();
                if (propertyType.isGwtPrimitiveType()) {
                    return String.format("%sJs.set(this.object(), %s, %s);",
                            isVoid(returnType) ? "" : "return ",
                            arguments.get(0).getName(), arguments.get(1).getName());
                } else if (propertyType.isLongPrimitiveType()) {
                    return String.format("%sJs.set(this.object(), %s, (double) %s);",
                            isVoid(returnType) ? "" : "return ",
                            arguments.get(0).getName(), arguments.get(1).getName());
                } else {
                    return String.format("%sJs.<%s>set(this.object(), %s, %s);",
                            isVoid(returnType) ? "" : "return ",
                            arguments.get(1).getType().displayValue(),
                            arguments.get(0).getName(), arguments.get(1).getName());
                }
            case "deleter":
                if (arguments.size() != 1) {
                    parsingContext.getlogger().formatError("Unexpected number of arguments (%d) in deleter for %s",
                            arguments.size(), containingType);
                    return null;
                }
                return String.format("%sJs.delete(this.object(), %s);", isVoid(returnType) ? "" : "return ",
                        arguments.get(0).getName());
            case "legacycaller":
                if (arguments.size() != 1) {
                    parsingContext.getlogger().formatError("Unexpected number of arguments (%d) in legacy caller for %s",
                            arguments.size(), containingType);
                    return null;
                }
                return String.format("Js.call(this.object(), %s);", arguments.get(0).getName());
            default:
                return null;
        }
    }

    private boolean isVoid(Type returnType) {
        return "void".equals(returnType.displayValue());
    }
}
