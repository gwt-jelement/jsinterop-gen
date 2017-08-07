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

package com.tenxdev.jsinterop.generator.processing.packageusage;

import com.tenxdev.jsinterop.generator.model.*;
import com.tenxdev.jsinterop.generator.model.types.ObjectType;
import com.tenxdev.jsinterop.generator.model.types.Type;
import com.tenxdev.jsinterop.generator.processing.visitors.AbstractModelVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PackageUsageModelVisitor extends AbstractModelVisitor<List<String>> {

    private static final List<String> NO_IMPLEMENTATION = Collections.emptyList();
    private Type jsType;

    @Override
    public Map<AbstractDefinition, List<String>> accept(Model model) {
        jsType =model.getTypeFactory().getTypeNoParse("Js");
        return super.accept(model);
    }

    @Override
    protected List<String> visitDefinition(Model model, AbstractDefinition definition) {
        List<String> imports=new ArrayList<>();
        if (definition.getExtension()!=null){
            imports.addAll(getExtensionImports(model, definition));
        }
        imports.addAll(super.visitDefinition(model, definition));
        return imports;
    }

    private List<String> getExtensionImports(Model model, AbstractDefinition definition) {
        List<String> imports = new ArrayList<>();
        definition.getExtension().getImports().forEach(importName->{
            Type type=model.getTypeFactory().getType(importName);
            if (type instanceof ObjectType){
                imports.add(((ObjectType)type).getPackageName()+"."+importName);
            }
            //TODO should log type not found
        });
        return imports;
    }

    @Override
    protected List<String> visitTypeDefinition(TypeDefinition definition) {
        return NO_IMPLEMENTATION;
    }

    @Override
    protected List<String> visitEnumDefinition(EnumDefinition definition) {
        return NO_IMPLEMENTATION;
    }

    @Override
    protected List<String> visitCallbackDefinition(CallbackDefinition definition) {
        return new MethodVisitor().accept(definition.getMethod());
    }

    @Override
    protected List<String> visitDictionaryDefinition(DictionaryDefinition definition) {
        return new DictionaryDefinitionUsageVisitor(jsType).accept(definition);
    }

    @Override
    public List<String> visitInterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        return new InterfaceDefinitionUsageVisitor(jsType).accept(interfaceDefinition);
    }

    @Override
    protected List<String> visitImplementsDefinition(ImplementsDefinition definition) {
        return NO_IMPLEMENTATION;
    }


}
