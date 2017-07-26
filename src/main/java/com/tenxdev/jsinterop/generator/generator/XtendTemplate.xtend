package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.AbstractDefinition
import java.util.Set
import java.util.TreeSet
import java.util.Arrays
import com.tenxdev.jsinterop.generator.model.Method
import com.tenxdev.jsinterop.generator.model.types.NativeType
import com.tenxdev.jsinterop.generator.model.types.Type
import com.tenxdev.jsinterop.generator.processing.TypeFactory
import javax.annotation.Nonnull
import com.tenxdev.jsinterop.generator.model.interfaces.HasUnionReturnTypes
import com.tenxdev.jsinterop.generator.model.types.UnionType

class XtendTemplate {

    static final Set<String> JAVA_RESERVED_KEYWORDS = new TreeSet<String>(
            Arrays.asList("abstract", "continue", "for", "new", "switch",
            "assert", "default", "goto", "package", "synchronized",
            "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while", "_"));

    def copyright()'''
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
    '''

    def imports(String basePackageName, AbstractDefinition definition)'''
        «FOR importName: definition.getImportedPackages»
            import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
        «ENDFOR»
    '''

    def sanitizeName(String name){
        name.replace("[]","Array    ").replaceAll("<\\.?>","")
    }

    def adjustJavaName(String name) {
        if (JAVA_RESERVED_KEYWORDS.contains(name))  name + "_" else name
    }

    def getCallbackMethodName(Method method) {
        if (method.getName() === null || method.getName().isEmpty() ) "callback" else method.getName()
    }

    def boxType(Type type) {
        if (type instanceof NativeType) {
            var boxedType = TypeFactory.BOXED_TYPES.get(type.getTypeName());
            return if (boxedType !== null)  boxedType else type;
        }
        //TODO may need to box other types
        return type;
    }

    def enumValueToJavaName(@Nonnull String value) {
        var result = value;
        if (value.startsWith("\"") && value.endsWith("\"")) {
            result = value.substring(1, value.length() - 1);
        }
        if (result.isEmpty()) {
            result = "NONE";
        }
        if (!Character.isAlphabetic(result.charAt(0))) {
            result = "_" + result;
        }
        return result.replace('-', '_').replace('/', '_').replace('+', '_');
    }

    def unionTypeName(Type type, AbstractDefinition definition){
        type.displayValue.replace(definition.name+".","")
    }

    def unionTypes(HasUnionReturnTypes definition)'''
        «FOR unionType: definition.unionReturnTypes»
            «IF (unionType as UnionType).owner === definition»
                @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                public interface «(unionType as UnionType).name» {
                    «FOR type: (unionType as UnionType).types»
                        @JsOverlay
                        static «(unionType as UnionType).name» of(«type.displayValue» value){
                            return Js.cast(value);
                        }

                    «ENDFOR»
                    «FOR type: (unionType as UnionType).types»
                        @JsOverlay
                        default «type.displayValue» as«type.displayValue.toFirstUpper.adjustName»(){
                            return Js.cast(this);
                        }

                    «ENDFOR»
                    «FOR type: (unionType as UnionType).types»
                        @JsOverlay
                        default boolean is«type.displayValue.toFirstUpper.adjustName»(){
                            return (Object) this instanceof «(boxType(type).displayValue.removeGeneric)»;
                        }

                    «ENDFOR»
                }

            «ENDIF»
        «ENDFOR»
    '''

    def adjustName(String value){
        value.removeGeneric.adjustArray;
    }

    def removeGeneric(String value){
        value.replaceAll("<.*?>","")
    }

    def adjustArray(String value){
        value.replace("[]", "Array").removeGeneric;
    }


}