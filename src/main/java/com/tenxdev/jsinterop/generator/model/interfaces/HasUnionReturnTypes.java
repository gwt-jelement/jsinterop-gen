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

package com.tenxdev.jsinterop.generator.model.interfaces;

import com.tenxdev.jsinterop.generator.model.types.UnionType;

import java.util.List;

public interface HasUnionReturnTypes extends Definition {

    List<UnionType> getUnionReturnTypes();

    void setUnionReturnTypes(List<UnionType> unionReturnTypes);

    default void addUnionReturnType(UnionType unionType) {
        if (!getUnionReturnTypes().contains(unionType)) {
            getUnionReturnTypes().add(unionType);
        }
        if (unionType.getOwner() == null) {
            unionType.setOwner(getName());
        }
    }
}
