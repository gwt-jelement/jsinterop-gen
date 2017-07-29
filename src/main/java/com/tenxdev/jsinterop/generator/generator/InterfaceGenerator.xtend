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

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition
import java.util.Collections
import com.tenxdev.jsinterop.generator.model.MethodArgument
import com.tenxdev.jsinterop.generator.model.types.NativeType
import com.tenxdev.jsinterop.generator.model.Method
import com.tenxdev.jsinterop.generator.model.Attribute
import com.tenxdev.jsinterop.generator.model.types.ArrayType
import com.tenxdev.jsinterop.generator.model.Constructor
import com.tenxdev.jsinterop.generator.model.types.UnionType

class InterfaceGenerator extends XtendTemplate{

    def generate(String basePackageName, InterfaceDefinition definition){
        Collections.sort(definition.methods)
        return '''
«copyright»
package «basePackageName»«definition.getPackageName()»;

«imports(basePackageName, definition)»

@JsType(namespace = JsPackage.GLOBAL, name="«definition.jsTypeName»", isNative = true)
public class «definition.name.adjustJavaName»«generic(definition)»«extendsClass(definition)»{
    «constants(definition)»
    «unionTypes(definition)»
    «fields(definition)»
    «constructors(definition)»
    «nonFieldAttributes(definition)»
    «methods(definition)»
    «IF definition.name=="JsObject"»
        @JsOverlay
        public final T get(String propertyName){
            return JsUtils.<T>get(this, propertyName);
        }

        @JsOverlay
        public final void set(String propertyName, T value){
            JsUtils.<T>set(this, propertyName, value);
        }

        @JsOverlay
        public final void delete(String propertyName){
            JsUtils.<T>delete(this, propertyName);
        }

        @JsOverlay
        public final boolean has(String propertyName){
            return JsUtils.<T>has(this, propertyName);
        }

        @JsOverlay
        public static final <T> JsObject<T> of(String key, T value) {
            return JsUtils.<T>of(key, value);
        }
    «ENDIF»
}
'''
    }

    def extendsClass(InterfaceDefinition definition){
        if (definition.parent!==null)  " extends "+definition.parent.displayValue
    }

    def constants(InterfaceDefinition definition)'''
        «FOR constant: definition.constants AFTER "\n"»
            public static «constant.type.displayValue» «constant.name»;
        «ENDFOR»
    '''

    def constructors(InterfaceDefinition definition)'''
        «FOR constructor: definition.constructors»
            @JsConstructor
            public «definition.name»(«arguments(constructor)»){
                «IF definition.parent!==null»
                    super(«superArguments(constructor)»);
                «ENDIF»
            }

        «ENDFOR»
    '''

    def fields(InterfaceDefinition definition)'''
        «FOR attribute: definition.attributes»
            «IF attribute.enumSubstitutionType !== null»
                «attribute.checkDeprecated»
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.enumSubstitutionType.displayValue» «attribute.name.adjustJavaName»;

            «ELSEIF attribute.type instanceof UnionType»
                «attribute.checkDeprecated»
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.type.unionTypeName(definition)» «attribute.name.adjustJavaName»;

            «ELSEIF attribute.isStatic»
                «attribute.checkDeprecated»
                @JsProperty(name="«attribute.jsPropertyName»")
                public static «attribute.type.displayValue» «attribute.name.adjustJavaName»;

            «ELSEIF attribute.eventHandler»
                «attribute.checkDeprecated»
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.type.displayValue» «attribute.name.adjustJavaName»;

            «ENDIF»
        «ENDFOR»
    '''

