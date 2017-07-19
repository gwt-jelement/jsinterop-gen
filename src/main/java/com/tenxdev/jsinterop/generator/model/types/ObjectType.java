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

package com.tenxdev.jsinterop.generator.model.types;

public class ObjectType implements Type, PackageType {
    private final String packageName;
    private final String typeName;

    public ObjectType(String typeName, String packageName) {
        this.typeName = typeName;
        this.packageName = packageName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String displayValue() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectType that = (ObjectType) o;

        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectType{" +
                "packageName='" + packageName + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
