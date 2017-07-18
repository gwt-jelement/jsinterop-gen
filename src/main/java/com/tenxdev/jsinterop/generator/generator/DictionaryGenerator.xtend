package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.DefinitionInfo
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition

class DictionaryGenerator {

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.getDefinition() as DictionaryDefinition
        return '''
package «basePackageName»«definitionInfo.getPackageName()»;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

«FOR importName: definitionInfo.getImportedPackages»
import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
«ENDFOR»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.getName»{



}
    '''
    }
}