package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.CallbackDefinition
import com.tenxdev.jsinterop.generator.model.DefinitionInfo

class CallbackGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.definition as CallbackDefinition
        return '''
package «basePackageName»«definitionInfo.getPackgeName()»;

import jsinterop.annotations.JsFunction;
«FOR importName: definitionInfo.importedPackages»
import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
«ENDFOR»

@JsFunction
public interface «definition.name»{
    «definition.method.returnType.displayValue» «definition.method.callbackMethodName»(«
        FOR argument: definition.method.arguments SEPARATOR ", "
    »«argument.type.displayValue» «argument.name»«ENDFOR»);
}
    '''
    }

}