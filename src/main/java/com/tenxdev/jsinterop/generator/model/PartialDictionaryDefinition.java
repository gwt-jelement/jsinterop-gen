package com.tenxdev.jsinterop.generator.model;

import java.util.List;

public class PartialDictionaryDefinition extends DictionaryDefinition implements PartialDefinition {

    public PartialDictionaryDefinition(String name, String parent, List<DictionaryMember> members) {
        super(name, parent, members);
    }

    public PartialDictionaryDefinition(DictionaryDefinition dictionaryDefinition) {
        super(dictionaryDefinition.getName(), dictionaryDefinition.getParent(), dictionaryDefinition.getMembers());
    }
}
