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
package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.AbstractDefinition
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition
import com.tenxdev.jsinterop.generator.model.types.Type
import com.tenxdev.jsinterop.generator.model.types.EnumType
import com.tenxdev.jsinterop.generator.model.types.ArrayType

class DictionaryGenerator extends XtendTemplate{

    def generate(String basePackageName, DictionaryDefinition definition){
        return '''
«copyright»
package «basePackageName»«definition.getPackageName()»;

«imports(basePackageName, definition)»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.getName»«
        IF definition.parent!==null» extends «definition.parent.displayValue»«ENDIF»{

    «unionTypes(definition)»
    «FOR member: definition.members»
        «IF member.enumSubstitutionType instanceof EnumType»
            @JsProperty(name="«member.name»")
            public «member.enumSubstitutionType.displayValue» «member.name.adjustJavaName»;

            @JsOverlay
            public final «member.type.displayValue» get«member.name.toFirstUpper»(){
                return «member.type.displayValue».of(this.«member.name.adjustJavaName»);
            }

            @JsOverlay
            public final void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                this.«member.name.adjustJavaName» = «member.name.adjustJavaName».getInternalValue();
            }

        «ELSEIF member.enumSubstitutionType instanceof ArrayType»
            @JsProperty(name="«member.name»")
            public «member.enumSubstitutionType.displayValue» «member.name.adjustJavaName»;

            @JsOverlay
            public final «member.type.displayValue» get«member.name.toFirstUpper»(){
                return «(member.type as ArrayType).type.displayValue».ofArray(this.«member.name.adjustJavaName»);
            }

            @JsOverlay
            public final void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                this.«member.name.adjustJavaName» = Arrays.stream(«member.name.adjustJavaName»)
                    .map(«(member.type as ArrayType).type.displayValue»::getInternalValue)
                    .toArray(«(member.enumSubstitutionType as ArrayType).type.displayValue»[]::new);
            }

        «ELSE»
            @JsProperty(name="«member.name»")
            public «member.type.displayValue» «member.name.adjustJavaName»;

        «ENDIF»
    «ENDFOR»

}
    '''
    }

    def enumType(Type type){
        type instanceof EnumType
    }

    def isDecimal(String value){
        value.matches("-?[0-9]+\\.[0-9]+(e[0-9]+)?")
    }

    def unionTypes(DictionaryDefinition definition)'''
        «IF definition.unionReturnTypes !== null»
            «FOR unionType: definition.unionReturnTypes»
                «IF unionType.owner==definition»
                    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                    public interface «unionType.name» {
                        «FOR type: unionType.types»
                        @JsOverlay
                        default «type.displayValue» as«type.displayValue.toFirstUpper.adjust»(){
                            return Js.cast(this);
                        }

                        «ENDFOR»
                        «FOR type: unionType.types»
                        @JsOverlay
                        default boolean is«type.displayValue.toFirstUpper.adjust»(){
                            return (Object) this instanceof «(boxType(type).displayValue).removeGeneric»;
                        }

                        «ENDFOR»
                    }

                «ENDIF»
            «ENDFOR»
        «ENDIF»
    '''

    def removeGeneric(String value){
        value.replaceAll("<.*?>","")
    }

    def adjust(String value){
        value.replace("[]", "Array").removeGeneric;
    }

}