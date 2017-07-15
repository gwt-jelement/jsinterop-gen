package com.tenxdev.jsinterop.generator.model;

import java.util.Collections;

public class ImplementsDefinition extends InterfaceDefinition{

    public ImplementsDefinition(String name, String parent) {
        super(name, parent, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public String toString() {
        return "\n"+getName()+" implements "+getParent();
    }

}
