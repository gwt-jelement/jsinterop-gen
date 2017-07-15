package com.tenxdev.jsinterop.generator.model;

import java.util.List;

public class PartialInterfaceDefinition extends InterfaceDefinition implements PartialDefinition {

    public PartialInterfaceDefinition(String name, String parent, List<Method> constructors, List<InterfaceMember> members) {
        super(name, parent, constructors, members);
    }

    public PartialInterfaceDefinition(InterfaceDefinition interfaceDefinition) {
        super(interfaceDefinition);
    }
}
