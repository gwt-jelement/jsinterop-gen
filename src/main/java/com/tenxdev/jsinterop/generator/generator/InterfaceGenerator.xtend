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
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition
import java.util.Collections
import com.tenxdev.jsinterop.generator.model.MethodArgument
import com.tenxdev.jsinterop.generator.model.types.NativeType
import com.tenxdev.jsinterop.generator.model.Method

class InterfaceGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.getDefinition() as InterfaceDefinition
        Collections.sort(definition.methods)
        return '''
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
 package «basePackageName»«definitionInfo.getPackageName()»;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

«FOR importName: definitionInfo.importedPackages»
import  «IF importName.startsWith(".")»«basePackageName»«ENDIF»«importName»;
«ENDFOR»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.name.adjustJavaName»«
        IF definition.parent!==null» extends «definition.parent.displayValue»«ENDIF» {

    «FOR unionType: definition.getUnionReturnTypes»
    @JsType(isNative = true, name = "?", namespace = JsPackage.GLOBAL)
    public interface «unionType.name» {
        «FOR type: unionType.types»
        @JsOverlay
        default «type.displayValue» as«type.displayValue.toFirstUpper»(){
            return Js.cast(this);
        }

        «ENDFOR»
        «FOR type: unionType.types»
        @JsOverlay
        default boolean is«type.displayValue.toFirstUpper»(){
            return (Object) this instanceof «type.displayValue»;
        }

        «ENDFOR»
    }

    «ENDFOR»
    «FOR attribute: definition.attributes»
//        @JsProperty(name="«attribute.name»")
//        public native «attribute.type.displayValue» get«attribute.name.toFirstUpper»();
        «IF !attribute.readOnly»

//        @JsProperty(name="«attribute.name»")
//        public native void set«attribute.name.toFirstUpper»(«attribute.type.displayValue» «attribute.name»);
        «ENDIF»

    «ENDFOR»
    «FOR method: definition.methods SEPARATOR "\n"»
        «IF method.enumOverlay===null»
        @JsMethod(name = "«method.name»")
        «IF method.privateMethod»private«ELSE»public«ENDIF» native «method.returnType.displayValue» _«method.name.adjustJavaName»(«
        FOR argument: method.arguments SEPARATOR ", "
        »«argument.type.displayValue» «argument.name.adjustJavaName»«ENDFOR»);
    «ELSE»
        @JsOverlay
        public «method.returnType.displayValue» «method.name.adjustJavaName»(«
        FOR argument: method.arguments SEPARATOR ", "
        »«argument.type.displayValue» «argument.name.adjustJavaName»«ENDFOR»){
            «IF hasReturnType(method)»return «ENDIF»«
                IF method.enumReturnType»«method.returnType.displayValue».of(«ENDIF
                »_«method.name»(«FOR argument: method.enumOverlay.arguments SEPARATOR ", "»«
        enumMethodArgument(argument)»«ENDFOR»«IF method.enumReturnType»)«ENDIF»);
    }
    «ENDIF»«ENDFOR»

}


    '''
    }

    def enumMethodArgument(MethodArgument argument) {
        if(argument.enumSubstitution)
            '''«argument.name».getInternalValue()'''
        else
            argument.name
    }

    def hasReturnType(Method method){
        !(method.returnType instanceof NativeType && (method.returnType as NativeType).typeName === "void")
    }
}