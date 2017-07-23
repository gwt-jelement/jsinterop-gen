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
    «constructors(definition)»
    «attributes(definition)»
    «methods(definition)»
}
'''
    }

    def extendsClass(InterfaceDefinition definition){
        if (definition.parent!==null)  " extends "+definition.parent.displayValue
    }

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

    def enumMethodArgument(MethodArgument argument) {
        if(argument.enumSubstitution)
            argument.name+".getInternalValue()"
        else
            argument.name
    }

    def hasReturnType(Method method){
        !(method.returnType instanceof NativeType && (method.returnType as NativeType).typeName === "void")
    }

    def constants(InterfaceDefinition definition)'''
        «FOR constant: definition.constants AFTER "\n"»
            public static «constant.type.displayValue» «constant.name»;
        «ENDFOR»

    '''

    def unionTypes(InterfaceDefinition definition)'''
        «FOR unionType: definition.unionReturnTypes»
            «IF unionType.owner===definition»
                @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
                public interface «unionType.name» {
                    «FOR type: unionType.types»
                    @JsOverlay
                    static «unionType.name» of(«type.displayValue» value){
                        return Js.cast(value);
                    }

                    «ENDFOR»
                    «FOR type: unionType.types»
                    @JsOverlay
                    default «type.displayValue» as«type.displayValue.toFirstUpper»(){
                        return Js.cast(this);
                    }

                    «ENDFOR»
                    «FOR type: unionType.types»
                    @JsOverlay
                    default boolean is«type.displayValue.toFirstUpper»(){
                        return (Object) this instanceof «(boxType(type).displayValue)»;
                    }

                    «ENDFOR»
                }

            «ENDIF»
        «ENDFOR»
    '''

    def attributes(InterfaceDefinition definition)'''
        «FOR attribute: definition.attributes»
            «IF attribute.enumSubstitutionType !== null»
                @JsProperty(name="«attribute.name»")
                public «staticModifier(attribute)»«attribute.enumSubstitutionType.displayValue» «attribute.javaName.adjustJavaName»;

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
            «ELSE»
                @JsProperty(name="«attribute.name»")
                public «staticModifier(attribute)»«attribute.type.displayValue» «attribute.javaName.adjustJavaName»;

            «ENDIF»
        «ENDFOR»
    '''

    def staticThis(Attribute attribute){
        if (attribute.static) attribute.type.displayValue else "this"
    }

    def readOnlyAttributes(InterfaceDefinition definition)'''
        «FOR attribute: definition.readOnlyAttributes»
            «IF attribute.enumSubstitutionType!==null»
                «readOnlyEnumAttribute(attribute)»
            «ELSE»
                @JsProperty(name="«attribute.name»")
                public «staticModifier(attribute)»native «attribute.type.displayValue» get«attribute.javaName.toFirstUpper»();

            «ENDIF»
        «ENDFOR»
    '''

    def readOnlyEnumAttribute(Attribute attribute)'''
        «IF attribute.type instanceof ArrayType»
            @JsOverlay
            public «staticModifier(attribute)»final «attribute.type.displayValue» get«attribute.name.toFirstUpper»As«attribute.type.displayValue.sanitizeName»(){
                return «(attribute.type as ArrayType).type.displayValue».ofArray(get«attribute.name.toFirstUpper»());
            }
        «ELSE»
            @JsOverlay
            public «staticModifier(attribute)»final «attribute.type.displayValue» get«attribute.name.toFirstUpper»As«attribute.type.displayValue.sanitizeName»(){
                return «attribute.type.displayValue».of(get«attribute.name.toFirstUpper»());
            }
        «ENDIF»

        @JsProperty(name="«attribute.name»")
        public «staticModifier(attribute)»native «attribute.enumSubstitutionType.displayValue» get«attribute.name.toFirstUpper»();

    '''

    def writeOnlyAttributes(InterfaceDefinition definition)'''
        «FOR attribute: definition.writeOnlyAttributes»
            «IF attribute.enumSubstitutionType!==null»
                «writeOnlyEnumAttribute(attribute)»
            «ELSE»
                @JsOverlay
                public «staticModifier(attribute)»final void set«attribute.javaName.toFirstUpper»(«attribute.type.displayValue» «adjustJavaName(attribute.javaName)»){
                    this.«(attribute.reference?:attribute).javaName» = Js.cast(«adjustJavaName(attribute.javaName)»);
                }

            «ENDIF»
        «ENDFOR»
    '''

    def writeOnlyEnumAttribute(Attribute attribute)'''
        @JsOverlay
        public «staticModifier(attribute)»final void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.name»){
            set«attribute.name.toFirstUpper»(«attribute.name».getInternalValue());
        }

        @JsProperty(name="«attribute.name»")
        public «staticModifier(attribute)»native void set«attribute.name.toFirstUpper»(«attribute.enumSubstitutionType.displayValue» «attribute.name»);

    '''

    def methods(InterfaceDefinition definition)'''
        «FOR method: definition.methods SEPARATOR "\n"»
            «IF method.enumOverlay===null»
                «nativeMethod(method)»
            «ELSE»
                «enumOverlayMethod(method)»
            «ENDIF»
         «ENDFOR»

    '''

    def nativeMethod(Method method)'''
        @JsMethod(name = "«method.name»")
        public native «method.returnType.displayValue» «method.name.adjustJavaName»(«arguments(method)»);
    '''

    def enumOverlayMethod(Method method)'''
        @JsOverlay
        public final «method.returnType.displayValue» «method.javaName.adjustJavaName»(«arguments(method)»){
            «hasReturn(method)»«hasEnumReturnType(method)»«method.name»(«enumMethodArguments(method)»);
        }
    '''

    def vararg(MethodArgument argument){
        if (argument.vararg) "..."
    }

    def accessModifier(Method method){
        if (method.privateMethod) "private" else "public"
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

}