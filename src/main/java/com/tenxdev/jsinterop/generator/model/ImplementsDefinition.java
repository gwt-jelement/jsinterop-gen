package com.tenxdev.jsinterop.generator.model;

import java.util.Collections;

public class ImplementsDefinition extends InterfaceDefinition{

    public ImplementsDefinition(String name, String parent) {
        super(name, parent, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public boolean isPartial() {
        return true;
    }

    @Override
    public String toString() {
        return "\n"+getName()+" implements "+getParent();
    }

    public static boolean is(Definition definition){
        return definition.getClass().equals(ImplementsDefinition.class);
    }
}
