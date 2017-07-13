package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.InterfaceDefinition

class InterfaceGenerator {

    def String generate(InterfaceDefinition defs, String packageBase, String packageSuffix)
        '''
package «packageBase».«packageSuffix»;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative=true, namespace = JsPackage.GLOBAL)
public interface «defs.getName()» «IF defs.getParent()!==null»extends «defs.getParent()»«ENDIF»{

}

'''
}