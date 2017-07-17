package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.DefinitionInfo
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition
import java.util.Collections

class InterfaceGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.getDefinition() as InterfaceDefinition
        Collections.sort(definition.methods)
        return '''
package «basePackageName»«definitionInfo.getPackgeName()»;

«IF !definition.methods.empty»import jsinterop.annotations.JsMethod;«ENDIF»
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

«FOR importName: definitionInfo.importedPackages»
import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
«ENDFOR»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.name.adjustJavaName»«
        IF definition.parent!==null» extends «definition.parent.displayValue»«ENDIF» {
    «FOR method: definition.methods»

    @JsMethod(name = "«method.name»")
    public native «method.returnType.displayValue» «method.name.adjustJavaName»(«
        FOR argument: method.arguments SEPARATOR ", "
        »«argument.type.displayValue» «argument.name.adjustJavaName»«ENDFOR»);
    «ENDFOR»

}


    '''
    }
}