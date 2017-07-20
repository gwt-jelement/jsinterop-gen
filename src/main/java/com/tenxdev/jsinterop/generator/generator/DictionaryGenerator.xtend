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

import com.tenxdev.jsinterop.generator.model.DefinitionInfo
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition
import com.tenxdev.jsinterop.generator.model.DictionaryMember
import com.tenxdev.jsinterop.generator.model.types.Type
import com.tenxdev.jsinterop.generator.model.types.EnumType
import com.tenxdev.jsinterop.generator.model.types.UnionType
import com.tenxdev.jsinterop.generator.model.types.ArrayType
import com.tenxdev.jsinterop.generator.model.types.NativeType

class DictionaryGenerator extends XtendTemplate{

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.getDefinition() as DictionaryDefinition
        return '''
«copyright»
package «basePackageName»«definitionInfo.getPackageName()»;

«imports(basePackageName, definitionInfo)»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.getName»{

    «unionTypes(definition)»
    «FOR member: definition.members»
        «IF member.enumSubstitutionType instanceof EnumType»
            @JsProperty(name="«member.name»")
            public «member.enumSubstitutionType.displayValue» «member.name.adjustJavaName»«defaultValue(member)»;

            @JsOverlay
            public «member.type.displayValue» get«member.name.toFirstUpper»(){
                return «member.type.displayValue».of(this.«member.name.adjustJavaName»);
            }

            @JsOverlay
            public void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                this.«member.name.adjustJavaName» = «member.name.adjustJavaName».getInternalValue();
            }

        «ELSEIF member.enumSubstitutionType instanceof ArrayType»
            @JsProperty(name="«member.name»")
            public «member.enumSubstitutionType.displayValue» «member.name.adjustJavaName»«defaultValue(member)»;

            @JsOverlay
            public «member.type.displayValue» get«member.name.toFirstUpper»(){
                return «(member.type as ArrayType).type.displayValue».ofArray(this.«member.name.adjustJavaName»);
            }

            @JsOverlay
            public void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                this.«member.name.adjustJavaName» = Arrays.stream(«member.name.adjustJavaName»)
                    .map(«(member.type as ArrayType).type.displayValue»::getInternalValue)
                    .toArray(«(member.enumSubstitutionType as ArrayType).type.displayValue»[]::new);
            }

        «ELSE»
            @JsProperty(name="«member.name»")
            public «member.type.displayValue» «member.name.adjustJavaName»«defaultValue(member)»;

        «ENDIF»
    «ENDFOR»

}
    '''
    }

    def enumType(Type type){
        type instanceof EnumType
    }

    def defaultValue(DictionaryMember member){
        if (member.defaultValue=="[]") {
            if(member.enumSubstitutionType!=null)
                return " = new " + member.enumSubstitutionType.displayValue.replace("[]","[0]")
            else
                return " = new " + member.type.displayValue.replace("[]","[0]")
        }else if (member.defaultValue == "null"){
            if (member.type.isNumber)
                return " = 0"
            else
                return " = null"
        } else if (member.defaultValue != null) {
            if(member.type instanceof UnionType)
                return " = Js.cast(" + member.defaultValue +")"
            else if (member.type instanceof EnumType)
                return " = "+member.type.displayValue+".of(" + member.defaultValue + ")"
            else if (member.defaultValue.isDecimal){
                if (member.type.is("float"))
                    return " = " + member.defaultValue+"f"
            }
            " = " + member.defaultValue
        }
    }

    def isDecimal(String value){
        value.matches("-?[0-9]+\\.[0-9]+(e[0-9]+)?")
    }

    def unionTypes(DictionaryDefinition definition)'''
        «IF definition.unionReturnTypes !== null»
            «FOR unionType: definition.unionReturnTypes»
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