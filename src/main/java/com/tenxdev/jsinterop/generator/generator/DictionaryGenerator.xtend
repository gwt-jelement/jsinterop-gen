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

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition
import com.tenxdev.jsinterop.generator.model.types.ArrayType
import com.tenxdev.jsinterop.generator.model.types.UnionType

class DictionaryGenerator extends XtendTemplate{

    def generate(String basePackageName, DictionaryDefinition definition){
        return '''
«copyright»
package «basePackageName»«definition.getPackageName()»;

«imports(basePackageName, definition)»

@JsType(name="Object", namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.name»«
        IF definition.parent!==null» extends «definition.parent.displayValue»«ENDIF»{

    public «definition.name»(){
    }

    «unionTypes(definition)»
    «FOR member: definition.members»
        «IF member.enumSubstitutionType !== null»
            «IF member.enumSubstitutionType instanceof ArrayType»
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
                public «member.enumSubstitutionType.displayValue» «member.name.adjustJavaName»;

                @JsOverlay
                public final «member.type.displayValue» get«member.name.toFirstUpper»(){
                    return «member.type.displayValue».of(this.«member.name.adjustJavaName»);
                }

                @JsOverlay
                public final void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                    this.«member.name.adjustJavaName» = «member.name.adjustJavaName».getInternalValue();
                }

            «ENDIF»
        «ELSEIF member.type instanceof UnionType »
            @JsProperty(name="«member.name»")
            public «member.type.unionTypeName(definition)» «member.name.adjustJavaName»;

            «FOR type: (member.type as UnionType).types»
                @JsOverlay
                public final void set«member.name.toFirstUpper»(«type.displayValue» «member.name.adjustJavaName»){
                    this.«member.name.adjustJavaName» = «member.type.displayValue».of(«member.name.adjustJavaName»);
                }

            «ENDFOR»
        «ELSE»
            @JsProperty(name="«member.name»")
            public «member.type.displayValue» «member.name.adjustJavaName»;

            @JsOverlay
            public final «member.type.displayValue» get«member.name.toFirstUpper»(){
                return this.«member.name.adjustJavaName»;
            }

            @JsOverlay
            public final void set«member.name.toFirstUpper»(«member.type.displayValue» «member.name.adjustJavaName»){
                this.«member.name.adjustJavaName» = «member.name.adjustJavaName»;
            }

        «ENDIF»
    «ENDFOR»

}
    '''
    }

}