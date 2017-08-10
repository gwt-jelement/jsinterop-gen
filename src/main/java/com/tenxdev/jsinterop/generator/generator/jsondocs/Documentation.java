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

package com.tenxdev.jsinterop.generator.generator.jsondocs;

import com.tenxdev.jsinterop.generator.generator.jsondocs.inpit.MemberInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Documentation {
    private Map<String, ClassInfo> documentationMap = new HashMap<>();

    public Optional<String> getClassDescription(String name) {
        ClassInfo classInfo = documentationMap.get(name);
        return classInfo == null ? Optional.empty() : Optional.ofNullable(classInfo.getDescription());
    }

    public Optional<String> getMemberDescription(String className, String memberName) {
        ClassInfo classInfo = documentationMap.get(className);
        if (classInfo != null) {
            MemberInfo memberInfo = classInfo.getMember(memberName);
            return memberInfo == null ? Optional.empty() : Optional.ofNullable(memberInfo.getDescription());
        }
        return Optional.empty();
    }

    public Optional<String> getArgumentDescription(String className, String memberName, int index) {
        ClassInfo classInfo = documentationMap.get(className);
        if (classInfo != null) {
            MemberInfo memberInfo = classInfo.getMember(memberName);
            if (memberInfo != null && memberInfo.getParameters() != null &&
                    memberInfo.getParameters().size() > index) {
                String description = memberInfo.getParameters().get(index).getDescription();
                return description!=null? Optional.of(escape(description)):Optional.empty();
            }
        }
        return Optional.empty();
    }

    void addClassInfo(ClassInfo classInfo) {
        documentationMap.put(classInfo.getName(), classInfo);
    }

    private String escape(String value){
        return value.replace("<","&lt;").replace(">", "&gt;");
    }
}
