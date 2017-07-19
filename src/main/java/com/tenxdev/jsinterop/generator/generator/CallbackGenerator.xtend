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

import com.tenxdev.jsinterop.generator.model.CallbackDefinition
import com.tenxdev.jsinterop.generator.model.DefinitionInfo

class CallbackGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo){
        var definition=definitionInfo.definition as CallbackDefinition
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