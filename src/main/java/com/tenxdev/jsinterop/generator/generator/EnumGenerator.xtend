package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.DefinitionInfo;
import com.tenxdev.jsinterop.generator.model.EnumDefinition;

class EnumGenerator extends Template {

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.getDefinition() as EnumDefinition;
        return '''
package «basePackageName»«definitionInfo.getPackgeName()»;

public enum «definition.getName»{
    «FOR value: definition.getValues() SEPARATOR ",\n" AFTER ";\n"
        »«value.enumValueToJavaName().toUpperCase()»(«value»)«ENDFOR»

    private String internalValue;

    «definition.getName()»(String internalValue){
        this.internalValue = internalValue;
    }

    public String getInternalValue(){
        return this.internalValue;
    }
}
    '''
    }
}