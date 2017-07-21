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

package com.tenxdev.jsinterop.generator.processing.visitors;

import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDictionaryDefinitionVisitor<T> {

    public T accept(DictionaryDefinition definition) {
        List<T> result = new ArrayList<>();
        result.add(visitMembers(definition.getMembers()));
        result.add(visitParent(definition.getParent()));
        return coallesce(result);
    }

    protected abstract T coallesce(List<T> result);

    protected abstract T visitMembers(List<DictionaryMember> members);

    protected abstract T visitParent(Type parent);
}