    def nonFieldAttributes(InterfaceDefinition definition)'''
        «FOR attribute: definition.attributes»
            «IF attribute.enumSubstitutionType !== null»
                «IF !attribute.writeOnly»
                    «IF attribute.type instanceof ArrayType»
                        «attribute.checkDeprecated»
                        @JsOverlay
                        public final «staticModifier(attribute)»«attribute.type.displayValue» get«attribute.name.toFirstUpper»(){
                           return «attribute.type.displayValue.replace("[]","")».ofArray(«attribute.name.adjustJavaName»);
                        }

                    «ELSE»
                        «attribute.checkDeprecated»
                        @JsOverlay
                        public final «staticModifier(attribute)»«attribute.type.displayValue» get«attribute.name.toFirstUpper»(){
                           return «attribute.type.displayValue».of(«attribute.name.adjustJavaName»);
                        }

                    «ENDIF»
                «ENDIF»
                «IF !attribute.readOnly»
                    «attribute.checkDeprecated»
                    @JsOverlay
                    public final «staticModifier(attribute)»void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.name.adjustJavaName»){
                       «staticThis(attribute)».«attribute.name.adjustJavaName» = «attribute.name.adjustJavaName».getInternalValue();
                    }

                «ENDIF»
            «ELSEIF attribute.type instanceof UnionType»
                «IF !attribute.writeOnly»
                    «attribute.checkDeprecated»
                    @JsOverlay
                    public «staticModifier(attribute)»final «attribute.type.unionTypeName(definition)» get«attribute.name.toFirstUpper»(){
                        return «staticThis(attribute)».«attribute.name.adjustJavaName»;
                    }

                «ENDIF»
                «IF !attribute.readOnly»
                    «FOR type: (attribute.type as UnionType).types »
                        «attribute.checkDeprecated»
                        @JsOverlay
                        public «staticModifier(attribute)»final void set«attribute.name.toFirstUpper»(«type.displayValue» «attribute.name.adjustJavaName»){
                            «staticThis(attribute)».«attribute.name.adjustJavaName» = «attribute.type.displayValue».of(«attribute.name.adjustJavaName»);
                        }

                    «ENDFOR»
                «ENDIF»
            «ELSEIF attribute.eventHandler»
                «attribute.checkDeprecated»
                @JsOverlay
                public «staticModifier(attribute)»final «attribute.type.displayValue» get«attribute.eventHandlerName»(){
                    return «staticThis(attribute)».«attribute.name.adjustJavaName»;
                }

                «attribute.checkDeprecated»
                @JsOverlay
                public «staticModifier(attribute)»final void set«attribute.eventHandlerName»(«attribute.type.displayValue» «attribute.name.adjustJavaName»){
                    «staticThis(attribute)».«attribute.name.adjustJavaName» = «attribute.name.adjustJavaName»;
                }

            «ELSEIF !attribute.isStatic»
                «IF !attribute.writeOnly»
                    «attribute.checkDeprecated»
                    @JsProperty(name="«attribute.name»")
                    public «staticModifier(attribute)»native «attribute.type.displayValue» get«attribute.name.toFirstUpper»();

                «ENDIF»
                «IF !attribute.readOnly»
                    «attribute.checkDeprecated»
                    @JsProperty(name="«attribute.name»")
                    public «staticModifier(attribute)»native void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.name.adjustJavaName»);

                «ENDIF»
            «ENDIF»
        «ENDFOR»
    '''

    def methods(InterfaceDefinition definition)'''
        «FOR method: definition.methods»
            «IF method.enumOverlay===null»
                «method.checkDeprecated»
                @JsMethod(name = "«method.name»")
                public «staticModifier(method)»native «typeSpecifier(method)»«method.returnType.displayValue» «method.javaName.adjustJavaName»(«arguments(method)»);

            «ELSE»
                «method.checkDeprecated»
                @JsOverlay
                public «staticModifier(method)»final «typeSpecifier(method)»«method.returnType.displayValue» «method.javaName.adjustJavaName»(«arguments(method)»){
                    «hasReturn(method)»«hasEnumReturnType(method)»«method.enumOverlay.javaName»(«enumMethodArguments(method)»);
                }

            «ENDIF»
         «ENDFOR»
    '''

    def enumMethodArgument(MethodArgument argument) {
        if(argument.enumSubstitution)
            argument.name+".getInternalValue()"
        else
            argument.name
    }

    def hasReturnType(Method method){
        !(method.returnType instanceof NativeType && (method.returnType as NativeType).typeName === "void")
    }

    def staticThis(Attribute attribute){
        if (attribute.static) attribute.type.displayValue else "this"
    }

    def vararg(MethodArgument argument){
        if (argument.vararg) "..."
    }

    def staticModifier(Attribute attribute){
        if (attribute.static) "static "
    }

    def staticModifier(Method method){
        if (method.static) "static "
    }

    def hasReturn(Method method){
        if (hasReturnType(method)) "return "
    }

    def hasEnumReturnType(Method method){
        if (method.enumReturnType) '''«method.returnType.displayValue».of('''
    }

    def arguments(Method method)'''
        «FOR argument: method.arguments SEPARATOR ", "
            »«argument.type.displayValue»«vararg(argument)» «argument.name.adjustJavaName»«ENDFOR»'''

    def argumentNames(Method method)'''
        «FOR argument: method.arguments SEPARATOR ", "»«argument.name.adjustJavaName»«ENDFOR»'''

    def enumMethodArguments(Method method)'''«FOR argument: method.enumOverlay.arguments SEPARATOR ", "»«enumMethodArgument(argument)»«ENDFOR»«IF method.enumReturnType»)«ENDIF»'''

    def superArguments(Constructor constructor)'''«
        FOR argument: constructor.superArguments SEPARATOR ", "»(«argument.type.displayValue») null«ENDFOR»'''

    def eventHandler(Attribute attribute){
        attribute.name.startsWith("on") && attribute.type.displayValue.startsWith("EventHandler")
    }

    def eventHandlerName(Attribute attribute){
        attribute.name.substring(0,2).toFirstUpper + attribute.name.substring(2).toFirstUpper
    }

    def checkDeprecated(Attribute attribute){
        if (attribute.deprecated) "@Deprecated\n"
    }

    def checkDeprecated(Method method){
        if (method.deprecated) "@Deprecated\n"
    }

    def generic(InterfaceDefinition definition){
        if (definition.genericParameter!==null) '''<«definition.genericParameter»>'''
    }

    def typeSpecifier(Method method){
        if (method.genericTypeSpecifiers!==null) '''<«method.genericTypeSpecifiers»> '''
    }
}