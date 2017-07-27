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

package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.interfaces.InterfaceMember;
import com.tenxdev.jsinterop.generator.model.types.Type;

public class Feature implements InterfaceMember {

    private final FeatureType featureType;
    private final boolean readOnly;
    private final Type valueType;
    private final Type keyType;

    public Feature(FeatureType featureType, Type valueType, boolean readOnly,
                   ExtendedAttributes extendedAttributes) {
        this(featureType, null, valueType, readOnly, extendedAttributes);
    }

    public Feature(FeatureType featureType, Type keyType, Type valueType, boolean readOnly,
                   ExtendedAttributes extendedAttributes) {
        this.featureType = featureType;
        this.valueType = valueType;
        this.keyType = keyType;
        this.readOnly = readOnly;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Type getValueType() {
        return valueType;
    }

    public Type getKeyType() {
        return keyType;
    }

    public boolean isPairIterator() {
        return keyType != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (readOnly != feature.readOnly) return false;
        if (featureType != feature.featureType) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!valueType.equals(feature.valueType)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return keyType.equals(feature.keyType);
    }

    @Override
    public int hashCode() {
        int result = featureType != null ? featureType.hashCode() : 0;
        result = 31 * result + (readOnly ? 1 : 0);
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        result = 31 * result + (keyType != null ? keyType.hashCode() : 0);
        return result;
    }

    public enum FeatureType {VALUE_ITERATOR, MAP_ITERATOR, MAP_LIKE, SET_LIKE, STRINGIFIER}
}
