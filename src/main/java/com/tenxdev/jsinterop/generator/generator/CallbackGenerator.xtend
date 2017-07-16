package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.CallbackDefinition
import com.tenxdev.jsinterop.generator.model.DefinitionInfo
import com.tenxdev.jsinterop.generator.processing.TypeMapper

class CallbackGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo, TypeMapper typeMapper){
        var definition=definitionInfo.definition as CallbackDefinition
        return '''
package «basePackageName»«definitionInfo.getPackgeName()»;

import jsinterop.annotations.JsFunction;
«FOR importName: definitionInfo.importedPackages»
import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
«ENDFOR»

@JsFunction
public interface «definition.name»{
    «typeMapper.mapType(definition.method.firstReturnType)» «definition.method.callbackMethodName»(«
        FOR argument: definition.method.arguments SEPARATOR ", "
    »«typeMapper.mapType(argument.types.get(0))» «argument.name»«ENDFOR»);
}
    '''
    }

}