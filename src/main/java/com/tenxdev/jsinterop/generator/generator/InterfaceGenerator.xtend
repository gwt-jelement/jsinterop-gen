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
import com.tenxdev.jsinterop.generator.model.interfaces.ExtendedAttribute

class InterfaceGenerator extends XtendTemplate{

    def generate(String basePackageName, InterfaceDefinition definition){
        Collections.sort(definition.methods)
        return '''
«copyright»
package «basePackageName»«definition.getPackageName()»;

«imports(basePackageName, definition)»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.name.adjustJavaName»«extendsClass(definition)»{
    «constants(definition)»
    «unionTypes(definition)»
    «fields(definition)»
    «constructors(definition)»
    «nonFieldAttributes(definition)»
    «methods(definition)»
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
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.enumSubstitutionType.displayValue» «attribute.javaName.adjustJavaName»;

            «ELSEIF attribute.type instanceof UnionType»
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.type.unionTypeName(definition)» «attribute.javaName.adjustJavaName»;

            «ELSEIF attribute.eventHandler»
                @JsProperty(name="«attribute.name»")
                private «staticModifier(attribute)»«attribute.type.displayValue» «attribute.javaName.adjustJavaName»;

            «ENDIF»
        «ENDFOR»
    '''

    def nonFieldAttributes(InterfaceDefinition definition)'''
        «FOR attribute: definition.attributes»
            «IF attribute.enumSubstitutionType !== null»
                «IF !attribute.writeOnly»
                    «IF attribute.type instanceof ArrayType»
                        @JsOverlay
                        public final «staticModifier(attribute)»«attribute.type.displayValue» get«attribute.name.toFirstUpper»(){
                           return «attribute.type.displayValue.replace("[]","")».ofArray(«attribute.javaName.adjustJavaName»);
                        }

                    «ELSE»
                        @JsOverlay
                        public final «staticModifier(attribute)»«attribute.type.displayValue» get«attribute.name.toFirstUpper»(){
                           return «attribute.type.displayValue».of(«attribute.javaName.adjustJavaName»);
                        }

                    «ENDIF»
                «ENDIF»
                «IF !attribute.readOnly»
                    @JsOverlay
                    public final «staticModifier(attribute)»void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.javaName.adjustJavaName»){
                       «staticThis(attribute)».«attribute.javaName.adjustJavaName» = «attribute.javaName.adjustJavaName».getInternalValue();
                    }

                «ENDIF»
            «ELSEIF attribute.type instanceof UnionType»
                «IF !attribute.writeOnly»
                    @JsOverlay
                    public «staticModifier(attribute)»final «attribute.type.unionTypeName(definition)» get«attribute.name.toFirstUpper»(){
                        return «staticThis(attribute)».«attribute.javaName.adjustJavaName»;
                    }

                «ENDIF»
                «IF !attribute.readOnly»
                    «FOR type: (attribute.type as UnionType).types »
                        @JsOverlay
                        public «staticModifier(attribute)»final void set«attribute.name.toFirstUpper»(«type.displayValue» «attribute.javaName.adjustJavaName»){
                            «staticThis(attribute)».«attribute.javaName.adjustJavaName» = «attribute.type.displayValue».of(«attribute.javaName.adjustJavaName»);
                        }

                    «ENDFOR»
                «ENDIF»
            «ELSEIF attribute.eventHandler»
                @JsOverlay
                public «staticModifier(attribute)»final «attribute.type.displayValue» get«attribute.eventHandlerName»(){
                    return «staticThis(attribute)».«attribute.name.adjustJavaName»;
                }

                @JsOverlay
                public «staticModifier(attribute)»final void set«attribute.eventHandlerName»(«attribute.type.displayValue» «attribute.javaName.adjustJavaName»){
                    «staticThis(attribute)».«attribute.name.adjustJavaName» = «attribute.name.adjustJavaName»;
                }

            «ELSE»
                «IF !attribute.writeOnly»
                    @JsProperty(name="«attribute.name»")
                    public «staticModifier(attribute)»native «attribute.type.displayValue» get«attribute.name.toFirstUpper»();

                «ENDIF»
                «IF !attribute.readOnly»
                    @JsProperty(name="«attribute.name»")
                    public «staticModifier(attribute)»native void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.javaName.adjustJavaName»);

                «ENDIF»
            «ENDIF»
        «ENDFOR»
    '''

    def methods(InterfaceDefinition definition)'''
        «FOR method: definition.methods»
            «IF method.enumOverlay===null»
                @JsMethod(name = "«method.name»")
                public native «returnType(method)» «method.name.adjustJavaName»(«arguments(method)»);

            «ELSE»
                @JsOverlay
                public final «returnType(method)» «method.javaName.adjustJavaName»(«arguments(method)»){
                    «hasReturn(method)»«hasEnumReturnType(method)»«method.name»(«enumMethodArguments(method)»);
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

    def returnType(Method method){
        if (method.hasExtendedAttribute(ExtendedAttribute.GENERIC_RETURN))
            "<T extends "+method.returnType.displayValue+"> T"
        else
            method.returnType.displayValue
    }


    def vararg(MethodArgument argument){
        if (argument.vararg) "..."
    }

    def staticModifier(Attribute attribute){
        if (attribute.static) "static "
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
}