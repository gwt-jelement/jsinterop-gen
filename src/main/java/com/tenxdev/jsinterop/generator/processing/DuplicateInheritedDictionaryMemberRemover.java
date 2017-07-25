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

package com.tenxdev.jsinterop.generator.processing;

import com.tenxdev.jsinterop.generator.logging.Logger;
import com.tenxdev.jsinterop.generator.model.AbstractDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryDefinition;
import com.tenxdev.jsinterop.generator.model.DictionaryMember;
import com.tenxdev.jsinterop.generator.model.Model;

import java.util.ArrayList;
import java.util.List;

public class DuplicateInheritedDictionaryMemberRemover {

    private Model model;
    private Logger logger;

    public DuplicateInheritedDictionaryMemberRemover(Model model, Logger logger) {
        this.model = model;
        this.logger = logger;
    }

    public void process() {
        logger.info(Logger.LEVEL_INFO, () -> "Removing duplicate inherited fields from dictionaries");
        model.getDictionaryDefinitions().stream()
                .filter(definition -> definition.getParent() != null)
                .forEach(this::processDictionary);
    }

    private void processDictionary(DictionaryDefinition definition) {
        List<DictionaryMember> membersToRemove = new ArrayList<>();
        for (DictionaryMember member : definition.getMembers()) {
            if (parentHasMember(definition, member)) {
                membersToRemove.add(member);
            }
        }
        definition.getMembers().removeAll(membersToRemove);
    }

    private boolean parentHasMember(DictionaryDefinition definition, DictionaryMember member) {
        DictionaryDefinition parentDefinition = getParentDefinition(definition);
        return parentDefinition != null &&
                (parentDefinition.getMembers().contains(member) || parentHasMember(parentDefinition, member));
    }

    private DictionaryDefinition getParentDefinition(DictionaryDefinition definition) {
        if (definition.getParent() != null) {
            AbstractDefinition parentDefinition = model.getDefinition(definition.getParent().getTypeName());
            if (parentDefinition instanceof DictionaryDefinition) {
                return (DictionaryDefinition) parentDefinition;
            }
            logger.formatError("DuplicateInheritedDictionaryMemberRemover: Inconsitent parent type for %s%n",
                    definition.getName());
        }
        return null;
    }

}
