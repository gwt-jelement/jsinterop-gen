package com.tenxdev.jsinterop.generator.model;

import com.tenxdev.jsinterop.generator.model.types.Type;

import java.util.List;

public class PartialInterfaceDefinition extends InterfaceDefinition implements PartialDefinition {

    public PartialInterfaceDefinition(String name, Type parent, List<Method> constructors, List<InterfaceMember> members) {
        super(name, parent, constructors, members);
    }

    public PartialInterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        super(interfaceDefinition);
    }
}
